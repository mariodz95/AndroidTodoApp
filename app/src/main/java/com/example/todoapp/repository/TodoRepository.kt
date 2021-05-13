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

    fun updateTodo(newName: String, todoId: UUID){
        todoDao.updateTodo(newName, todoId)
    }

    fun updateTodoDetail(newDetail: String, todoId: UUID){
        todoDao.updateTodoDetail(newDetail, todoId)
    }

    fun getTodoById(todoId: UUID): LiveData<Todo>{
        return todoDao.getTodoById(todoId)
    }

    fun removeRemainder(todoId: UUID){
        todoDao.removeRemainder(todoId)
    }
}