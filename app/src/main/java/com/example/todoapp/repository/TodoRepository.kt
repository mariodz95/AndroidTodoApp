package com.example.todoapp.repository

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.di.repositoryModule

class TodoRepository(private val todoDao: TodoDao ) {

    fun insertTodo(todo: Todo){
        todoDao.insertTodo(todo)
    }

    fun getAllTodos(): LiveData<List<Todo>>{
        val test = todoDao.getAllTodos()
        Log.v("sada", "repos lista : ${test.value}")
        return todoDao.getAllTodos()
    }
}