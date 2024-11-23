package com.harisfi.listo.screens.edit_todo

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.commons.composable.*
import com.harisfi.listo.commons.ext.card
import com.harisfi.listo.commons.ext.fieldModifier
import com.harisfi.listo.commons.ext.spacer
import com.harisfi.listo.commons.ext.toolbarActions
import com.harisfi.listo.models.Priority
import com.harisfi.listo.models.Todo
import com.harisfi.listo.ui.theme.ListoTheme
import java.util.Calendar

@Composable
fun EditTodoScreen(
    popUpScreen: () -> Unit,
    viewModel: EditTodoViewModel = hiltViewModel()
) {
    val todo by viewModel.todo
    val activity = LocalContext.current as AppCompatActivity

    EditTodoScreenContent(
        todo = todo,
        onDoneClick = { viewModel.onDoneClick(popUpScreen) },
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onUrlChange = viewModel::onUrlChange,
        onDateChange = viewModel::onDateChange,
        onTimeChange = viewModel::onTimeChange,
        onPriorityChange = viewModel::onPriorityChange,
        onFlagToggle = viewModel::onFlagToggle,
        activity = activity
    )
}

@Composable
fun EditTodoScreenContent(
    modifier: Modifier = Modifier,
    todo: Todo,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onPriorityChange: (String) -> Unit,
    onFlagToggle: (String) -> Unit,
    activity: AppCompatActivity?
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
        BasicField(AppText.url, todo.url, onUrlChange, fieldModifier)

        Spacer(modifier = Modifier.spacer())
        CardEditors(todo, onDateChange, onTimeChange, activity)
        CardSelectors(todo, onPriorityChange, onFlagToggle)

        Spacer(modifier = Modifier.spacer())
    }
}

@Composable
private fun CardEditors(
    todo: Todo,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    activity: AppCompatActivity?
) {
    RegularCardEditor(AppText.date, AppIcon.ic_calendar, todo.dueDate, Modifier.card()) {
        showDatePicker(activity, onDateChange)
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, todo.dueTime, Modifier.card()) {
        showTimePicker(activity, onTimeChange)
    }
}

@Composable
private fun CardSelectors(
    todo: Todo,
    onPriorityChange: (String) -> Unit,
    onFlagToggle: (String) -> Unit
) {
    val prioritySelection = Priority.getByName(todo.priority).name
    CardSelector(AppText.priority, Priority.getOptions(), prioritySelection, Modifier.card()) {
            newValue ->
        onPriorityChange(newValue)
    }

    val flagSelection = EditFlagOption.getByCheckedState(todo.flag).name
    CardSelector(AppText.flag, EditFlagOption.getOptions(), flagSelection, Modifier.card()) { newValue
        ->
        onFlagToggle(newValue)
    }
}

private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
    val calendar = Calendar.getInstance()

    activity?.let {
        val datePicker = android.app.DatePickerDialog(
            it,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateChange(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}

private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()

    activity?.let {
        val timePicker = TimePickerDialog(
            it,
            { _, hourOfDay, minute ->
                onTimeChange(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }
}

@Preview(showBackground = true)
@Composable
fun EditTodoScreenPreview() {
    val todo = Todo(
        title = "Todo title",
        description = "Todo description",
        flag = true
    )

    ListoTheme {
        EditTodoScreenContent(
            todo = todo,
            onDoneClick = { },
            onTitleChange = { },
            onDescriptionChange = { },
            onUrlChange = { },
            onDateChange = { },
            onTimeChange = { _, _ -> },
            onPriorityChange = { },
            onFlagToggle = { },
            activity = null
        )
    }
}