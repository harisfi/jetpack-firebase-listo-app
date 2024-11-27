package com.harisfi.listo.models.services

import android.content.Context
import android.net.Uri
import com.harisfi.listo.models.Todo
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val todos: Flow<List<Todo>>
    suspend fun getTodo(todoId: String): Todo?
    suspend fun save(todo: Todo): String
    suspend fun update(todo: Todo)
    suspend fun delete(todoId: String)
    suspend fun getCompletedTodosCount(): Int
    suspend fun uploadImage(context: Context, uri: Uri): String
}