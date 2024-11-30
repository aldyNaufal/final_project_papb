package com.example.myproject.camera

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryPage(
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel,
    navController: NavController
) {
    // Gunakan .collectAsStateWithLifecycle() untuk Flow
    val photos by cameraViewModel.allPhotos
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val onPhotoSelected: (Uri) -> Unit = { uri ->
        // Simpan URI foto yang dipilih ke task database atau lakukan aksi lainnya
        cameraViewModel.addPhotoUri(uri)
        navController.popBackStack() // Kembali ke halaman sebelumnya
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Photo Gallery") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(photos) { photo ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Saved Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clickable {
                            onPhotoSelected(Uri.parse(photo.uri))
                        }
                )
            }
        }
    }
}