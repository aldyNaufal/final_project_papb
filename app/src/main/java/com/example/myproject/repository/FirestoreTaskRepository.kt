package com.example.myproject.repository

import com.example.myproject.data.Task
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreTaskRepository(private val firestore: FirebaseFirestore) {

    // Menambahkan task ke Firestore
    fun addTask(task: Task, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("tasks")
            .document(task.id.toString())
            .set(task)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Memperbarui task di Firestore
    fun updateTask(task: Task, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("tasks")
            .document(task.id.toString())
            .set(task)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Menghapus task dari Firestore
    fun deleteTask(id: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("tasks")
            .document(id.toString())
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Mendapatkan semua task dari Firestore
    fun getAllTasks(onSuccess: (List<Task>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.documents.mapNotNull { it.toObject(Task::class.java) }
                onSuccess(tasks)
            }
            .addOnFailureListener { onFailure(it) }
    }

    // Sinkronisasi real-time dari Firestore
    fun startRealTimeSync(onTasksUpdated: (List<Task>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("tasks")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    onFailure(e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val tasks = snapshots.documents.mapNotNull { it.toObject(Task::class.java) }
                    onTasksUpdated(tasks)
                }
            }
    }
}
