package com.example.todoapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel

@Composable
fun TodoListContent(
    todoViewModel: TodoViewModel
){
    val todoList: List<Todo> by todoViewModel.todoList!!.observeAsState(listOf())

    TodoList(todoList)
}

@Composable
fun TodoList(
    todoList: List<Todo>
){
    LazyColumn{
        items(todoList){ todo->
            TodoListRow(todo)
        }
    }
}

@Composable
fun TodoListRow(
    todo: Todo
){
    Card(Modifier.fillMaxWidth().padding(8.dp), elevation = 8.dp){
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(8.dp),
        ) {
            Text("${todo.name}", modifier = Modifier.align(Alignment.Start))
        }

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(8.dp),
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = null,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}