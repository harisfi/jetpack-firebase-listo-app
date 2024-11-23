package com.harisfi.listo.models.services.impl

import com.harisfi.listo.BuildConfig
import com.harisfi.listo.R.xml as AppConfig
import com.harisfi.listo.models.services.ConfigurationService
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class ConfigurationServiceImpl @Inject constructor() : ConfigurationService {
    private val remoteConfig
        get() = Firebase.remoteConfig

    init {
        if (BuildConfig.DEBUG) {
            val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
            remoteConfig.setConfigSettingsAsync(configSettings)
        }

        remoteConfig.setDefaultsAsync(AppConfig.remote_config_defaults)
    }

    override suspend fun fetchConfiguration(): Boolean = remoteConfig.fetchAndActivate().await()

    override val isShowTodoEditButtonConfig: Boolean
        get() = remoteConfig[SHOW_TODO_EDIT_BUTTON_KEY].asBoolean()

    companion object {
        private const val SHOW_TODO_EDIT_BUTTON_KEY = "show_todo_edit_button"
        private const val FETCH_CONFIG_TRACE = "fetchConfig"
    }
}