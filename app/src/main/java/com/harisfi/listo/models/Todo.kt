package com.harisfi.listo.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Todo(
    @DocumentId val id: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    val title: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val userId: String = ""
)