package com.example.todoapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.database.entity.Todo

@Composable
fun TodoListRow(
    todo: Todo,
    checked: (Todo) -> Unit,
){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = 8.dp){
        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start

            ) {
                Text("${todo.name}", modifier = Modifier.align(Alignment.Start), fontSize = 16.sp)
                Text("${if(todo.category == null) "No Category!" else todo.category}", modifier = Modifier.align(Alignment.Start), fontSize = 12.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
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
}