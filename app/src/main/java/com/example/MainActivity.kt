package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.viewmodel.TaskViewModel
import com.example.service.NotificationHelper
import com.example.ui.screens.MainScreen

class MainActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create Notification channel for task alerts
        NotificationHelper.createNotificationChannel(this)

        setContent {
            MainScreen(viewModel = viewModel)
        }
    }
}
