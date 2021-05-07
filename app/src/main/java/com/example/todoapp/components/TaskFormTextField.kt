package com.example.todoapp.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun TaskFormTextField(
    focusRequester: FocusRequester,
    coroutineScope: CoroutineScope,
    keyboardController:  SoftwareKeyboardController?,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
){
    TextField(
        modifier = Modifier.focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                coroutineScope.launch {
                    keyboardController?.hide()
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                    onValueChange("")
                }
            }),
        value = value,
        onValueChange = onValueChange,
        maxLines = 2,
        placeholder = { Text(placeholder) },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor =  Color.Transparent,
        )
    )
}