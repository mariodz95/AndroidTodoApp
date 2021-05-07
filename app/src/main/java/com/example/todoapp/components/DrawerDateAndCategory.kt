package com.example.todoapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        selectedHour = selectedHour
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
    selectedHour: Int
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
}


