package com.example.todoapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                        Icon(
                            painter = painterResource(id = itemsIcons[selectedIndex]),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = items[selectedIndex])
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {removeCategory()},
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

