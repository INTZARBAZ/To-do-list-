package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val category: String = "Personal",
    val categoryColorHex: String = "#D8B4F8", // Soft Lavender default
    val priority: String = Priority.MEDIUM.name,
    val dueDateMillis: Long? = null,
    val dueTimeHour: Int? = null,
    val dueTimeMinute: Int? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
