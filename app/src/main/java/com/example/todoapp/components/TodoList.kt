package com.example.todoapp.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TodoListContent(
    todoViewModel: TodoViewModel,
    todoList: List<Todo>
){
    TodoList(
        todoList = todoList,
        checked = { todoViewModel.checkTodo(it) },
        )
}

@Composable
fun TodoList(
    todoList: List<Todo>,
    checked: (Todo) -> Unit,
){
    LazyColumn{
        items(todoList){ todo->
            TodoListRow(
                todo = todo,
                checked = {checked(it)},
                )
        }
    }
}

@Composable
fun TodoListRow(
    todo: Todo,
    checked: (Todo) -> Unit,
){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = 8.dp){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
        ) {
            Text("${todo.name}", modifier = Modifier.align(Alignment.Start))
            Text("${todo.category}", modifier = Modifier.align(Alignment.End))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
        ) {
            Checkbox(
                checked = if(todo.isDone) true else false,
                onCheckedChange = {
                    checked(todo)
                                  },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}