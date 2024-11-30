package com.example.myproject.ui.theme

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) // Format: 29 Nov 2024, 13:45
    return sdf.format(Date(timestamp))
}
