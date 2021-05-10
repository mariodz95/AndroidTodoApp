package com.example.todoapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.components.*
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel
import com.example.todoapp.model.TodoViewModelFactory
import com.example.todoapp.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val repository : TodoRepository by inject()
    private lateinit var todoViewModel: TodoViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var factory = TodoViewModelFactory(repository)
        todoViewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]


        setContent {
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
            )
            HomeScreen(bottomSheetScaffoldState, todoViewModel)
                }
            }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(bottomSheetScaffoldState: BottomSheetScaffoldState, todoViewModel: TodoViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = remember {LocalSoftwareKeyboardController}
    val focusRequester = remember  { FocusRequester() }

    val taskName = todoViewModel.taskName.value
    val taskDetail = todoViewModel.taskDetail.value
    val displayTaskDetails = todoViewModel.displayTaskDetails.value

    var expanded = todoViewModel.expanded.value
    val items = todoViewModel.items
    val itemsIcons = todoViewModel.itemsIcons

    var isCollapsed = todoViewModel.isCollapsed.value

    val unfinishedTodoList: List<Todo> by todoViewModel.todoList.observeAsState(listOf())
    val finishedTodoList: List<Todo> by todoViewModel.finishedTodoList.observeAsState(listOf())

    Log.v("sada", "GOtova Lista: ${unfinishedTodoList}")

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text(
                    "Todo App",
                    color = Color.White
                )
            },
            backgroundColor = Color(0xFF1976D2)
        )  },
        content = {
            HomeContent(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                coroutineScope = coroutineScope,
                taskName = taskName,
                taskDetail = taskDetail,
                onTaskNameChange = { todoViewModel.onTaskNameChange(it) },
                onTaskDetailChange = { todoViewModel.onTaskDetailChange(it) },
                keyboardController = keyboardController.current,
                focusRequester = focusRequester,
                displayTaskDetails = displayTaskDetails,
                clearValues = {todoViewModel.clearValues()},
                todoViewModel = todoViewModel,
                isCollapsed = isCollapsed,
                collapse = {todoViewModel.collapse()},
                unfinishedTodoList = unfinishedTodoList,
                finishedTodoList = finishedTodoList
            )

            Dropdown(
                expanded = expanded,
                items = items,
                onExpand = {todoViewModel.onExpand(it)},
                onSelectedIndexChange = {todoViewModel.onSelectedIndexChange(it)},
                itemsIcons = itemsIcons
                )
        },
    )
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun HomeContent(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    taskName: String,
    taskDetail: String,
    onTaskNameChange: (String) -> Unit,
    onTaskDetailChange: (String) -> Unit,
    keyboardController:  SoftwareKeyboardController?,
    focusRequester: FocusRequester,
    displayTaskDetails: Boolean,
    clearValues : () -> Unit,
    todoViewModel: TodoViewModel,
    isCollapsed: Boolean,
    collapse: () -> Unit,
    unfinishedTodoList: List<Todo>,
    finishedTodoList: List<Todo>
){
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box (
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(20.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    TaskFormTextField(
                        focusRequester = focusRequester,
                        coroutineScope = coroutineScope,
                        keyboardController = keyboardController,
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        placeholder = "New task",
                        value = taskName,
                        onValueChange = onTaskNameChange,

                    )
                    if(displayTaskDetails) {
                        Spacer(modifier = Modifier.height(16.dp))
                        TaskFormTextField(
                            focusRequester = focusRequester,
                            coroutineScope = coroutineScope,
                            keyboardController = keyboardController,
                            bottomSheetScaffoldState = bottomSheetScaffoldState,
                            placeholder = "Task Detail",
                            value = taskDetail,
                            onValueChange = onTaskDetailChange,

                            )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerDateAndCategoryContent(todoViewModel = todoViewModel)
                    DrawerButtonsContent(todoViewModel = todoViewModel, bottomSheetScaffoldState, coroutineScope,
                    )
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            keyboardController?.hide()
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                            clearValues()

                        },
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                TodoListContent(
                    todoViewModel = todoViewModel,
                    todoList = unfinishedTodoList
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            collapse()
                        }
                        .padding(10.dp)
                ){
                    Text("Done", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = {collapse() }) {

                        Icon(if(isCollapsed) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp, "")
                    }
                }
                if(isCollapsed) {
                    TodoListContent(
                        todoViewModel = todoViewModel,
                        todoList = finishedTodoList
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(10.dp),
                    onClick =  {
                        coroutineScope.launch {
                            focusRequester.requestFocus()
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    },
                    backgroundColor = Color(0xFF1976D2),
                    shape = CircleShape,
                ){
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    }
}


