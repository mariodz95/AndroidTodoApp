package com.example.todoapp.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel
import com.google.gson.Gson

@Composable
fun TodoDetailContent(navController: NavHostController, todoString: String?, todoViewModel: TodoViewModel) {

    var todo: Todo? = null
    if(todoString != null){
        val gson = Gson()
        todo = gson.fromJson(todoString, Todo::class.java)
    }

    TodoDetail(
        navController = navController,
        todo = todo,
        deleteTodo = {todoViewModel.deleteTodo(it)}
    )

}

@Composable
fun TodoDetail(
    navController: NavHostController,
    todo: Todo?,
    deleteTodo: (Todo) -> Unit
){
    val materialBlue700= Color(0xFF1976D2)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "TopAppBar", color = Color.White)
                },
                backgroundColor = materialBlue700,
                navigationIcon = {
                    // navigation icon is use
                    // for drawer icon.
                    IconButton(onClick = { navController.popBackStack()}) {
                        // below line is use to
                        // specify navigation icon.
                        Icon(Icons.Filled.ArrowBack, "", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                            todo?.let { deleteTodo(it)
                            navController.popBackStack()
                        } }) {
                        Icon(
                            Icons.Filled.Delete, "",
                            tint = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

            )
        },
        content = { Text("BodyContent") },
        )
}