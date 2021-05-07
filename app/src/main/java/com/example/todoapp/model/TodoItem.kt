package com.example.todoapp.model

import java.util.*

data class TodoItem(
    val id: UUID,
    val name: String,
    val details: String? = null,
    val category: String,
    val dateAdded: Date,
    val remainder: Date,
)
