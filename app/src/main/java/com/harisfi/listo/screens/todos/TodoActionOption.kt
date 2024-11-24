package com.harisfi.listo.screens.todos

enum class TodoActionOption(val title: String) {
    EditTodo("Edit todo"),
    DeleteTodo("Delete todo");

    companion object {
        fun getByTitle(title: String): TodoActionOption {
            values().forEach { action -> if (title == action.title) return action }

            return EditTodo
        }
    }
}