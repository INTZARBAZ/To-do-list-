package com.example.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.Priority
import com.example.data.model.SubtaskEntity
import com.example.data.model.TaskWithSubtasks
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateTaskBottomSheet(
    sheetState: SheetState,
    categories: List<CategoryEntity>,
    editingTask: TaskWithSubtasks? = null,
    onSaveTask: (
        title: String,
        notes: String,
        category: String,
        categoryColorHex: String,
        priority: String,
        dueDateMillis: Long?,
        dueTimeHour: Int?,
        dueTimeMinute: Int?,
        subtasks: List<String>
    ) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var title by remember(editingTask) { mutableStateOf(editingTask?.task?.title ?: "") }
    var notes by remember(editingTask) { mutableStateOf(editingTask?.task?.notes ?: "") }
    var selectedCategory by remember(editingTask, categories) {
        mutableStateOf(
            categories.find { it.name == editingTask?.task?.category } ?: categories.firstOrNull() ?: CategoryEntity(
                name = "Personal",
                colorHex = "#D8B4F8"
            )
        )
    }
    var selectedPriority by remember(editingTask) {
        mutableStateOf(
            try {
                Priority.valueOf(editingTask?.task?.priority ?: Priority.MEDIUM.name)
            } catch (e: Exception) {
                Priority.MEDIUM
            }
        )
    }

    var dueDateMillis by remember(editingTask) { mutableStateOf<Long?>(editingTask?.task?.dueDateMillis) }
    var dueTimeHour by remember(editingTask) { mutableStateOf<Int?>(editingTask?.task?.dueTimeHour) }
    var dueTimeMinute by remember(editingTask) { mutableStateOf<Int?>(editingTask?.task?.dueTimeMinute) }

    val subtasks = remember(editingTask) {
        mutableStateListOf<String>().apply {
            editingTask?.subtasks?.forEach { add(it.title) }
        }
    }
    var newSubtaskText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("create_task_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = if (editingTask != null) "Edit Task" else "Create New Task",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title TextField
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("What needs to be done?", style = MaterialTheme.typography.bodyLarge) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_title_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Picker (Pastel Pills)
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { cat ->
                    val color = remember(cat.colorHex) {
                        try {
                            Color(android.graphics.Color.parseColor(cat.colorHex))
                        } catch (e: Exception) {
                            Color(0xFFD8B4F8)
                        }
                    }
                    val isSelected = selectedCategory.name == cat.name

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) color else color.copy(alpha = 0.2f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, color) else null,
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Priority Flag Selector
            Text(
                text = "Priority",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Priority.values().forEach { priority ->
                    val isSelected = selectedPriority == priority
                    val priorityColor = priority.toColor()

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) priorityColor else priorityColor.copy(alpha = 0.2f),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedPriority = priority }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else priorityColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = priority.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Time Picker Row
            Text(
                text = "Schedule Date & Time",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Date Chip
                val dateText = if (dueDateMillis != null) {
                    SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(Date(dueDateMillis!!))
                } else {
                    "Set Date"
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val cal = Calendar.getInstance()
                            if (dueDateMillis != null) cal.timeInMillis = dueDateMillis!!
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val newCal = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, year)
                                        set(Calendar.MONTH, month)
                                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    }
                                    dueDateMillis = newCal.timeInMillis
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }

                // Time Chip
                val timeText = if (dueTimeHour != null && dueTimeMinute != null) {
                    String.format(Locale.getDefault(), "%02d:%02d", dueTimeHour, dueTimeMinute)
                } else {
                    "Set Time"
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val cal = Calendar.getInstance()
                            val hour = dueTimeHour ?: cal.get(Calendar.HOUR_OF_DAY)
                            val minute = dueTimeMinute ?: cal.get(Calendar.MINUTE)
                            TimePickerDialog(
                                context,
                                { _, h, m ->
                                    dueTimeHour = h
                                    dueTimeMinute = m
                                    if (dueDateMillis == null) {
                                        dueDateMillis = System.currentTimeMillis()
                                    }
                                },
                                hour,
                                minute,
                                true
                            ).show()
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = timeText,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtasks Section
            Text(
                text = "Subtasks",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            subtasks.forEachIndexed { index, sub ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Text("•", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(sub, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { subtasks.removeAt(index) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(16.dp))
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newSubtaskText,
                    onValueChange = { newSubtaskText = it },
                    placeholder = { Text("Add subtask...", style = MaterialTheme.typography.bodySmall) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newSubtaskText.isNotBlank()) {
                            subtasks.add(newSubtaskText.trim())
                            newSubtaskText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Subtask", tint = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes TextField
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Add notes or details...", style = MaterialTheme.typography.bodyMedium) },
                maxLines = 3,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Task Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSaveTask(
                            title.trim(),
                            notes.trim(),
                            selectedCategory.name,
                            selectedCategory.colorHex,
                            selectedPriority.name,
                            dueDateMillis,
                            dueTimeHour,
                            dueTimeMinute,
                            subtasks.toList()
                        )
                    }
                },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_task_button")
            ) {
                Text(
                    text = if (editingTask != null) "Update Task" else "Create Task",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
