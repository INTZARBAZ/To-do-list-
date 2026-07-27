package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.TaskWithSubtasks
import com.example.data.viewmodel.TaskViewModel
import com.example.ui.components.AdEngineDialog
import com.example.ui.components.CreateTaskBottomSheet
import com.example.ui.components.PassRequiredDialog
import com.example.ui.theme.PastelTodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TaskViewModel
) {
    val tasks by viewModel.allTasks.collectAsState()
    val categories by viewModel.allCategories.collectAsState()
    val selectedTheme by viewModel.selectedTheme.collectAsState()
    val selectedWidgetTheme by viewModel.selectedWidgetTheme.collectAsState()
    val useSystemFont by viewModel.useSystemFont.collectAsState()
    val widgetOpacity by viewModel.widgetOpacity.collectAsState()
    val isPassActive by viewModel.isPassActive.collectAsState()
    val passRemainingMillis by viewModel.passRemainingMillis.collectAsState()
    val eventWidgetModel by viewModel.eventWidgetData.collectAsState()

    val showAdDialog by viewModel.showAdDialog.collectAsState()
    val adHoursToGrant by viewModel.adDialogHoursToGrant.collectAsState()
    val showPassRequiredDialog by viewModel.showPassRequiredDialog.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showWidgetCustomizerScreen by remember { mutableStateOf(false) }

    // Bottom Sheet state
    var showCreateBottomSheet by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<TaskWithSubtasks?>(null) }
    var presetDueDateMillis by remember { mutableStateOf<Long?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    PastelTodoTheme(
        paletteType = selectedTheme,
        useSystemFont = useSystemFont
    ) {
        if (showWidgetCustomizerScreen) {
            WidgetCustomizerScreen(
                currentWidgetTheme = selectedWidgetTheme,
                currentWidgetOpacity = widgetOpacity,
                isPassActive = isPassActive,
                eventWidgetModel = eventWidgetModel,
                onApplyWidgetTheme = { theme, opacity ->
                    viewModel.selectWidgetTheme(theme)
                    viewModel.setWidgetOpacity(opacity)
                    showWidgetCustomizerScreen = false
                },
                onSaveEventWidgetData = { event ->
                    viewModel.saveEventWidgetData(event)
                },
                onTriggerAdDialog = { viewModel.triggerWatchAd(8) },
                onTriggerAdDialogWithHours = { hours -> viewModel.triggerWatchAd(hours) },
                onBack = { showWidgetCustomizerScreen = false }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        containerColor = com.example.ui.theme.NaturalNavBackground,
                        tonalElevation = 4.dp,
                        modifier = Modifier.testTag("bottom_navigation_bar")
                    ) {
                        NavigationBarItem(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = "Tasks") },
                            label = { Text("Tasks", fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1D192B),
                                unselectedIconColor = Color(0xFF49454F),
                                selectedTextColor = Color(0xFF1D192B),
                                unselectedTextColor = Color(0xFF49454F),
                                indicatorColor = Color(0xFFE8DEF8)
                            ),
                            modifier = Modifier.testTag("nav_tab_tasks")
                        )

                        NavigationBarItem(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar") },
                            label = { Text("Calendar", fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1D192B),
                                unselectedIconColor = Color(0xFF49454F),
                                selectedTextColor = Color(0xFF1D192B),
                                unselectedTextColor = Color(0xFF49454F),
                                indicatorColor = Color(0xFFE8DEF8)
                            ),
                            modifier = Modifier.testTag("nav_tab_calendar")
                        )

                        NavigationBarItem(
                            selected = selectedTabIndex == 2,
                            onClick = { selectedTabIndex = 2 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Mine") },
                            label = { Text("Mine", fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Medium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1D192B),
                                unselectedIconColor = Color(0xFF49454F),
                                selectedTextColor = Color(0xFF1D192B),
                                unselectedTextColor = Color(0xFF49454F),
                                indicatorColor = Color(0xFFE8DEF8)
                            ),
                            modifier = Modifier.testTag("nav_tab_mine")
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            editingTask = null
                            presetDueDateMillis = null
                            showCreateBottomSheet = true
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                        elevation = FloatingActionButtonDefaults.elevation(6.dp),
                        modifier = Modifier.testTag("add_task_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create Task", modifier = Modifier.size(32.dp))
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (selectedTabIndex) {
                        0 -> TasksScreen(
                            tasks = tasks,
                            categories = categories,
                            isPassActive = isPassActive,
                            passRemainingMillis = passRemainingMillis,
                            onPassClick = { viewModel.triggerWatchAd(8) },
                            onToggleTask = { viewModel.toggleTaskCompletion(it) },
                            onToggleSubtask = { viewModel.toggleSubtask(it) },
                            onEditTask = {
                                editingTask = it
                                showCreateBottomSheet = true
                            },
                            onDeleteTask = { viewModel.deleteTask(it) },
                            onAddTaskClick = {
                                editingTask = null
                                showCreateBottomSheet = true
                            }
                        )

                        1 -> CalendarScreen(
                            tasks = tasks,
                            isPassActive = isPassActive,
                            passRemainingMillis = passRemainingMillis,
                            onPassClick = { viewModel.triggerWatchAd(8) },
                            onToggleTask = { viewModel.toggleTaskCompletion(it) },
                            onToggleSubtask = { viewModel.toggleSubtask(it) },
                            onEditTask = {
                                editingTask = it
                                showCreateBottomSheet = true
                            },
                            onDeleteTask = { viewModel.deleteTask(it) },
                            onAddTaskForDate = { dateMillis ->
                                editingTask = null
                                presetDueDateMillis = dateMillis
                                showCreateBottomSheet = true
                            }
                        )

                        2 -> MineScreen(
                            tasks = tasks,
                            categories = categories,
                            selectedTheme = selectedTheme,
                            selectedWidgetTheme = selectedWidgetTheme,
                            useSystemFont = useSystemFont,
                            widgetOpacity = widgetOpacity,
                            isPassActive = isPassActive,
                            passRemainingMillis = passRemainingMillis,
                            onSelectTheme = { viewModel.selectTheme(it) },
                            onToggleSystemFont = { viewModel.setUseSystemFont(it) },
                            onWidgetOpacityChange = { viewModel.setWidgetOpacity(it) },
                            onOpenWidgetCustomizer = { showWidgetCustomizerScreen = true },
                            onWatchAd = { hours -> viewModel.triggerWatchAd(hours) },
                            onAddCategory = { name, hex -> viewModel.addCategory(name, hex) }
                        )
                    }

                    // Modal Bottom Sheet for task creation/editing
                    if (showCreateBottomSheet) {
                        CreateTaskBottomSheet(
                            sheetState = sheetState,
                            categories = categories,
                            editingTask = editingTask,
                            onSaveTask = { title, notes, cat, catHex, priority, dueDate, timeH, timeM, subtasks ->
                                if (editingTask != null) {
                                    viewModel.updateTask(
                                        task = editingTask!!.task.copy(
                                            title = title,
                                            notes = notes,
                                            category = cat,
                                            categoryColorHex = catHex,
                                            priority = priority,
                                            dueDateMillis = dueDate,
                                            dueTimeHour = timeH,
                                            dueTimeMinute = timeM
                                        ),
                                        subtasks = editingTask!!.subtasks,
                                        newSubtaskTitles = subtasks
                                    )
                                } else {
                                    viewModel.createTask(
                                        title = title,
                                        notes = notes,
                                        category = cat,
                                        categoryColorHex = catHex,
                                        priority = priority,
                                        dueDateMillis = dueDate ?: presetDueDateMillis,
                                        dueTimeHour = timeH,
                                        dueTimeMinute = timeM,
                                        subtaskTitles = subtasks
                                    )
                                }
                                showCreateBottomSheet = false
                            },
                            onDismiss = { showCreateBottomSheet = false }
                        )
                    }
                }
            }
        }

        // Dialogs for 24-Hour Pass System
        if (showAdDialog) {
            AdEngineDialog(
                hoursToGrant = adHoursToGrant,
                onAdCompleted = { viewModel.completeAdAndGrantPass() },
                onDismiss = { viewModel.dismissAdDialog() }
            )
        }

        if (showPassRequiredDialog) {
            PassRequiredDialog(
                onWatchAd = { hours -> viewModel.triggerWatchAd(hours) },
                onDismiss = { viewModel.dismissPassRequiredDialog() }
            )
        }
    }
}
