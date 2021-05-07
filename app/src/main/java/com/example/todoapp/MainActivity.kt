package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.components.Dropdown
import com.example.todoapp.components.TaskFormTextField
import com.example.todoapp.model.TodoViewModel
import com.example.todoapp.model.TodoViewModelFactory
import com.example.todoapp.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : ComponentActivity() {

    private val repository : TodoRepository by inject()
    private lateinit var todoViewModel: TodoViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var factory = TodoViewModelFactory(repository)
        todoViewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]


        setContent {
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
            )
            HomeScreen(bottomSheetScaffoldState, todoViewModel)
                }
            }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(bottomSheetScaffoldState: BottomSheetScaffoldState, todoViewModel: TodoViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = remember {LocalSoftwareKeyboardController}
    val focusRequester = remember  { FocusRequester() }

    val taskName = todoViewModel.taskName.value
    val taskDetail = todoViewModel.taskDetail.value
    val displayTaskDetails = todoViewModel.displayTaskDetails.value

    var selectedIndex = todoViewModel.selectedIndex.value
    var expanded = todoViewModel.expanded.value
    val items = todoViewModel.items
    val itemsIcons = todoViewModel.itemsIcons

    var selectedHour = todoViewModel.selectedHour.value
    var selectedMinute = todoViewModel.selectedMinute.value

    var selectedDay = todoViewModel.selectedDay.value
    var selectedMonth = todoViewModel.selectedMonth.value
    var selectedYear = todoViewModel.selectedYear.value

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context, DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, day: Int, month: Int, year: Int ->
            todoViewModel.addDate(day = day, month = month, year = year)
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        context,
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            todoViewModel.addTime(hourOfDay = hourOfDay, minuteOfDay = minute)
        }, mHour, mMinute, false
    )

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text(
                    "Todo App",
                    color = Color.White
                )
            },
            backgroundColor = Color(0xFF1976D2)
        )  },
        content = {
            HomeContent(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                coroutineScope = coroutineScope,
                taskName = taskName,
                taskDetail = taskDetail,
                onTaskNameChange = { todoViewModel.onTaskNameChange(it) },
                onTaskDetailChange = { todoViewModel.onTaskDetailChange(it) },
                keyboardController = keyboardController.current,
                focusRequester = focusRequester,
                displayTaskDetails = displayTaskDetails,
                taskDetailDisplayChange = { todoViewModel.taskDetailDisplayChange() },
                clearValues = {todoViewModel.clearValues()},
                onExpand = {todoViewModel.onExpand(it)},
                items = items,
                itemsIcons = itemsIcons,
                selectedIndex = selectedIndex,
                datePickerDialog = datePickerDialog,
                timePickerDialog = timePickerDialog,
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                selectedDay = selectedDay,
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                insertTodo = {todoViewModel.insertTodo()},
                todoViewModel = todoViewModel
            )

            Dropdown(
                expanded = expanded,
                items = items,
                onExpand = {todoViewModel.onExpand(it)},
                onSelectedIndexChange = {todoViewModel.onSelectedIndexChange(it)},
                itemsIcons = itemsIcons
                )
        },
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun HomeContent(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    taskName: String,
    taskDetail: String,
    onTaskNameChange: (String) -> Unit,
    onTaskDetailChange: (String) -> Unit,
    keyboardController:  SoftwareKeyboardController?,
    focusRequester: FocusRequester,
    displayTaskDetails: Boolean,
    taskDetailDisplayChange: () -> Unit,
    clearValues : () -> Unit,
    onExpand: (Boolean) -> Unit,
    items: List<String>,
    itemsIcons: List<Int>,
    selectedIndex: Int,
    datePickerDialog: DatePickerDialog,
    timePickerDialog: TimePickerDialog,
    selectedHour: Int,
    selectedMinute: Int,
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    insertTodo: () -> Unit,
    todoViewModel: TodoViewModel
){
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box (
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(20.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    TaskFormTextField(
                        focusRequester = focusRequester,
                        coroutineScope = coroutineScope,
                        keyboardController = keyboardController,
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        placeholder = "New task",
                        value = taskName,
                        onValueChange = onTaskNameChange,

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if(displayTaskDetails) {
                        TaskFormTextField(
                            focusRequester = focusRequester,
                            coroutineScope = coroutineScope,
                            keyboardController = keyboardController,
                            bottomSheetScaffoldState = bottomSheetScaffoldState,
                            placeholder = "Task Detail",
                            value = taskDetail,
                            onValueChange = onTaskDetailChange,

                            )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =  Arrangement.Center
                    ){
                        if(selectedIndex != -1){
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
                                    Row(){
                                        Icon(
                                            painter = painterResource(id = itemsIcons[selectedIndex]),
                                            contentDescription = null // decorative element
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = items[selectedIndex])
                                    }
                                }
                                }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        if(selectedDay != 0){
                            Card(

                                shape = RoundedCornerShape(3.dp)
                            ){
                                Column(
                                    modifier = Modifier
                                        .height(25.dp)
                                        .width(125.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("$selectedDay/$selectedMonth/$selectedYear $selectedHour:$selectedMinute")
                                }
                            }
                        }

                    }
                    ButtonsContent(todoViewModel = todoViewModel)

   /*                 Row(){
                        IconButton(
                            onClick = taskDetailDisplayChange,
                        ){
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = ""
                            )
                        }
                        IconButton(
                            onClick = {onExpand(true)},
                        ){
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = ""
                            )
                        }
                        IconButton(
                            onClick = {
                                timePickerDialog.show()
                                datePickerDialog.show()
                                      },
                        ){
                            Icon(
                                Icons.Filled.DateRange,
                                contentDescription = ""
                            )
                        }

                        IconButton(
                            onClick = {insertTodo()},
                        ){
                            Icon(
                                Icons.Filled.Send,
                                contentDescription = ""
                            )
                        }
                    }*/
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            keyboardController?.hide()
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                            clearValues()
                        },
                    )
                },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(10.dp),
                onClick =  {
                    coroutineScope.launch {
                        focusRequester.requestFocus()
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                },
                backgroundColor = Color(0xFF1976D2),
                shape = CircleShape,
            ){
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}

@Composable
fun ButtonsContent(todoViewModel: TodoViewModel){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context, DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, day: Int, month: Int, year: Int ->
            todoViewModel.addDate(day = day, month = month, year = year)
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        context,
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            todoViewModel.addTime(hourOfDay = hourOfDay, minuteOfDay = minute)
        }, mHour, mMinute, false
    )

    Buttons(
        taskDetailDisplayChange = {todoViewModel.taskDetailDisplayChange()},
        onExpand = {todoViewModel.onExpand(it)},
        timePickerDialog = timePickerDialog,
        datePickerDialog = datePickerDialog,
        insertTodo = {todoViewModel.insertTodo()}
    )

}

@Composable
fun Buttons(
    taskDetailDisplayChange: () -> Unit,
    onExpand: (Boolean) -> Unit,
    timePickerDialog: TimePickerDialog,
    datePickerDialog: DatePickerDialog,
    insertTodo: () -> Unit
){

    Row(){
        IconButton(
            onClick = taskDetailDisplayChange,
        ){
            Icon(
                Icons.Filled.Add,
                contentDescription = ""
            )
        }
        IconButton(
            onClick = {onExpand(true)},
        ){
            Icon(
                Icons.Filled.Star,
                contentDescription = ""
            )
        }
        IconButton(
            onClick = {
                timePickerDialog.show()
                datePickerDialog.show()
            },
        ){
            Icon(
                Icons.Filled.DateRange,
                contentDescription = ""
            )
        }

        IconButton(
            onClick = {insertTodo()},
        ){
            Icon(
                Icons.Filled.Send,
                contentDescription = ""
            )
        }
    }

}