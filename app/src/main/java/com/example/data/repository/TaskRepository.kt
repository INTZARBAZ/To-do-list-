package com.example.data.repository

import com.example.data.local.CategoryDao
import com.example.data.local.TaskDao
import com.example.data.model.CategoryEntity
import com.example.data.model.Priority
import com.example.data.model.SubtaskEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class TaskRepository(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) {

    val allTasksWithSubtasks: Flow<List<TaskWithSubtasks>> = taskDao.getAllTasksWithSubtasks()
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    fun getTaskById(id: Long): Flow<TaskWithSubtasks?> = taskDao.getTaskWithSubtasksById(id)

    suspend fun insertTaskWithSubtasks(task: TaskEntity, subtaskTitles: List<String>): Long {
        val taskId = taskDao.insertTask(task)
        if (subtaskTitles.isNotEmpty()) {
            val subtasks = subtaskTitles.filter { it.isNotBlank() }.map { title ->
                SubtaskEntity(taskId = taskId, title = title, isCompleted = false)
            }
            if (subtasks.isNotEmpty()) {
                taskDao.insertSubtasks(subtasks)
            }
        }
        return taskId
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun updateTaskWithSubtasks(
        task: TaskEntity,
        subtasks: List<SubtaskEntity>,
        newSubtaskTitles: List<String>
    ) {
        taskDao.updateTask(task)
        // Update existing subtasks
        subtasks.forEach { subtask ->
            taskDao.updateSubtask(subtask)
        }
        // Insert new subtasks
        val newSubtasks = newSubtaskTitles.filter { it.isNotBlank() }.map { title ->
            SubtaskEntity(taskId = task.id, title = title, isCompleted = false)
        }
        if (newSubtasks.isNotEmpty()) {
            taskDao.insertSubtasks(newSubtasks)
        }
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun toggleSubtaskCompletion(subtask: SubtaskEntity) {
        taskDao.updateSubtask(subtask.copy(isCompleted = !subtask.isCompleted))
    }

    suspend fun addSubtask(taskId: Long, title: String) {
        if (title.isNotBlank()) {
            taskDao.insertSubtask(SubtaskEntity(taskId = taskId, title = title, isCompleted = false))
        }
    }

    suspend fun deleteSubtask(subtask: SubtaskEntity) {
        taskDao.deleteSubtask(subtask)
    }

    suspend fun addCategory(name: String, colorHex: String, iconName: String = "Bookmark") {
        categoryDao.insertCategory(CategoryEntity(name = name, colorHex = colorHex, iconName = iconName))
    }

    suspend fun seedDefaultsIfEmpty() {
        if (categoryDao.getCategoryCount() == 0) {
            val defaultCategories = listOf(
                CategoryEntity(name = "Personal", colorHex = "#D8B4F8", iconName = "Person"),
                CategoryEntity(name = "Work", colorHex = "#A3C4F3", iconName = "Work"),
                CategoryEntity(name = "Health", colorHex = "#B8E0D2", iconName = "Favorite"),
                CategoryEntity(name = "Study", colorHex = "#FFF1C5", iconName = "School"),
                CategoryEntity(name = "Shopping", colorHex = "#FFC2D1", iconName = "ShoppingBag")
            )
            defaultCategories.forEach { categoryDao.insertCategory(it) }

            // Seed initial sample pastel tasks
            val cal = Calendar.getInstance()
            val todayMillis = cal.timeInMillis

            val task1Id = taskDao.insertTask(
                TaskEntity(
                    title = "Plan weekly goals & pastel moodboard",
                    notes = "Organize tasks for the week in soft lavender palette",
                    category = "Personal",
                    categoryColorHex = "#D8B4F8",
                    priority = Priority.HIGH.name,
                    dueDateMillis = todayMillis,
                    dueTimeHour = 10,
                    dueTimeMinute = 30,
                    isCompleted = false
                )
            )
            taskDao.insertSubtasks(
                listOf(
                    SubtaskEntity(taskId = task1Id, title = "Review calendar schedule", isCompleted = true),
                    SubtaskEntity(taskId = task1Id, title = "Set priorities & tags", isCompleted = false)
                )
            )

            val task2Id = taskDao.insertTask(
                TaskEntity(
                    title = "Evening mindfulness & meditation",
                    notes = "15-minute relaxation exercise with ocean theme",
                    category = "Health",
                    categoryColorHex = "#B8E0D2",
                    priority = Priority.LOW.name,
                    dueDateMillis = todayMillis,
                    dueTimeHour = 20,
                    dueTimeMinute = 0,
                    isCompleted = false
                )
            )
            taskDao.insertSubtasks(
                listOf(
                    SubtaskEntity(taskId = task2Id, title = "Prepare calm space", isCompleted = false)
                )
            )

            // Seed task for tomorrow
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrowMillis = cal.timeInMillis
            val task3Id = taskDao.insertTask(
                TaskEntity(
                    title = "Design review & UI sprint prep",
                    notes = "Check Flutter/Compose pastel cards and subtask component specs",
                    category = "Work",
                    categoryColorHex = "#A3C4F3",
                    priority = Priority.URGENT.name,
                    dueDateMillis = tomorrowMillis,
                    dueTimeHour = 14,
                    dueTimeMinute = 15,
                    isCompleted = false
                )
            )
            taskDao.insertSubtasks(
                listOf(
                    SubtaskEntity(taskId = task3Id, title = "Export design assets", isCompleted = false),
                    SubtaskEntity(taskId = task3Id, title = "Update status board", isCompleted = false)
                )
            )
        }
    }
}
