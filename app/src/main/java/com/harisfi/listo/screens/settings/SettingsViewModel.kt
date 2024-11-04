package com.harisfi.listo.screens.settings

import com.harisfi.listo.LOGIN_SCREEN
import com.harisfi.listo.REGISTER_SCREEN
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService
) : ListoViewModel() {
    val uiState = accountService.currentUser.map { SettingsUiState() }

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(REGISTER_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(LOGIN_SCREEN)
        }
    }
}