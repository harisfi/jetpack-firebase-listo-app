package com.harisfi.listo.screens.edit_todo

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import com.harisfi.listo.CAMERA_SCREEN
import com.harisfi.listo.R
import com.harisfi.listo.TODO_ID
import com.harisfi.listo.commons.ext.idFromParameter
import com.harisfi.listo.commons.snackbar.SnackbarManager
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel()
class EditTodoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageService: StorageService,
) : ListoViewModel() {
    val todo = mutableStateOf(Todo())
    val imageFile = mutableStateOf<File?>(null)

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

    fun onImageChange(newValue: File) {
        imageFile.value = newValue
    }

    suspend fun onDoneClick(popUpScreen: () -> Unit, context: Context) {
        var editedTodo = todo.value

        if (editedTodo.title.isBlank()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (imageFile.value != null) {
            try {
                val url = storageService.uploadImage(context, imageFile.value!!.toUri())
                editedTodo = Todo(
                    id = editedTodo.id,
                    createdAt = editedTodo.createdAt,
                    title = editedTodo.title,
                    description = editedTodo.description,
                    completed = editedTodo.completed,
                    userId = editedTodo.userId,
                    imgUrl = url
                )
            } catch (e: Error) {
                SnackbarManager.showMessage(R.string.file_error)
                return
            }
        }

        launchCatching {
            if (editedTodo.id.isBlank()) {
                storageService.save(editedTodo)
            } else {
                storageService.update(editedTodo)
            }
            popUpScreen()
        }
    }

    fun onOpenCameraClick(
        openScreen: (String) -> Unit
    ) {
        openScreen(CAMERA_SCREEN)
    }
}