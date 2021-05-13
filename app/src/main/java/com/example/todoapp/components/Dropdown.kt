package com.example.todoapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun Dropdown(
    expanded: Boolean,
    items: List<String>,
    onExpand: (Boolean) -> Unit,
    onSelectedIndexChange: (Int) -> Unit,
    itemsIcons: List<Int>,
) {
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpand(false) },
            modifier = Modifier.fillMaxWidth().background(
                Color.White)
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onSelectedIndexChange(index)
                    onExpand(false)
                }) {
                    Icon(
                        painter = painterResource(id = itemsIcons[index]),
                        contentDescription = null
                    )
                    Text(text = item)
                }
            }
        }
    }
}