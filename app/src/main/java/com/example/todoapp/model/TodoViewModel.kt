package com.example.todoapp.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todoapp.R
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.repository.TodoRepository
import java.util.*

class TodoViewModel(private val repository: TodoRepository ) : ViewModel(){
    var displayTaskDetails = mutableStateOf(false)
    var taskName = mutableStateOf("")
    var taskDetail = mutableStateOf("")

    var expanded = mutableStateOf(false)
    var selectedIndex =  mutableStateOf(-1)

    val items = listOf("Work", "Shopping", "Learning", "Gaming", "Cooking", "Exercise", "Home")
    val itemsIcons = listOf(
        R.drawable.ic_baseline_work_24,
        R.drawable.ic_baseline_add_shopping_cart_24,
        R.drawable.ic_baseline_menu_book_24,
        R.drawable.ic_baseline_computer_24,
        R.drawable.ic_baseline_outdoor_grill_24,
        R.drawable.ic_baseline_fitness_center_24,
        R.drawable.ic_baseline_home_24,
    )

    var selectedHour =  mutableStateOf(0)
    var selectedMinute =  mutableStateOf(0)


    var selectedDay = mutableStateOf(0)
    var selectedMonth = mutableStateOf(0)
    var selectedYear = mutableStateOf(0)

    fun taskDetailDisplayChange(){
        displayTaskDetails.value = if(displayTaskDetails.value) false else true
    }

    fun onTaskNameChange(name: String){
        taskName.value = name
    }

    fun onTaskDetailChange(name: String){
        taskDetail.value = name
    }

    fun clearValues(){
        taskName.value = ""
        taskDetail.value = ""
    }

    fun onExpand(expand: Boolean){
        expanded.value = expand
    }

    fun onSelectedIndexChange(index: Int){
        selectedIndex.value = index
    }

    fun addTime(hourOfDay: Int, minuteOfDay: Int){
        selectedHour.value = hourOfDay
        selectedMinute.value = minuteOfDay
    }

    fun addDate(day: Int, month: Int, year: Int){
        selectedDay.value = day
        selectedMonth.value = month
        selectedYear.value = year
    }

    fun insertTodo(){
        Log.v("sada", " taskName.value: ${ taskName.value}" )
        Log.v("sada", " taskDetail.value: ${ taskDetail.value}" )
        Log.v("sada", " selectedIndex.value: ${ selectedIndex.value}" )


        val todo = Todo(
            UUID.randomUUID(),
            taskName.value,
            taskDetail.value,
            if(selectedIndex.value != -1) items[selectedIndex.value] else null,
            Calendar.getInstance().getTime()
        )

        Log.v("sada", "todo item $todo" )
        repository.insertTodo(todo)
    }
}