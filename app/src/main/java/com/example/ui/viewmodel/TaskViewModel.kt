package com.example.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CategoryEntity
import com.example.data.model.PastelPaletteType
import com.example.data.model.SubtaskEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskWithSubtasks
import com.example.data.preferences.PassAndThemeManager
import com.example.data.repository.TaskRepository
import com.example.service.NotificationHelper
import com.example.service.WidgetService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val passAndThemeManager: PassAndThemeManager

    val allTasks: StateFlow<List<TaskWithSubtasks>>
    val allCategories: StateFlow<List<CategoryEntity>>
    val selectedTheme: StateFlow<PastelPaletteType>
    val selectedWidgetTheme: StateFlow<PastelPaletteType>
    val useSystemFont: StateFlow<Boolean>
    val widgetOpacity: StateFlow<Float>
    val eventWidgetData: StateFlow<com.example.data.model.EventWidgetModel>

    // Active Pass remaining time in millis
    private val _passRemainingMillis = MutableStateFlow(0L)
    val passRemainingMillis: StateFlow<Long> = _passRemainingMillis.asStateFlow()

    private val _isPassActive = MutableStateFlow(true)
    val isPassActive: StateFlow<Boolean> = _isPassActive.asStateFlow()

    // Dialog state management
    private val _showAdDialog = MutableStateFlow(false)
    val showAdDialog: StateFlow<Boolean> = _showAdDialog.asStateFlow()

    private val _adDialogHoursToGrant = MutableStateFlow(8)
    val adDialogHoursToGrant: StateFlow<Int> = _adDialogHoursToGrant.asStateFlow()

    private val _showPassRequiredDialog = MutableStateFlow(false)
    val showPassRequiredDialog: StateFlow<Boolean> = _showPassRequiredDialog.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao(), database.categoryDao())
        passAndThemeManager = PassAndThemeManager(application)

        allTasks = repository.allTasksWithSubtasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allCategories = repository.allCategories.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        selectedTheme = passAndThemeManager.selectedTheme
        selectedWidgetTheme = passAndThemeManager.selectedWidgetTheme
        useSystemFont = passAndThemeManager.useSystemFont
        widgetOpacity = passAndThemeManager.widgetOpacity
        eventWidgetData = passAndThemeManager.eventWidgetData

        viewModelScope.launch {
            repository.seedDefaultsIfEmpty()
            WidgetService.updateHomeScreenWidget(getApplication())
        }

        // Live pass countdown ticker
        viewModelScope.launch {
            while (true) {
                val remaining = passAndThemeManager.getPassRemainingMillis()
                val active = passAndThemeManager.isPassActive()
                _passRemainingMillis.value = remaining
                _isPassActive.value = active
                delay(1000)
            }
        }
    }

    fun selectTheme(theme: PastelPaletteType) {
        val success = passAndThemeManager.setTheme(theme)
        if (!success) {
            _showPassRequiredDialog.value = true
        }
    }

    fun selectWidgetTheme(theme: PastelPaletteType): Boolean {
        val success = passAndThemeManager.setWidgetTheme(theme)
        if (success) {
            WidgetService.updateHomeScreenWidget(getApplication())
        } else {
            _showPassRequiredDialog.value = true
        }
        return success
    }

    fun setUseSystemFont(useSystem: Boolean) {
        passAndThemeManager.setUseSystemFont(useSystem)
    }

    fun setWidgetOpacity(opacity: Float) {
        passAndThemeManager.setWidgetOpacity(opacity)
        WidgetService.updateHomeScreenWidget(getApplication())
    }

    fun saveEventWidgetData(event: com.example.data.model.EventWidgetModel) {
        passAndThemeManager.eventWidgetPreferences.saveEventModel(event)
        selectWidgetTheme(PastelPaletteType.GLASSMORPHISM)
        WidgetService.updateHomeScreenWidget(getApplication())
    }

    fun triggerWatchAd(hours: Int) {
        _adDialogHoursToGrant.value = hours
        _showAdDialog.value = true
    }

    fun dismissAdDialog() {
        _showAdDialog.value = false
    }

    fun completeAdAndGrantPass() {
        val hours = _adDialogHoursToGrant.value
        passAndThemeManager.extendPassByHours(hours)
        _showAdDialog.value = false
        _showPassRequiredDialog.value = false
    }

    fun dismissPassRequiredDialog() {
        _showPassRequiredDialog.value = false
    }

    fun createTask(
        title: String,
        notes: String,
        category: String,
        categoryColorHex: String,
        priority: String,
        dueDateMillis: Long?,
        dueTimeHour: Int?,
        dueTimeMinute: Int?,
        subtaskTitles: List<String>
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                notes = notes,
                category = category,
                categoryColorHex = categoryColorHex,
                priority = priority,
                dueDateMillis = dueDateMillis,
                dueTimeHour = dueTimeHour,
                dueTimeMinute = dueTimeMinute,
                isCompleted = false
            )
            val taskId = repository.insertTaskWithSubtasks(task, subtaskTitles)
            NotificationHelper.scheduleTaskReminder(getApplication(), task.copy(id = taskId))
            WidgetService.updateHomeScreenWidget(getApplication())
        }
    }

    fun updateTask(
        task: TaskEntity,
        subtasks: List<SubtaskEntity>,
        newSubtaskTitles: List<String>
    ) {
        viewModelScope.launch {
            repository.updateTaskWithSubtasks(task, subtasks, newSubtaskTitles)
            NotificationHelper.scheduleTaskReminder(getApplication(), task)
            WidgetService.updateHomeScreenWidget(getApplication())
        }
    }

    fun toggleTaskCompletion(taskWithSubtasks: TaskWithSubtasks) {
        viewModelScope.launch {
            val updatedTask = taskWithSubtasks.task.copy(isCompleted = !taskWithSubtasks.task.isCompleted)
            repository.updateTask(updatedTask)
            WidgetService.updateHomeScreenWidget(getApplication())
        }
    }

    fun toggleSubtask(subtask: SubtaskEntity) {
        viewModelScope.launch {
            repository.toggleSubtaskCompletion(subtask)
            WidgetService.updateHomeScreenWidget(getApplication())
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            NotificationHelper.cancelTaskReminder(getApplication(), task.id)
            repository.deleteTask(task)
            WidgetService.updateHomeScreenWidget(getApplication())
        }
    }

    fun addSubtask(taskId: Long, title: String) {
        viewModelScope.launch {
            repository.addSubtask(taskId, title)
            WidgetService.updateHomeScreenWidget(getApplication())
        }
    }

    fun addCategory(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.addCategory(name, colorHex)
        }
    }
}
