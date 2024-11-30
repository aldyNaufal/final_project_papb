package com.example.myproject.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String?,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    @Ignore
    constructor() : this(0, "", null, false, System.currentTimeMillis())
}

