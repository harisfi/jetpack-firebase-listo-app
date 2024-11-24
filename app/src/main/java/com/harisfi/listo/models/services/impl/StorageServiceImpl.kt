package com.harisfi.listo.models.services.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.models.services.StorageService
import com.google.firebase.firestore.AggregateSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {

    private val collection get() = firestore.collection(TODO_COLLECTION)
        .whereEqualTo(USER_ID_FIELD, auth.currentUserId)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val todos: Flow<List<Todo>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore
                    .collection(TODO_COLLECTION)
                    .whereEqualTo(USER_ID_FIELD, user.id)
                    .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                    .dataObjects()
            }

    override suspend fun getTodo(todoId: String): Todo? =
        firestore.collection(TODO_COLLECTION).document(todoId).get().await().toObject()

    override suspend fun save(todo: Todo): String
        {
            val updatedTask = todo.copy(userId = auth.currentUserId)
            return firestore.collection(TODO_COLLECTION).add(updatedTask).await().id
        }

    override suspend fun update(todo: Todo) {
        firestore.collection(TODO_COLLECTION).document(todo.id).set(todo).await()
    }

    override suspend fun delete(todoId: String) {
        firestore.collection(TODO_COLLECTION).document(todoId).delete().await()
    }

    override suspend fun getCompletedTodosCount(): Int {
        val query = collection.whereEqualTo(COMPLETED_FIELD, true).count()
        return query.get(AggregateSource.SERVER).await().count.toInt()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COMPLETED_FIELD = "completed"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val TODO_COLLECTION = "todos"
    }
}