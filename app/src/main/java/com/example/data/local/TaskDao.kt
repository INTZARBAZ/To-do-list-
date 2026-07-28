package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.SubtaskEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, dueDateMillis ASC, createdAt DESC")
    fun getAllTasksWithSubtasks(): Flow<List<TaskWithSubtasks>>

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, dueDateMillis ASC, createdAt DESC")
    fun getAllTasksSync(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskByIdSync(taskId: Long): TaskEntity?

    @Update
    fun updateTaskSync(task: TaskEntity)

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskWithSubtasksById(taskId: Long): Flow<TaskWithSubtasks?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtasks(subtasks: List<SubtaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtask(subtask: SubtaskEntity): Long

    @Update
    suspend fun updateSubtask(subtask: SubtaskEntity)

    @Delete
    suspend fun deleteSubtask(subtask: SubtaskEntity)

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubtasksForTask(taskId: Long)
}
