package com.example.myproject.repository

import com.example.myproject.data.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTaskStream(): Flow<List<Task>>

    suspend fun getTaskById(id: Int): Task?

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun updateTaskCompletionStatus(id: Int, isCompleted: Boolean)

    suspend fun insertTasks(tasks: List<Task>)
}


