package com.harisfi.listo.screens.todos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harisfi.listo.models.Todo
import com.harisfi.listo.ui.theme.ListoTheme
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText

@Composable
fun TodosScreen(
    openScreen: (String) -> Unit,
    viewModel: TodosViewModel = hiltViewModel()
) {
    val todos = viewModel.todos.collectAsStateWithLifecycle(emptyList())

    TodosScreenContent(
        todos = todos.value,
        onAddClick = viewModel::onAddClick,
        onSettingsClick = viewModel::onSettingsClick,
        onTodoCheckChange = viewModel::onTodoCheckChange,
        onTodoActionClick = viewModel::onTodoActionClick,
        openScreen = openScreen
    )

    LaunchedEffect(viewModel) { }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodosScreenContent(
    modifier: Modifier = Modifier,
    todos: List<Todo>,
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
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .padding(12.dp, 0.dp, 12.dp, 0.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(AppText.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp
                )

                FilledTonalIconButton(onClick = { onSettingsClick(openScreen) }) {
                    Icon(
                        painter = painterResource(AppIcon.ic_settings),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Settings"
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            LazyColumn {
                items(todos, key = { it.id }) { todoItem ->
                    TodoItem(
                        todo = todoItem,
                        onCheckChange = { onTodoCheckChange(todoItem) },
                        onActionClick = { action ->
                            onTodoActionClick(
                                openScreen,
                                todoItem,
                                action
                            )
                        }
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
        completed = true
    )

    ListoTheme {
        TodosScreenContent(
            todos = listOf(todo),
            onAddClick = { },
            onSettingsClick = { },
            onTodoCheckChange = { },
            onTodoActionClick = { _, _, _ -> },
            openScreen = { }
        )
    }
}
