package com.example.todoapp.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.model.TodoViewModel
import java.util.*

@Composable
fun DrawerButtonsContent(todoViewModel: TodoViewModel){
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