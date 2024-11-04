package com.harisfi.listo.models.services

import com.harisfi.listo.models.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun signOut()
}