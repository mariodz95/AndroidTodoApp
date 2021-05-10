package com.example.todoapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
}