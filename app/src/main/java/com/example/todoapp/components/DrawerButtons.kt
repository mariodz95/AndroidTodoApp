package com.example.todoapp.components

import android.app.*
import android.content.Context
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.example.todoapp.R
import com.example.todoapp.model.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Composable
fun DrawerButtonsContent(
    todoViewModel: TodoViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    keyboardController: SoftwareKeyboardController?,
    navController: NavHostController
){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]
    val context = LocalContext.current

    val taskName = todoViewModel.taskName.value

    val timePickerDialog = TimePickerDialog(
        context, R.style.DialogTheme,
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

    Buttons(
        taskDetailDisplayChange = {todoViewModel.taskDetailDisplayChange()},
        onExpand = {todoViewModel.onExpand(it)},
        datePickerDialog = datePickerDialog,
        insertTodo = {todoViewModel.insertTodo()},
        taskName = taskName,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        coroutineScope = coroutineScope,
        clearValues = {todoViewModel.clearValues()},
        setRemainder = {todoViewModel.setRemainder(it)},
        keyboardController = keyboardController,
        addDrawerHeight = {todoViewModel.addDrawerHeight()},
        navController = navController

    )
}

@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Composable
fun Buttons(
    taskDetailDisplayChange: () -> Unit,
    onExpand: (Boolean) -> Unit,
    datePickerDialog: DatePickerDialog,
    insertTodo: () -> Unit,
    taskName: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    clearValues: () -> Unit,
    setRemainder: (Context) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    addDrawerHeight: () -> Unit,
    navController: NavHostController
){
    val context = LocalContext.current

    Row(){
        IconButton(
            onClick = {
                taskDetailDisplayChange()
                addDrawerHeight()
            },
        ){
            Icon(
                Icons.Filled.Add,
                contentDescription = "",
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = {
                onExpand(true)
                      },
        ){
            Icon(
                Icons.Filled.Star,
                contentDescription = "",
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = {
                datePickerDialog.show()
            },
        ){
            Icon(
                Icons.Filled.DateRange,
                contentDescription = "",
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = {
                navController.navigate("mapScreen")
            },
        ){
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = "",
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = {
                coroutineScope.launch {
                    setRemainder(context)
                    insertTodo()
                    keyboardController?.hide()
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                    clearValues()
                }},
            enabled = if(taskName == "") false else true,
        ){
            Icon(
                Icons.Filled.Send,
                contentDescription = "",
                tint = if(taskName == "") Color.Gray else Color.Blue
            )
        }
    }
}


