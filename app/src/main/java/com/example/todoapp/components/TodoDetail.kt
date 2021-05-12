package com.example.todoapp.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    todoViewModel.getTodoById(todo?.id!!)

    val todoItem by todoViewModel.todo.observeAsState()

    val todoDetailDisplayName = todoViewModel.todoDetailDisplayName.value
    val todoDetailDisplayDetail = todoViewModel.todoDetailsDisplayDetails.value
    val addValue = todoViewModel.addValue.value

    if(todoItem != null) {
        if(addValue){
            todoViewModel.todoDetailDisplayNameChange(todoItem?.name!!)
            todoViewModel.todoDetailDisplayDetailChange(todoItem?.details!!)
            todoViewModel.disableAddValue()
        }
        TodoDetail(
            navController = navController,
            todo = todoItem,
            deleteTodo = {todoViewModel.deleteTodo(it)},
            checkTodo = {todoViewModel.checkTodo(it)},
            todoDetailDisplayNameChange = { todoViewModel.todoDetailDisplayNameChange(it)},
            todoDetailDisplayDetailChange ={ todoViewModel.todoDetailDisplayDetailChange(it)},
            todoDetailDisplayName = todoDetailDisplayName,
            todoDetailDisplayDetail = todoDetailDisplayDetail,
            enableAddValue = {todoViewModel.enableAddValue()},
            clearDisplayValues ={todoViewModel.clearDisplayValues()},
            updateTodo = {todoViewModel.updateTodo(todoDetailDisplayName, todoDetailDisplayDetail, todo.id)}
        )
    }
}

@Composable
fun TodoDetail(
    navController: NavHostController,
    todo: Todo?,
    deleteTodo: (Todo) -> Unit,
    checkTodo: (Todo) -> Unit,
    todoDetailDisplayNameChange: (String) -> Unit,
    todoDetailDisplayDetailChange: (String) -> Unit,
    todoDetailDisplayName: String,
    todoDetailDisplayDetail: String,
    enableAddValue: () -> Unit,
    clearDisplayValues: () -> Unit,
    updateTodo: () -> Unit,
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
                    IconButton(onClick = {
                        updateTodo()
                        clearDisplayValues()
                        enableAddValue()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                            todo?.let {
                                deleteTodo(it)
                                clearDisplayValues()
                                enableAddValue()
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
                    enableAddValue()
                    checkTodo(todo!!)
                    navController.popBackStack()
                },
                backgroundColor = Color(0xFF1976D2),
                shape = CircleShape,
            ){
                Icon(Icons.Filled.Done, "")
            } },
        content = {
            Column(
                modifier = Modifier.padding(24.dp)
            ){
                Row(
                ){
                    TextField(
                        value = todoDetailDisplayName,
                        onValueChange = {todoDetailDisplayNameChange(it)},
                        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        label = { Text("") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
                Row(){
                    TextField(
                        value = todoDetailDisplayDetail,
                        onValueChange = {todoDetailDisplayDetailChange(it)},
                        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                        label = { Text("Add details!") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            disabledTextColor = Color.Transparent,
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
                Row(
                    modifier = Modifier.padding(8.dp)
                ){
                    if(todo?.category == null){
                        Text("No category!")
                    }else{
                        Text("${todo.category}")

                    }
                }
                Row(
                    modifier = Modifier.padding(8.dp)
                ){
                    Text("${todo?.dateAdded}")
                }
            } },
        )
}

@Composable
fun TodoDetailTextField(
    todo: Todo,
    updateValue: (String) -> Unit,
    displayText: String
){
    TextField(
        value = displayText,
        onValueChange = {updateValue(it)},
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
        label = { Text("") },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

