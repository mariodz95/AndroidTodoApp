package com.example.todoapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Todo (
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "details") val details: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "dateAdded") val dateAdded: Date?
)