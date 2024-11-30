package com.example.myproject.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.myproject.data.Task
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.myproject.R

@Composable
fun Item(
    task: Task,
    onUploadImage: (Task) -> Unit,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bagian kiri: Data Task
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Menampilkan judul task
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge
            )
            // Menampilkan deskripsi (jika ada)
            Text(
                text = task.description ?: "No Description",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            // Menampilkan tanggal pembuatan task
            Text(
                text = "Created on: ${formatDate(task.createdAt)}", // Format tanggal
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onUploadImage(task) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = "Upload Image",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified // Warna asli
                )
            }
            IconButton(onClick = { onEdit(task) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit Task",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified // Warna asli
                )
            }
            IconButton(onClick = { onDelete(task) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete Task",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified // Warna asli
                )
            }
        }

    }
}


