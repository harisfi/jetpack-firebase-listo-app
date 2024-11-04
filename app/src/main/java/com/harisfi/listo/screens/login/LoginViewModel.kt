package com.harisfi.listo.screens.login

import androidx.compose.runtime.mutableStateOf
import com.harisfi.listo.LOGIN_SCREEN
import com.harisfi.listo.SETTINGS_SCREEN
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.commons.ext.isValidEmail
import com.harisfi.listo.commons.snackbar.SnackbarManager
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService
) : ListoViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(SETTINGS_SCREEN, LOGIN_SCREEN)
        }
    }
}