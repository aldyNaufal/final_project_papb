package com.example.myproject.camera

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uri: String,
    val timestamp: Long = System.currentTimeMillis()
)
