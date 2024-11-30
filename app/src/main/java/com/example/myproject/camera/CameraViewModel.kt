package com.example.myproject.camera


import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private val photoDao = AppDatabase.getDatabase(application).photoDao()

    val allPhotos: Flow<List<Photo>> = photoDao.getAllPhotos()

    fun addPhotoUri(uri: Uri) {
        viewModelScope.launch {
            try {
                photoDao.insert(Photo(uri = uri.toString()))
            } catch (e: Exception) {
                // Log error or handle as needed
            }
        }
    }

    fun deletePhoto(photoId: Int) {
        viewModelScope.launch {
            try {
                photoDao.deletePhoto(photoId)
            } catch (e: Exception) {
                // Log error or handle as needed
            }
        }
    }
}