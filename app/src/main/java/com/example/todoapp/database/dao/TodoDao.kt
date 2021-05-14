package com.example.todoapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.database.entity.Todo
import java.util.*

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo: Todo)

    @Query("SELECT * FROM todo WHERE isDone = :doneStatus UNION SELECT * FROM todo WHERE isDone = :doneStatus order by category")
    fun getAllTodos(doneStatus: Int) : LiveData<MutableList<Todo>>

    @Query("UPDATE todo SET isDone =:checkStatus WHERE id = :id")
    fun updateTodoCheckStatus(checkStatus: Boolean, id: UUID)

    @Delete
    fun deleteTodo(todo: Todo)

    @Query("UPDATE todo SET name = :newName WHERE id = :todoId")
    fun updateTodo(newName: String, todoId: UUID)

    @Query("SELECT *FROM todo WHERE id = :todoId")
    fun getTodoById(todoId: UUID) : LiveData<Todo>

    @Query("UPDATE todo SET details = :newDetail WHERE id = :todoId")
    fun updateTodoDetail(newDetail: String, todoId: UUID)

    @Query("UPDATE todo SET remainder = null WHERE id = :todoId")
    fun removeRemainder(todoId: UUID)

    @Query("UPDATE todo SET category = :category WHERE id = :todoId")
    fun addTodoCategory(category: String?, todoId: UUID)

    @Query("UPDATE todo SET remainder = :date WHERE id = :todoId")
    fun addTodoRemainder(todoId: UUID, date: Date)
}