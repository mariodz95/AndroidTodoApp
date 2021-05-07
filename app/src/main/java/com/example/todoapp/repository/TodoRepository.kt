package com.example.todoapp.repository

import android.util.Log
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.database.entity.Todo

class TodoRepository(private val todoDao: TodoDao ) {

    fun insertTodo(todo: Todo){
        Log.v("sada", "repository todo :  $todo")
        todoDao.insertTodo(todo)
    }
}