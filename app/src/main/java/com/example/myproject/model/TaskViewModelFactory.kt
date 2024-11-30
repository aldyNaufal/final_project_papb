package com.example.myproject.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.TaskDatabase
import com.example.myproject.repository.FirestoreTaskRepository
import com.example.myproject.repository.OfflineItemsRepository
import com.google.firebase.firestore.FirebaseFirestore

class TaskViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            // Mendapatkan instance DAO untuk Room
            val dao = TaskDatabase.getDatabase(context).taskDao()

            // Membuat instance repository untuk Room
            val roomRepository = OfflineItemsRepository(dao)

            // Membuat instance Firestore untuk Firestore repository
            val firestoreRepository = FirestoreTaskRepository(FirebaseFirestore.getInstance())

            // Membuat instance TaskViewModel dengan dua repository
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(roomRepository, firestoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

