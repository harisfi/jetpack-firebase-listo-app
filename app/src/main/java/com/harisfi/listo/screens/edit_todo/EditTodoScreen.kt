package com.harisfi.listo.screens.edit_todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .padding(12.dp, 0.dp, 12.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(AppText.edit_todo),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )

            FilledTonalIconButton(onClick = { onDoneClick() }) {
                Icon(
                    painter = painterResource(AppIcon.ic_check),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Settings"
                )
            }
        }

        Spacer(Modifier.height(32.dp))

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