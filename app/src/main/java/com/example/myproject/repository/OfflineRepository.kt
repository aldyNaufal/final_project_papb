package com.example.myproject.repository

import com.example.myproject.data.Task
import com.example.myproject.data.TaskDao
import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTaskStream(): Flow<List<Task>> = taskDao.getAllTasks()

    override suspend fun getTaskById(id: Int): Task? = taskDao.getTaskById(id) // Langsung suspend

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    override suspend fun insertTasks(tasks: List<Task>) { // Implementasi baru
        taskDao.insertAll(tasks)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    override suspend fun updateTaskCompletionStatus(id: Int, isCompleted: Boolean) {
        taskDao.updateTaskCompletionStatus(id, isCompleted)
    }
}


