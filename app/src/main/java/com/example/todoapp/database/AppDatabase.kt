package com.example.todoapp.database

import androidx.room.Database;
import androidx.room.TypeConverters;
import androidx.room.RoomDatabase;
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.database.entity.Todo

import com.example.todoapp.util.RoomDateConverter;

@Database(entities = [Todo::class,], version = 1, exportSchema = false)
@TypeConverters(RoomDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}