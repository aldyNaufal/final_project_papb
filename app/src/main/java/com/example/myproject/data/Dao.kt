package com.example.myproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Mengambil semua task dalam urutan waktu terbaru
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    // Mengambil task berdasarkan ID
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): Task?

    // Menambahkan atau memperbarui task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Task>)

    // Menghapus task
    @Delete
    suspend fun deleteTask(task: Task)

    // Memperbarui task secara keseluruhan (opsional)
    @Update
    suspend fun updateTask(task: Task)

    // Memperbarui status penyelesaian task
    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletionStatus(taskId: Int, isCompleted: Boolean)
}

