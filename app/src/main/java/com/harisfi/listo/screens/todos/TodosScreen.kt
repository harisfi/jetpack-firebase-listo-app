package com.harisfi.listo.screens.todos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.commons.composable.ActionToolbar
import com.harisfi.listo.commons.ext.toolbarActions
import com.harisfi.listo.models.Todo
import com.harisfi.listo.ui.theme.ListoTheme

@Composable
fun TodosScreen(
    openScreen: (String) -> Unit,
    viewModel: TodosViewModel = hiltViewModel()
) {
    val todos = viewModel.todos.collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options

    TodosScreenContent(
        todos = todos.value,
        options = options,
        onAddClick = viewModel::onAddClick,
        onSettingsClick = viewModel::onSettingsClick,
        onTodoCheckChange = viewModel::onTodoCheckChange,
        onTodoActionClick = viewModel::onTodoActionClick,
        openScreen = openScreen
    )

    LaunchedEffect(viewModel) { viewModel.loadTodoOptions() }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodosScreenContent(
    modifier: Modifier = Modifier,
    todos: List<Todo>,
    options: List<String>,
    onAddClick: ((String) -> Unit) -> Unit,
    onSettingsClick: ((String) -> Unit) -> Unit,
    onTodoCheckChange: (Todo) -> Unit,
    onTodoActionClick: ((String) -> Unit, Todo, String) -> Unit,
    openScreen: (String) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick(openScreen) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            ActionToolbar(
                title = AppText.todos,
                modifier = Modifier.toolbarActions(),
                primaryActionIcon = AppIcon.ic_settings,
                primaryAction = { onSettingsClick(openScreen) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(todos, key = { it.id }) { todoItem ->
                    TodoItem(
                        todo = todoItem,
                        options = options,
                        onCheckChange = { onTodoCheckChange(todoItem) },
                        onActionClick = { action -> onTodoActionClick(openScreen, todoItem, action) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodosScreenPreview() {
    val todo = Todo(
        title = "Todo title",
        flag = true,
        completed = true
    )

    val options = TodoActionOption.getOptions(hasEditOption = true)

    ListoTheme {
        TodosScreenContent(
            todos = listOf(todo),
            options = options,
            onAddClick = { },
            onSettingsClick = { },
            onTodoCheckChange = { },
            onTodoActionClick = { _, _, _ -> },
            openScreen = { }
        )
    }
}