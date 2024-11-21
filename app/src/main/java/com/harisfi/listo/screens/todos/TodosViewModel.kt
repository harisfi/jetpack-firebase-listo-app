package com.harisfi.listo.screens.todos

import androidx.compose.runtime.mutableStateOf
import com.harisfi.listo.EDIT_TODO_SCREEN
import com.harisfi.listo.SETTINGS_SCREEN
import com.harisfi.listo.TODO_ID
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.ConfigurationService
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : ListoViewModel() {
    val options = mutableStateOf<List<String>>(listOf())

    val todos = storageService.todos

    fun loadTodoOptions() {
        val hasEditOption = configurationService.isShowTodoEditButtonConfig
        options.value = TodoActionOption.getOptions(hasEditOption)
    }

    fun onTodoCheckChange(todo: Todo) {
        launchCatching { storageService.update(todo.copy(completed = !todo.completed)) }
    }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_TODO_SCREEN)

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onTodoActionClick(openScreen: (String) -> Unit, todo: Todo, action: String) {
        when (TodoActionOption.getByTitle(action)) {
            TodoActionOption.EditTodo -> openScreen("$EDIT_TODO_SCREEN?$TODO_ID={${todo.id}}")
            TodoActionOption.ToggleFlag -> onFlagTodoClick(todo)
            TodoActionOption.DeleteTodo -> onDeleteTodoClick(todo)
        }
    }

    private fun onFlagTodoClick(todo: Todo) {
        launchCatching { storageService.update(todo.copy(flag = !todo.flag)) }
    }

    private fun onDeleteTodoClick(todo: Todo) {
        launchCatching { storageService.delete(todo.id) }
    }
}