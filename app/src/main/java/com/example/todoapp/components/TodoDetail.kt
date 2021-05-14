package com.example.todoapp.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodoDetailContent(navController: NavHostController, todoString: String?, todoViewModel: TodoViewModel) {
    var todo: Todo? = null
    if(todoString != null){
        val gson = Gson()
        todo = gson.fromJson(todoString, Todo::class.java)
    }
    todoViewModel.getTodoById(todo?.id!!)

    val todoDetailDisplayName = todoViewModel.todoDetailDisplayName.value
    val todoDetailDisplayDetail = todoViewModel.todoDetailsDisplayDetails.value
    val todoCategoryDisplay = todoViewModel.todoCategoryDisplay.value
    val todoRemainderDisplay = todoViewModel.todoRemainderDisplay.value
    val addValue = todoViewModel.addValue.value

    val expanded = todoViewModel.expanded.value
    val items = todoViewModel.items
    val itemsIcons = todoViewModel.itemsIcons

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]
    val context = LocalContext.current

    val timePickerDialog = TimePickerDialog(context, R.style.DialogTheme,
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            todoViewModel.addTime(hourOfDay = hourOfDay, minuteOfDay = minute)
        }, mHour, mMinute, true
    )

    val datePickerDialog = DatePickerDialog(
        context, R.style.DialogTheme,
        DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, year: Int, month: Int, day: Int ->
            todoViewModel.addDate(day = day, month = month, year = year)
            timePickerDialog.show()
        }, year, month, day
    )

    timePickerDialog.setOnDismissListener {
        todoViewModel.addTodoRemainder(todo.id, context)
    }

    if(todo != null) {
        if(addValue){
            todoViewModel.todoDetailDisplayNameChange(todo?.name!!)
            todoViewModel.todoDetailDisplayDetailChange(todo?.details!!)
            todoViewModel.todoDetailsCategoryChange(if(todo.category != null) todo?.category!! else "")
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            todoViewModel.todoRemainderCategoryChange(if(todo.remainder != null) sdf.format(todo?.remainder).toString() else "")
            todoViewModel.disableAddValue()
        }
        val context = LocalContext.current
        TodoDetail(
            navController = navController,
            todo = todo,
            deleteTodo = {todoViewModel.deleteTodo(it, context)},
            checkTodo = {todoViewModel.checkTodo(it, context)},
            todoDetailDisplayNameChange = { todoViewModel.todoDetailDisplayNameChange(it)},
            todoDetailDisplayDetailChange ={ todoViewModel.todoDetailDisplayDetailChange(it)},
            todoDetailDisplayName = todoDetailDisplayName,
            todoDetailDisplayDetail = todoDetailDisplayDetail,
            enableAddValue = {todoViewModel.enableAddValue()},
            clearDisplayValues ={todoViewModel.clearDisplayValues()},
            updateTodo = {todoViewModel.updateTodo(todoDetailDisplayName, todoDetailDisplayDetail, todo.id)},
            removeRemainder = { todoViewModel.removeRemainder(todo.id, todo.requestCode!!, context) },
            onExpand = {todoViewModel.onExpand(it)},
            datePickerDialog = datePickerDialog,
            todoCategoryDisplay = todoCategoryDisplay,
            todoRemainderDisplay = todoRemainderDisplay,
            clearValues = {todoViewModel.clearValues()},
            removeDateDisplay = {todoViewModel.removeDateDisplay(todo.id)}
            )
        Dropdown(
            expanded = expanded,
            items = items,
            onExpand = {todoViewModel.onExpand(it)},
            onSelectedIndexChange = {todoViewModel.onSelectedIndexChange(it, true, todo.id)},
            itemsIcons = itemsIcons
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
    removeRemainder: () -> Unit,
    onExpand: (Boolean) -> Unit,
    datePickerDialog: DatePickerDialog,
    todoCategoryDisplay: String,
    todoRemainderDisplay: String,
    clearValues : () -> Unit,
    removeDateDisplay: () -> Unit,
    ){
    val materialBlue700= Color(0xFF1976D2)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Details", color = Color.White)
                },
                backgroundColor = materialBlue700,
                navigationIcon = {
                    IconButton(onClick = {
                        updateTodo()
                        clearDisplayValues()
                        enableAddValue()
                        clearValues()
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
                    modifier = if(todo?.isDone!!) Modifier.fillMaxWidth() else Modifier
                        .fillMaxWidth()
                        .clickable {
                            onExpand(true)
                        }
                ){
                    Icon( painter = painterResource(R.drawable.ic_baseline_category_24), "", tint = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    Spacer(modifier = Modifier.width(16.dp))
                    if(todoCategoryDisplay == ""){
                        Text(
                            text = "No category!",
                            color = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    }else{
                        TodoDetailCategoryCard(todoCategoryDisplay = todoCategoryDisplay, removeDateDisplay = removeDateDisplay)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = if(todo?.isDone!!) Modifier.fillMaxWidth() else Modifier
                        .fillMaxWidth()
                        .clickable {
                            datePickerDialog.show()
                        },
                ){
                    Icon( painter = painterResource(R.drawable.ic_baseline_date_range_24), "", tint = if(todo?.isDone!!) Color.Gray else Color.Black,)
                    Spacer(modifier = Modifier.width(16.dp))
                    if(todoRemainderDisplay == ""){
                        Text(
                            text = "No remainder!",
                            color = if (todo?.isDone!!) Color.Gray else Color.Black,
                        )
                    }else {
                        TodoDetailRemainderCard(
                            todoRemainderDisplay = todoRemainderDisplay,
                            todo = todo,
                            removeRemainder = removeRemainder
                        )
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

@Composable
fun TodoDetailCategoryCard(
    todoCategoryDisplay: String,
    removeDateDisplay: () -> Unit
){
    Card(
        shape = RoundedCornerShape(3.dp),
    ){
        Column(
            modifier = Modifier
                .height(25.dp)
                .width(125.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = todoCategoryDisplay)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {removeDateDisplay()},
                    modifier = Modifier
                        .then(Modifier.size(20.dp))
                        .border(
                            0.5.dp, Color.Black,
                            shape = CircleShape
                        )
                ){
                    Icon(Icons.Filled.Close, "")
                }
            }
        }
    }
}

@Composable
fun TodoDetailRemainderCard(
    todoRemainderDisplay: String,
    todo: Todo,
    removeRemainder: () -> Unit,
){
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
                Text(
                    text = todoRemainderDisplay,
                    color = if (todo?.isDone!!) Color.Gray else Color.Black)
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