package com.example.todoapp.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todoapp.R
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
        val context = LocalContext.current
        TodoDetail(
            navController = navController,
            todo = todoItem,
            deleteTodo = {todoViewModel.deleteTodo(it, context)},
            checkTodo = {todoViewModel.checkTodo(it)},
            todoDetailDisplayNameChange = { todoViewModel.todoDetailDisplayNameChange(it)},
            todoDetailDisplayDetailChange ={ todoViewModel.todoDetailDisplayDetailChange(it)},
            todoDetailDisplayName = todoDetailDisplayName,
            todoDetailDisplayDetail = todoDetailDisplayDetail,
            enableAddValue = {todoViewModel.enableAddValue()},
            clearDisplayValues ={todoViewModel.clearDisplayValues()},
            updateTodo = {todoViewModel.updateTodo(todoDetailDisplayName, todoDetailDisplayDetail, todo.id)},
            cancelNotification = { todoViewModel.cancelNotification(todo.requestCode!!, context) },
            removeRemainder = { todoViewModel.removeRemainder(todo.id, todo.requestCode!!, context) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
    cancelNotification: () -> Unit,
    removeRemainder: () -> Unit,
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
                        enabled= if(todo?.isDone!!) false else true,
                        value = todoDetailDisplayName,
                        onValueChange = {todoDetailDisplayNameChange(it)},
                        textStyle = TextStyle(color = if(todo?.isDone!!) Color.Gray else Color.Black, fontWeight = FontWeight.Bold, fontSize = 24.sp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Icon( painter = painterResource(R.drawable.ic_baseline_description_24), "", tint = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    TextField(
                        enabled= if(todo?.isDone!!) false else true,
                        value = todoDetailDisplayDetail,
                        onValueChange = {todoDetailDisplayDetailChange(it)},
                        textStyle = TextStyle(color = if(todo?.isDone!!) Color.Gray else Color.Black, fontSize = 20.sp),
                        label = { Text("Details!") },
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Icon( painter = painterResource(R.drawable.ic_baseline_category_24), "", tint = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    Spacer(modifier = Modifier.width(16.dp))
                    if(todo?.category == null){
                        Text(text = "No category!", color = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    }else{
                        Text(text = "${todo.category}", color = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ){
                    Icon( painter = painterResource(R.drawable.ic_baseline_date_range_24), "", tint = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    Spacer(modifier = Modifier.width(16.dp))

                    if(todo?.remainder == null){
                        Text(
                            text = "No remainder!",
                            color = if (todo?.isDone!!) Color.Gray else Color.Black,
                        )
                    }else {
                        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                        val date = sdf.format(todo?.remainder?.time)
                        Card(
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(160.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "$date", color = if (todo?.isDone!!) Color.Gray else Color.Black,)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = {removeRemainder()},
                                        enabled = if (todo?.isDone!!) false else true,
                                        modifier = Modifier
                                            .then(Modifier.size(20.dp))
                                            .border(
                                                0.5.dp, Color.Black,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "",
                                            tint = if(todo?.isDone!!) Color.Gray else Color.Black
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ){
                    Icon( painter = painterResource(R.drawable.ic_baseline_date_range_24), "", tint = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    Spacer(modifier = Modifier.width(16.dp))
                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val date = sdf.format(todo?.dateAdded?.time)
                    Text(text = "Created at: ${date}", color = if(todo?.isDone!!) Color.Gray else Color.Black,)
                }
            } },
        )
}
