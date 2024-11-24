package com.harisfi.listo.screens.edit_todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.commons.composable.*
import com.harisfi.listo.commons.ext.fieldModifier
import com.harisfi.listo.commons.ext.spacer
import com.harisfi.listo.commons.ext.toolbarActions
import com.harisfi.listo.models.Todo
import com.harisfi.listo.ui.theme.ListoTheme

@Composable
fun EditTodoScreen(
    popUpScreen: () -> Unit,
    viewModel: EditTodoViewModel = hiltViewModel()
) {
    val todo by viewModel.todo

    EditTodoScreenContent(
        todo = todo,
        onDoneClick = { viewModel.onDoneClick(popUpScreen) },
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
    )
}

@Composable
fun EditTodoScreenContent(
    modifier: Modifier = Modifier,
    todo: Todo,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.edit_todo,
            modifier = Modifier.toolbarActions(),
            primaryActionIcon = AppIcon.ic_check,
            primaryAction = { onDoneClick() }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()
        BasicField(AppText.title, todo.title, onTitleChange, fieldModifier)
        BasicField(AppText.description, todo.description, onDescriptionChange, fieldModifier)
        Spacer(modifier = Modifier.spacer())
    }
}

@Preview(showBackground = true)
@Composable
fun EditTodoScreenPreview() {
    val todo = Todo(
        title = "Todo title",
        description = "Todo description"
    )

    ListoTheme {
        EditTodoScreenContent(
            todo = todo,
            onDoneClick = { },
            onTitleChange = { },
            onDescriptionChange = { }
        )
    }
}