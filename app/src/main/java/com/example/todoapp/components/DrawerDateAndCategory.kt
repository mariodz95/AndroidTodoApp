package com.example.todoapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.TodoViewModel

@Composable
fun DrawerDateAndCategoryContent(todoViewModel: TodoViewModel){

    var selectedIndex = todoViewModel.selectedIndex.value
    val items = todoViewModel.items
    val itemsIcons = todoViewModel.itemsIcons

    var selectedHour = todoViewModel.selectedHour.value
    var selectedMinute = todoViewModel.selectedMinute.value

    var selectedDay = todoViewModel.selectedDay.value
    var selectedMonth = todoViewModel.selectedMonth.value
    var selectedYear = todoViewModel.selectedYear.value

    DrawerDateAndCategory(
        selectedIndex = selectedIndex,
        itemsIcons = itemsIcons,
        items = items,
        selectedDay = selectedDay,
        selectedMonth = selectedMonth,
        selectedYear = selectedYear,
        selectedMinute = selectedMinute,
        selectedHour = selectedHour,
        removeCategory = { todoViewModel.removeCategory()},
        removeDate = { todoViewModel.removeDate()},
    )
}

@Composable
fun DrawerDateAndCategory(
    selectedIndex: Int,
    itemsIcons: List<Int>,
    items: List<String>,
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    selectedMinute: Int,
    selectedHour: Int,
    removeCategory: () -> Unit,
    removeDate: () -> Unit,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =  Arrangement.Center
    ){
        if(selectedIndex != -1){
            CategoryCard(
                itemsIcons = itemsIcons,
                items = items,
                selectedIndex = selectedIndex,
                removeCategory = removeCategory,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        if(selectedDay != 0){
            RemainderDateCard(
                selectedDay = selectedDay,
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                removeDate = removeDate
            )
        }
    }
}
