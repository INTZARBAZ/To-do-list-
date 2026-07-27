package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubtaskEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TaskWithSubtasks
import com.example.ui.components.PassBadge
import com.example.ui.components.TaskCard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    tasks: List<TaskWithSubtasks>,
    isPassActive: Boolean,
    passRemainingMillis: Long,
    onPassClick: () -> Unit,
    onToggleTask: (TaskWithSubtasks) -> Unit,
    onToggleSubtask: (SubtaskEntity) -> Unit,
    onEditTask: (TaskWithSubtasks) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onAddTaskForDate: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var calendarMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDateCalendar by remember { mutableStateOf(Calendar.getInstance()) }

    val currentYear = calendarMonth.get(Calendar.YEAR)
    val currentMonth = calendarMonth.get(Calendar.MONTH)

    val monthHeaderTitle = remember(currentYear, currentMonth) {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendarMonth.time)
    }

    // Days in current month calculations
    val daysInMonthList = remember(currentYear, currentMonth) {
        val cal = calendarMonth.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7 // Mon = 0, Sun = 6
        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val list = mutableListOf<CalendarDateCell>()

        // Previous month padding
        val prevCal = cal.clone() as Calendar
        prevCal.add(Calendar.MONTH, -1)
        val prevMaxDays = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in firstDayOfWeek - 1 downTo 0) {
            val d = prevCal.clone() as Calendar
            d.set(Calendar.DAY_OF_MONTH, prevMaxDays - i)
            list.add(CalendarDateCell(d, isCurrentMonth = false))
        }

        // Current month days
        for (day in 1..maxDays) {
            val d = cal.clone() as Calendar
            d.set(Calendar.DAY_OF_MONTH, day)
            list.add(CalendarDateCell(d, isCurrentMonth = true))
        }

        // Next month padding to fill grid to multiple of 7
        var nextDay = 1
        while (list.size % 7 != 0) {
            val nextCal = cal.clone() as Calendar
            nextCal.add(Calendar.MONTH, 1)
            nextCal.set(Calendar.DAY_OF_MONTH, nextDay)
            list.add(CalendarDateCell(nextCal, isCurrentMonth = false))
            nextDay++
        }

        list
    }

    // Helper to get tasks for a specific Calendar day
    fun getTasksForDate(cal: Calendar): List<TaskWithSubtasks> {
        val startCal = cal.clone() as Calendar
        startCal.set(Calendar.HOUR_OF_DAY, 0)
        startCal.set(Calendar.MINUTE, 0)
        startCal.set(Calendar.SECOND, 0)
        startCal.set(Calendar.MILLISECOND, 0)
        val startMillis = startCal.timeInMillis

        val endCal = cal.clone() as Calendar
        endCal.set(Calendar.HOUR_OF_DAY, 23)
        endCal.set(Calendar.MINUTE, 59)
        endCal.set(Calendar.SECOND, 59)
        endCal.set(Calendar.MILLISECOND, 999)
        val endMillis = endCal.timeInMillis

        return tasks.filter { taskWithSub ->
            val due = taskWithSub.task.dueDateMillis
            due != null && due in startMillis..endMillis
        }
    }

    val selectedDateTasks = remember(tasks, selectedDateCalendar) {
        getTasksForDate(selectedDateCalendar)
    }

    val selectedDateStr = remember(selectedDateCalendar) {
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(selectedDateCalendar.time)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("calendar_screen_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Bar & Pass Status
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Calendar View",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Event dot indicators & scheduled task timeline",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PassBadge(
                    isPassActive = isPassActive,
                    remainingMillis = passRemainingMillis,
                    onClick = onPassClick
                )
            }
        }

        // Calendar Month Container Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Month Navigation Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = monthHeaderTitle,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val newCal = calendarMonth.clone() as Calendar
                                    newCal.add(Calendar.MONTH, -1)
                                    calendarMonth = newCal
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
                            }

                            // Jump to Today button
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.clickable {
                                    calendarMonth = Calendar.getInstance()
                                    selectedDateCalendar = Calendar.getInstance()
                                }
                            ) {
                                Text(
                                    text = "Today",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    val newCal = calendarMonth.clone() as Calendar
                                    newCal.add(Calendar.MONTH, 1)
                                    calendarMonth = newCal
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Day of Week Header Row (Mon..Sun)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                            Text(
                                text = day,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Requirement 1: Top Month Calendar Grid with event dot indicators!
                    // Grid of Date Cells
                    val rows = daysInMonthList.chunked(7)
                    rows.forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            week.forEach { cell ->
                                val cellDate = cell.calendar
                                val isSelected = isSameDay(cellDate, selectedDateCalendar)
                                val isToday = isSameDay(cellDate, Calendar.getInstance())

                                val dateTasks = getTasksForDate(cellDate)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(42.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            when {
                                                isSelected -> MaterialTheme.colorScheme.primary
                                                isToday -> MaterialTheme.colorScheme.primaryContainer
                                                else -> Color.Transparent
                                            }
                                        )
                                        .clickable {
                                            selectedDateCalendar = cellDate.clone() as Calendar
                                        }
                                        .padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cellDate.get(Calendar.DAY_OF_MONTH).toString(),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = when {
                                            isSelected -> Color.White
                                            !cell.isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                            isToday -> MaterialTheme.colorScheme.primary
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Requirement 1: Event dot indicators under dates!
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.height(6.dp)
                                    ) {
                                        if (dateTasks.isNotEmpty()) {
                                            dateTasks.take(3).forEach { taskWithSub ->
                                                val dotColor = remember(taskWithSub.task.categoryColorHex) {
                                                    try {
                                                        Color(android.graphics.Color.parseColor(taskWithSub.task.categoryColorHex))
                                                    } catch (e: Exception) {
                                                        Color(0xFFD8B4F8)
                                                    }
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(if (isSelected) Color.White else dotColor)
                                                )
                                            }
                                        } else {
                                            Spacer(modifier = Modifier.size(5.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Requirement 1: Selected Date Header & Task List with color-coded left borders for categories!
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = selectedDateStr,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${selectedDateTasks.size} tasks scheduled for this day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clickable {
                        onAddTaskForDate(selectedDateCalendar.timeInMillis)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Task for Date",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Bottom Task Cards List for Selected Date
        if (selectedDateTasks.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No tasks scheduled on this day",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap + Add above to schedule a task with category color & time!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(selectedDateTasks, key = { it.task.id }) { taskWithSub ->
                TaskCard(
                    taskWithSubtasks = taskWithSub,
                    onToggleTask = { onToggleTask(taskWithSub) },
                    onToggleSubtask = { onToggleSubtask(it) },
                    onEditTask = { onEditTask(taskWithSub) },
                    onDeleteTask = { onDeleteTask(taskWithSub.task) }
                )
            }
        }
    }
}

private data class CalendarDateCell(
    val calendar: Calendar,
    val isCurrentMonth: Boolean
)

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
