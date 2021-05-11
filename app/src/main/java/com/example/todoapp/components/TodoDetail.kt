package com.example.todoapp.components

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.todoapp.database.entity.Todo

@Composable
fun TodoDetail() {
    val materialBlue700= Color(0xFF1976D2)
    Scaffold(
        topBar = { TopAppBar(title = {Text("TopAppBar")},backgroundColor = materialBlue700)  },
        content = { Text("BodyContent") },
    )
}