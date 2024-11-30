package com.example.myproject.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myproject.R
import com.example.myproject.model.AuthState
import com.example.myproject.model.AuthViewModel
import com.example.myproject.model.TaskViewModel
import com.example.myproject.model.TaskViewModelFactory
import com.example.myproject.ui.theme.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    authViewModel: AuthViewModel? = null
) {
    val context = LocalContext.current
    val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(context))

    // Observasi state untuk semua task dari Room
    val allTasks by taskViewModel.allTasks.collectAsState(initial = emptyList())

    // Observasi state autentikasi dari AuthViewModel
    val authState = authViewModel?.authState?.observeAsState(initial = AuthState.Loading)?.value

    // Jika pengguna tidak terautentikasi, arahkan ke halaman login
    if (authState is AuthState.Unauthenticated) {
        LaunchedEffect(Unit) {
            navController?.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Page") },
                actions = {
                    authViewModel?.let { viewModel ->
                        IconButton(
                            onClick = {
                                viewModel.signout()
                                navController?.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Menampilkan email jika pengguna sudah terautentikasi
                if (authState is AuthState.Authenticated) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = "Selamat Datang",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = authState.email ?: "User",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menampilkan daftar tugas dari Room
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allTasks.filter { !it.isCompleted }, key = { it.id }) { task ->
                        Item(
                            task = task,
                            onUploadImage = { selectedTask ->
                                // Navigasi ke halaman galeri untuk upload gambar
                                navController?.navigate("gallery?taskId=${selectedTask.id}")
                            },
                            onEdit = { selectedTask ->
                                // Navigasi ke halaman edit dengan ID task
                                navController?.navigate("update/${selectedTask.id}")
                            },
                            onDelete = { selectedTask ->
                                // Hapus task dari database
                                taskViewModel.deleteTask(selectedTask)
                            }
                        )
                    }
                }

            }
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = { navController?.navigate("search") },
                    containerColor = Color(0xFF6A1B9A),
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "Search Task",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                FloatingActionButton(
                    onClick = { navController?.navigate("camera") },
                    containerColor = Color(0xFF6A1B9A)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera_icon),
                        contentDescription = "Open Camera",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                FloatingActionButton(
                    onClick = { navController?.navigate("add") },
                    containerColor = Color(0xFF6A1B9A),
                    modifier = Modifier.padding(end = 3.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_icon),
                        contentDescription = "Add Task",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    )
}







@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage()
}
