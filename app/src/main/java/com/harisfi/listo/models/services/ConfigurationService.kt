package com.harisfi.listo.models.services

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean
    val isShowTodoEditButtonConfig: Boolean
}