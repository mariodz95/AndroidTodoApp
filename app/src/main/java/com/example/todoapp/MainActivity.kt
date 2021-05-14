package com.example.todoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.todoapp.components.*
import com.example.todoapp.database.entity.Todo
import com.example.todoapp.model.TodoViewModel
import com.example.todoapp.model.TodoViewModelFactory
import com.example.todoapp.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
            val navController = rememberNavController()

            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
            )
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

            NavHost(navController, startDestination = "homeScreen") {
                composable(Screen.HomeScreen.route) { HomeScreen(bottomSheetScaffoldState, todoViewModel, navController) }
                composable(Screen.TodoDetailContent.route) { backStackEntry ->
                    TodoDetailContent(navController, backStackEntry.arguments?.getString("todo")!!, todoViewModel)
                }
            }
                }
            }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    todoViewModel: TodoViewModel,
    navController: NavHostController
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

    val display = todoViewModel.displayKeyboard.value

    if(display){
        keyboardController.current!!.show()
    }

    val height = todoViewModel.height.value

    val context = LocalContext.current

    val displayDone = todoViewModel.displayDone.value
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
        bottomBar = {
            BottomAppBar(backgroundColor = Color(0xFF1976D2), cutoutShape = CircleShape) {}
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {   FloatingActionButton(
            modifier = Modifier.padding(8.dp),
            onClick =  {
                todoViewModel.setDisplayKeyboard()
                coroutineScope.launch {
                    focusRequester.requestFocus()
                    bottomSheetScaffoldState.bottomSheetState.expand()
                    todoViewModel.setDisplayKeyboard()
                }
            },
            backgroundColor = Color(0xFF1976D2),
            shape = CircleShape,
        ){
            Icon(Icons.Filled.Add, "")
        }},
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
                finishedTodoList = finishedTodoList,
                height = height,
                navController = navController,
                context = context,
                insertTodo = {todoViewModel.insertTodo()},
                setRemainder = {todoViewModel.setRemainder(context)},
                displayDone = displayDone
            )

            Dropdown(
                expanded = expanded,
                items = items,
                onExpand = {todoViewModel.onExpand(it)},
                onSelectedIndexChange = {todoViewModel.onSelectedIndexChange(it, false, null)},
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
    finishedTodoList: List<Todo>,
    height: Int,
    navController: NavHostController,
    context: Context,
    insertTodo: () -> Unit,
    setRemainder: () -> Unit,
    displayDone: Boolean
){
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box (
                Modifier
                    .fillMaxWidth()
                    .height(410.dp + height.dp)
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
                        setRemainder =  setRemainder,
                        insertTodo = insertTodo,
                        clearValues = clearValues,
                        displayDone = displayDone
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
                            setRemainder =  setRemainder,
                            insertTodo = insertTodo,
                            clearValues = clearValues,
                            displayDone = displayDone
                            )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DrawerDateAndCategoryContent(todoViewModel = todoViewModel)
                    DrawerButtonsContent(todoViewModel = todoViewModel, bottomSheetScaffoldState, coroutineScope, keyboardController
                    )
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 60.dp)
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
            LazyColumn(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                items(unfinishedTodoList){ todo ->
                    TodoListRow(todo = todo, checked = { todoViewModel.checkTodo(it, context) }, navController = navController)
                }

                item {
                    if (finishedTodoList.size > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    collapse()
                                }
                                .padding(10.dp)
                        ) {
                            Text("Done", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(" (${finishedTodoList.size})")
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { collapse() }) {
                                Icon(
                                    if (isCollapsed) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                                    ""
                                )
                            }
                        }
                    }
                }
                if(isCollapsed) {
                    items(finishedTodoList){ todo ->
                        TodoListRow(todo = todo, checked = { todoViewModel.checkTodo(it, null) }, navController = navController)
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object HomeScreen : Screen("homeScreen", R.string.todo_details)
    object TodoDetailContent: Screen("todoDetails/{todo}", R.string.todo_details)
}
