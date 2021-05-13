package com.example.todoapp.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
    var todo: LiveData<Todo> = MutableLiveData<Todo>()

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
    val displayKeyboard = mutableStateOf(false)
    val height = mutableStateOf(0)

    val todoDetailDisplayName = mutableStateOf("")
    val todoDetailsDisplayDetails = mutableStateOf("")
    val addValue = mutableStateOf(true)

    val calendar = Calendar.getInstance()

    init{
        getAllTodos(0)
        getAllTodos(1)
    }

    fun deleteTodo(todo: Todo, context: Context){
        if(todo.requestCode != null){
            cancelNotification(todo.requestCode, context)
        }
        repository.deleteTodo(todo)
    }

    fun addDrawerHeight(){
        if(displayTaskDetails.value){
            height.value = 70
        }else{
            height.value = 0
        }
    }

    fun setDisplayKeyboard(){
        displayKeyboard.value = if(displayKeyboard.value) false else true
    }

    var requestCode = mutableStateOf(0)


    fun setRemainder(context: Context){
        if(selectedYear.value != 0){
            val notificationId = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            requestCode.value = (Date().time / 1000L % Int.MAX_VALUE).toInt()

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("notificationId", notificationId)
            intent.putExtra("notificationText", taskName.value)
            intent.putExtra("requestCode", requestCode.value)

            val alarmIntent = PendingIntent.getBroadcast(context, requestCode.value, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            var alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            calendar.set(Calendar.HOUR_OF_DAY, selectedHour.value)
            calendar.set(Calendar.MINUTE, selectedMinute.value)
            calendar.set(Calendar.SECOND, 0)

            calendar.set(Calendar.MONTH, selectedMonth.value)
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay.value)
            calendar.set(Calendar.YEAR, selectedYear.value)

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
        height.value = 0
    }

    fun onExpand(expand: Boolean){
        expanded.value = expand
    }

    fun onSelectedIndexChange(index: Int, updateTodo: Boolean, todoId: UUID?){
        selectedIndex.value = index
        if(updateTodo){
            addTodoCategory(items[selectedIndex.value], todoId!!)
        }
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
            Calendar.getInstance().time,
            false,
            requestCode.value,
            if(selectedYear.value == 0) null else calendar.time
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

    fun todoDetailDisplayNameChange(newName: String){
        todoDetailDisplayName.value = newName
    }

    fun todoDetailDisplayDetailChange(newDetail: String){
        todoDetailsDisplayDetails.value = newDetail
    }

    fun updateTodo(newName: String, newDetail: String, todoId: UUID){
        updateTodoName(newName, todoId)
        updateTodoDetail(newDetail, todoId)
        clearDisplayValues()
        addValue.value = true
    }

    fun updateTodoName(newName: String, todoId: UUID){
        repository.updateTodo(newName, todoId)
    }

    fun updateTodoDetail(newDetail: String, todoId: UUID){
        repository.updateTodoDetail(newDetail, todoId)
    }

    fun getTodoById(todoId: UUID){
        todo = repository.getTodoById(todoId)
    }

    fun disableAddValue(){
        addValue.value = false
    }

    fun enableAddValue(){
        addValue.value = true
    }

    fun clearDisplayValues(){
        todoDetailDisplayName.value = ""
        todoDetailsDisplayDetails.value = ""
    }

    fun cancelNotification(requestCode: Int, context: Context){
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmIntent.cancel()
    }

    fun removeRemainder(todoId: UUID, requestCode: Int, context: Context){
        cancelNotification(requestCode, context )
        repository.removeRemainder(todoId)
    }

    fun addTodoCategory(category: String, todoId: UUID){
        repository.addTodoCategory(category, todoId)
    }

    fun addTodoRemainder(todoId: UUID, context: Context){
        setRemainder(context)
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour.value)
        calendar.set(Calendar.MINUTE, selectedMinute.value)
        calendar.set(Calendar.SECOND, 0)

        calendar.set(Calendar.MONTH, selectedMonth.value)
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay.value)
        calendar.set(Calendar.YEAR, selectedYear.value)

        repository.addTodoRemainder(todoId, calendar.time)
    }
}