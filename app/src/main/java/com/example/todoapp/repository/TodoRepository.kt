package com.example.todoapp.repository


import androidx.lifecycle.LiveData
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.database.entity.Todo
import java.util.*

class TodoRepository(private val todoDao: TodoDao ) {

    fun insertTodo(todo: Todo){
        todoDao.insertTodo(todo)
    }

    fun getAllTodos(doneStatus: Int): LiveData<MutableList<Todo>>{
        return todoDao.getAllTodos(doneStatus)
    }

    fun updateTodoCheckStatus(checkStatus: Boolean, id: UUID){
        todoDao.updateTodoCheckStatus(checkStatus, id)
    }

    fun deleteTodo(todo: Todo){
        todoDao.deleteTodo(todo)
    }
}