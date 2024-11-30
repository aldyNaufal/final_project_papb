package com.example.myproject

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myproject.camera.CameraPage
import com.example.myproject.camera.CameraViewModel
import com.example.myproject.camera.CameraViewModelFactory
import com.example.myproject.camera.PhotoGalleryPage
import com.example.myproject.model.AuthViewModel
import com.example.myproject.pages.AddPage
import com.example.myproject.pages.HomePage
import com.example.myproject.pages.LoginPage
import com.example.myproject.pages.SearchPage
import com.example.myproject.pages.SignupPage
import com.example.myproject.pages.UpdatePage

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    cameraViewModel: CameraViewModel // Add CameraViewModel as a parameter
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("search") {
            SearchPage(modifier, navController)
        }
        composable(
            route = "update/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("id") ?: -1
            UpdatePage(modifier, navController, taskId)
        }
        composable("add") {
            AddPage(modifier, navController)
        }

        composable("camera") {
            CameraPage(
                modifier = modifier,
                navController = navController,
                cameraViewModel = cameraViewModel
            )
        }

        composable(
            "gallery?taskId={taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            val cameraViewModel: CameraViewModel = viewModel(
                factory = CameraViewModelFactory(LocalContext.current.applicationContext as Application)
            )
            PhotoGalleryPage(
                cameraViewModel = cameraViewModel,
                navController = navController
            )
        }
    }
}