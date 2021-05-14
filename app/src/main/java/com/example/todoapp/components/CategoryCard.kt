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


@Composable
fun CategoryCard(
    itemsIcons: List<Int>,
    items: List<String>,
    selectedIndex: Int,
    removeCategory: () -> Unit
){
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