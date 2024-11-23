package com.harisfi.listo.models.services.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.harisfi.listo.models.Priority
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.models.services.StorageService
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
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

    override suspend fun update(todo: Todo): Unit {

        firestore.collection(TODO_COLLECTION).document(todo.id).set(todo).await()
    }

    override suspend fun delete(todoId: String) {
        firestore.collection(TODO_COLLECTION).document(todoId).delete().await()
    }

    override suspend fun getCompletedTodosCount(): Int {
        val query = collection.whereEqualTo(COMPLETED_FIELD, true).count()
        return query.get(AggregateSource.SERVER).await().count.toInt()
    }

    override suspend fun getImportantCompletedTodosCount(): Int {
        val query = collection.where(
            Filter.and(
                Filter.equalTo(COMPLETED_FIELD, true),
                Filter.or(
                    Filter.equalTo(PRIORITY_FIELD, Priority.High.name),
                    Filter.equalTo(FLAG_FIELD, true)
                )
            )
        )

        return query.count().get(AggregateSource.SERVER).await().count.toInt()
    }

    override suspend fun getMediumHighTodosToCompleteCount(): Int {
        val query = collection
            .whereEqualTo(COMPLETED_FIELD, false)
            .whereIn(PRIORITY_FIELD, listOf(Priority.Medium.name, Priority.High.name)).count()

        return query.get(AggregateSource.SERVER).await().count.toInt()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COMPLETED_FIELD = "completed"
        private const val PRIORITY_FIELD = "priority"
        private const val FLAG_FIELD = "flag"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val TODO_COLLECTION = "todos"
    }
}