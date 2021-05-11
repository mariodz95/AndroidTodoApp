package com.example.todoapp.components

import android.app.*
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
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
    keyboardController: SoftwareKeyboardController?
    ){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val mHour = c[Calendar.HOUR_OF_DAY]
    val mMinute = c[Calendar.MINUTE]
    val context = LocalContext.current

    val taskName = todoViewModel.taskName.value

    val datePickerDialog = DatePickerDialog(
        context, DatePickerDialog.OnDateSetListener
        { datePicker: DatePicker, year: Int, month: Int, day: Int ->
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
        insertTodo = {todoViewModel.insertTodo()},
        taskName = taskName,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        coroutineScope = coroutineScope,
        clearValues = {todoViewModel.clearValues()},
        setRemainder = {todoViewModel.setRemainder(it)},
        keyboardController = keyboardController,
        addDrawerHeight = {todoViewModel.addDrawerHeight()}
        )
}

@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Composable
fun Buttons(
    taskDetailDisplayChange: () -> Unit,
    onExpand: (Boolean) -> Unit,
    timePickerDialog: TimePickerDialog,
    datePickerDialog: DatePickerDialog,
    insertTodo: () -> Unit,
    taskName: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    clearValues: () -> Unit,
    setRemainder: (Context) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    addDrawerHeight: () -> Unit,
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
                timePickerDialog.show()
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
                coroutineScope.launch {
                    insertTodo()
                    keyboardController?.hide()
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                    setRemainder(context)
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


