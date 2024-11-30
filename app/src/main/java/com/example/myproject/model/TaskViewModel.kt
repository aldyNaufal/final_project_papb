package com.example.myproject.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproject.data.Task
import com.example.myproject.repository.FirestoreTaskRepository
import com.example.myproject.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val roomRepository: TaskRepository,
    private val firestoreRepository: FirestoreTaskRepository
) : ViewModel() {

    // Stream untuk semua task (Room sebagai sumber utama)
    val allTasks: StateFlow<List<Task>> = roomRepository.getAllTaskStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Menambahkan task baru ke Room dan Firestore
    fun insertTask(task: Task) = viewModelScope.launch {
        roomRepository.insertTask(task)
        firestoreRepository.addTask(task, onSuccess = {
            println("Task successfully added to Firestore.")
        }, onFailure = { e ->
            println("Error adding task to Firestore: $e")
        })
    }

    // Menghapus task dari Room dan Firestore
    fun deleteTask(task: Task) = viewModelScope.launch {
        roomRepository.deleteTask(task)
        firestoreRepository.deleteTask(task.id, onSuccess = {
            println("Task successfully deleted from Firestore.")
        }, onFailure = { e ->
            println("Error deleting task from Firestore: $e")
        })
    }

    // Memperbarui task di Room dan Firestore
    fun updateTask(task: Task) = viewModelScope.launch {
        roomRepository.updateTask(task)
        firestoreRepository.updateTask(task, onSuccess = {
            println("Task successfully updated in Firestore.")
        }, onFailure = { e ->
            println("Error updating task in Firestore: $e")
        })
    }


    // Memperbarui status penyelesaian task di Room dan Firestore
    fun updateTaskCompletionStatus(id: Int, isCompleted: Boolean) = viewModelScope.launch {
        roomRepository.updateTaskCompletionStatus(id, isCompleted)
        val task = allTasks.value.find { it.id == id }
        if (task != null) {
            val updatedTask = task.copy(isCompleted = isCompleted)
            firestoreRepository.updateTask(updatedTask, onSuccess = {
                println("Task completion status successfully updated in Firestore.")
            }, onFailure = { e ->
                println("Error updating task completion status in Firestore: $e")
            })
        }
    }

    // Mengambil task berdasarkan ID
    fun getTaskById(taskId: Int): StateFlow<Task?> = allTasks
        .map { tasks -> tasks.find { it.id == taskId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Sinkronisasi Firestore ke Room
    fun syncFromFirestore() {
        firestoreRepository.getAllTasks(
            onSuccess = { tasks ->
                viewModelScope.launch {
                    roomRepository.insertTasks(tasks) // Sinkronisasi semua task ke Room
                }
            },
            onFailure = { e ->
                println("Error syncing tasks from Firestore: $e")
            }
        )
    }

    // Sinkronisasi real-time dari Firestore ke Room
    fun startRealTimeSync() {
        firestoreRepository.startRealTimeSync(
            onTasksUpdated = { tasks ->
                viewModelScope.launch {
                    roomRepository.insertTasks(tasks) // Perbarui Room saat Firestore berubah
                }
            },
            onFailure = { e ->
                println("Error during real-time sync: $e")
            }
        )
    }

}


