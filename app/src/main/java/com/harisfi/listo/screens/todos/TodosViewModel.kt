package com.harisfi.listo.screens.todos

import com.harisfi.listo.EDIT_TODO_SCREEN
import com.harisfi.listo.SETTINGS_SCREEN
import com.harisfi.listo.TODO_ID
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val storageService: StorageService,
) : ListoViewModel() {
    val todos = storageService.todos

    fun onTodoCheckChange(todo: Todo) {
        launchCatching { storageService.update(todo.copy(completed = !todo.completed)) }
    }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_TODO_SCREEN)

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onTodoActionClick(openScreen: (String) -> Unit, todo: Todo, action: String) {
        when (TodoActionOption.getByTitle(action)) {
            TodoActionOption.EditTodo -> openScreen("$EDIT_TODO_SCREEN?$TODO_ID={${todo.id}}")
            TodoActionOption.DeleteTodo -> onDeleteTodoClick(todo)
        }
    }

    private fun onDeleteTodoClick(todo: Todo) {
        launchCatching { storageService.delete(todo.id) }
    }
}
