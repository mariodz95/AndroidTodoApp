package com.example.todoapp.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

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
        deleteTodo = {todoViewModel.deleteTodo(it)},
        checkTodo = {todoViewModel.checkTodo(it)}
    )

}

@Composable
fun TodoDetail(
    navController: NavHostController,
    todo: Todo?,
    deleteTodo: (Todo) -> Unit,
    checkTodo: (Todo) -> Unit
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
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(8.dp),
                onClick =  {
                    checkTodo(todo!!)
                    navController.popBackStack()
                },
                backgroundColor = Color(0xFF1976D2),
                shape = CircleShape,
            ){
                Icon(Icons.Filled.Done, "")
            }
                               },
        content = {
            Column(
                modifier = Modifier.padding(24.dp)
            ){
                Row(
                    modifier = Modifier.padding(8.dp)
                ){
                    Text("${todo?.name}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }

                Row(
                    modifier = Modifier.padding(8.dp)
                ){
                    if(todo?.details == ""){
                        Text("No details!", fontSize = 20.sp)

                    }else{
                        Text("Details: ${todo?.details}", fontSize = 20.sp)
                    }
                }
                Row(
                    modifier = Modifier.padding(8.dp)
                ){
                    if(todo?.category == null){
                        Text("No category!")
                    }else{
                        Text("${todo?.category}")

                    }
                }

                Row(
                    modifier = Modifier.padding(8.dp)
                ){
                    Text("${todo?.dateAdded}")
                }
            }
                  },
        )
}