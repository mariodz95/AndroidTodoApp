package com.example.todoapp.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.todoapp.database.entity.Todo
import com.google.gson.Gson

@Composable
fun TodoListRow(
    todo: Todo,
    checked: (Todo) -> Unit,
    navController: NavHostController

){
    val gson = Gson()
    val todoString = gson.toJson(todo)
    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("todoDetails/$todoString")
            },
        elevation = 8.dp)
    {
        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("${todo.name}", modifier = Modifier.align(Alignment.Start), fontSize = 16.sp, color = if(todo.isDone) Color.Gray else Color.Black)
                Text("${if(todo.category == null) "No Category!" else todo.category}", modifier = Modifier.align(Alignment.Start), fontSize = 12.sp, color = if(todo.isDone) Color.Gray else Color.Black)
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
                    modifier = Modifier.align(Alignment.End),
                    colors =  CheckboxDefaults.colors(
                        checkedColor = Color.Gray

                    )
                )
            }
        }
    }
}