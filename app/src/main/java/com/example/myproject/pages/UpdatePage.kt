package com.example.myproject.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myproject.model.TaskViewModel
import com.example.myproject.model.TaskViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePage(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    taskId: Int = -1,
    taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(LocalContext.current))
) {
    // Mengambil task dari taskViewModel berdasarkan taskId
    val task = taskViewModel.getTaskById(taskId).collectAsState(initial = null).value

    if (task == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // State untuk meng-handle perubahan input
    var updatedTitle by remember { mutableStateOf(task.title) }
    var updatedDescription by remember { mutableStateOf(task.description ?: "") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Task") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Menampilkan detail task
                Text(
                    text = "Task Details:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = "ID: ${task.id}", fontSize = 14.sp)
                Text(
                    text = "Created At: ${
                        java.text.SimpleDateFormat(
                            "dd MMM yyyy, HH:mm",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date(task.createdAt))
                    }",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Input judul task baru
                OutlinedTextField(
                    value = updatedTitle,
                    onValueChange = { updatedTitle = it },
                    label = { Text("Task Title") },
                    placeholder = { Text("Enter new task title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input deskripsi task baru
                OutlinedTextField(
                    value = updatedDescription,
                    onValueChange = { updatedDescription = it },
                    label = { Text("Task Description") },
                    placeholder = { Text("Enter new task description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan error jika input kosong
                if (showError) {
                    Text(
                        text = "Task title cannot be empty!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Tombol update task
                Button(
                    onClick = {
                        if (updatedTitle.isBlank()) {
                            showError = true
                        } else {
                            val updatedTask = task.copy(
                                title = updatedTitle,
                                description = updatedDescription
                            )
                            // Update task ke database
                            taskViewModel.updateTask(updatedTask)
                            navController?.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                ) {
                    Text("Update Task", color = Color.White)
                }
            }
        }
    )
}

