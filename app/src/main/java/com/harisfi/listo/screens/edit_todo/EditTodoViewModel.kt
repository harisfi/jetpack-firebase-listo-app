package com.harisfi.listo.screens.edit_todo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.harisfi.listo.TODO_ID
import com.harisfi.listo.commons.ext.idFromParameter
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
}