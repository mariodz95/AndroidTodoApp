package com.example.todoapp.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.R
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.util.AlarmReceiver
import java.util.*

class TodoViewModel(private val repository: TodoRepository ) : ViewModel(){
    var todoList: LiveData<MutableList<Todo>> =  MutableLiveData<MutableList<Todo>>()
    var finishedTodoList: LiveData<MutableList<Todo>> =  MutableLiveData<MutableList<Todo>>()

    var isCollapsed = mutableStateOf(false)

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

    @ExperimentalComposeUiApi
    val keyboardController = mutableStateOf(  LocalSoftwareKeyboardController )


    init{
        getAllTodos(0)
        getAllTodos(1)
    }

    fun setRemainder(context: Context){
        if(selectedYear.value != 0){
            val notificationId = 46

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("notificationId", notificationId)
            intent.putExtra("notificationText", taskName.value)

            val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            var alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour.value);
            calendar.set(Calendar.MINUTE, selectedMinute.value);
            calendar.set(Calendar.SECOND, 0);

            alarm.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
        }
    }

    fun collapse(){
        isCollapsed.value = if(isCollapsed.value) false else true
    }

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
        removeCategory()
        removeDate()
        displayTaskDetails.value = false
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

    fun removeCategory(){
        selectedIndex.value = -1
    }

    fun removeDate(){
        selectedDay.value = 0
        selectedMonth.value = 0
        selectedYear.value = 0
    }

    fun insertTodo(){
        val todo = Todo(
            UUID.randomUUID(),
            taskName.value,
            taskDetail.value,
            if(selectedIndex.value != -1) items[selectedIndex.value] else null,
            Calendar.getInstance().getTime()
        )

        repository.insertTodo(todo)
    }

    fun getAllTodos(doneStatus: Int){
        if(doneStatus == 0) {
            todoList = repository.getAllTodos(doneStatus)
        }else{
            finishedTodoList = repository.getAllTodos(1)
        }
    }

    fun checkTodo(todo: Todo){
        repository.updateTodoCheckStatus(if(todo.isDone) false else true, todo.id)
    }
}