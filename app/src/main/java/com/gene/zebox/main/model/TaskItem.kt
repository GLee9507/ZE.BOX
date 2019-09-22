package com.gene.zebox.main.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_item")
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    val _id: Long,

    val title: String,
    val time: Long,
    val content: List<String>
)