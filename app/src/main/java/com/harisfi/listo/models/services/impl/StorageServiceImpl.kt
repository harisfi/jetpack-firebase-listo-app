package com.harisfi.listo.models.services.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.harisfi.listo.models.Todo
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.models.services.StorageService
import com.harisfi.listo.utils.Helper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {

    private val collection
        get() = firestore.collection(TODO_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, auth.currentUserId)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val todos: Flow<List<Todo>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore
                    .collection(TODO_COLLECTION)
                    .whereEqualTo(USER_ID_FIELD, user.id)
                    .orderBy(COMPLETED_FIELD, Query.Direction.ASCENDING)
                    .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                    .dataObjects()
            }

    private val isMediaManagerInitialized = mutableStateOf(false)

    override suspend fun getTodo(todoId: String): Todo? =
        firestore.collection(TODO_COLLECTION).document(todoId).get().await().toObject()

    override suspend fun save(todo: Todo): String {
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

    override suspend fun uploadImage(context: Context, uri: Uri): String =
        suspendCancellableCoroutine { continuation ->
            val config = mutableMapOf<String, Any>()
            config["cloud_name"] = Helper.getConfigValue(context, "cloud_name")!!
            config["api_key"] = Helper.getConfigValue(context, "api_key")!!
            config["api_secret"] = Helper.getConfigValue(context, "api_secret")!!
            if (!isMediaManagerInitialized.value) {
                MediaManager.init(context, config)
                isMediaManagerInitialized.value = true
            }

            val callback = object : UploadCallback {
                override fun onStart(requestId: String) {
                    Log.d("Cloudinary Quickstart", "Upload start")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    Log.d("Cloudinary Quickstart", "Upload progress")
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as String
                    continuation.resume(url)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(Exception("Upload failed: ${error.description}"))
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            }

            MediaManager.get().upload(uri.path).unsigned(Helper.getConfigValue(context, "upload_preset")!!).callback(callback).dispatch()
        }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val COMPLETED_FIELD = "completed"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val TODO_COLLECTION = "todos"
    }
}