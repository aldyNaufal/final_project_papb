package com.example.myproject.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
fun SearchPage(
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val context = LocalContext.current
    val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(context))

    var inputId by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var searchId by remember { mutableStateOf<Int?>(null) } // ID untuk pencarian

    // Mengambil task berdasarkan ID dari ViewModel
    val task by taskViewModel.getTaskById(searchId ?: -1).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Task by ID") },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigate("home") }) {
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
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Input field for ID
                OutlinedTextField(
                    value = inputId,
                    onValueChange = { inputId = it },
                    label = { Text("Task ID") },
                    placeholder = { Text("Enter Task ID") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showError
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search button
                Button(
                    onClick = {
                        val id = inputId.toIntOrNull()
                        if (id == null) {
                            showError = true
                        } else {
                            showError = false
                            searchId = id // Set ID for search
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error message if input is invalid
                if (showError) {
                    Text(
                        text = "Please enter a valid ID!",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display task details if found
                when {
                    task != null -> {
                        Text(
                            text = "Task Details:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(text = "ID: ${task!!.id}", fontSize = 14.sp)
                        Text(text = "Title: ${task!!.title}", fontSize = 14.sp)
                        Text(
                            text = "Description: ${task!!.description ?: "No description"}",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Created At: ${
                                java.text.SimpleDateFormat(
                                    "dd MMM yyyy, HH:mm",
                                    java.util.Locale.getDefault()
                                ).format(java.util.Date(task!!.createdAt))
                            }",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Status: ${if (task!!.isCompleted) "Completed" else "Pending"}",
                            fontSize = 14.sp
                        )
                    }

                    !showError && searchId != null -> {
                        Text(
                            text = "No task found with the given ID.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    )
}

