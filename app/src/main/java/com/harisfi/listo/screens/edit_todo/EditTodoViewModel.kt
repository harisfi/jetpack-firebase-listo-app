package com.harisfi.listo.screens.edit_todo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.harisfi.listo.TODO_ID
import com.harisfi.listo.commons.ext.idFromParameter
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditTodoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageService: StorageService,
) : ListoViewModel() {
    val todo = mutableStateOf(Todo())

    init {
        val todoId = savedStateHandle.get<String>(TODO_ID)
        if (todoId != null) {
            launchCatching {
                todo.value = storageService.getTodo(todoId.idFromParameter()) ?: Todo()
            }
        }
    }

    fun onTitleChange(newValue: String) {
        todo.value = todo.value.copy(title = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        todo.value = todo.value.copy(description = newValue)
    }

    fun onUrlChange(newValue: String) {
        todo.value = todo.value.copy(url = newValue)
    }

    fun onDateChange(newValue: Long) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
        calendar.timeInMillis = newValue
        val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
        todo.value = todo.value.copy(dueDate = newDueDate)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val newDueTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
        todo.value = todo.value.copy(dueTime = newDueTime)
    }

    fun onFlagToggle(newValue: String) {
        val newFlagOption = EditFlagOption.getBooleanValue(newValue)
        todo.value = todo.value.copy(flag = newFlagOption)
    }

    fun onPriorityChange(newValue: String) {
        todo.value = todo.value.copy(priority = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        launchCatching {
            val editedTodo = todo.value
            if (editedTodo.id.isBlank()) {
                storageService.save(editedTodo)
            } else {
                storageService.update(editedTodo)
            }
            popUpScreen()
        }
    }

    private fun Int.toClockPattern(): String {
        return if (this < 10) "0$this" else "$this"
    }

    companion object {
        private const val UTC = "UTC"
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
    }
}