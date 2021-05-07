package com.example.todoapp.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.model.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalMaterialApi
@Composable
fun DrawerButtonsContent(
    todoViewModel: TodoViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
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

    Log.v("sada", "tasknama $taskName ${taskName == ""}")

    Buttons(
        taskDetailDisplayChange = {todoViewModel.taskDetailDisplayChange()},
        onExpand = {todoViewModel.onExpand(it)},
        timePickerDialog = timePickerDialog,
        datePickerDialog = datePickerDialog,
        insertTodo = {todoViewModel.insertTodo()},
        taskName = taskName,
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        coroutineScope = coroutineScope,
        )

}

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
    ){

    Row(){
        IconButton(
            onClick = taskDetailDisplayChange,
        ){
            Icon(
                Icons.Filled.Add,
                contentDescription = "",
                tint = Color.Blue
            )
        }
        IconButton(
            onClick = {onExpand(true)},
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
                    bottomSheetScaffoldState.bottomSheetState.collapse()
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