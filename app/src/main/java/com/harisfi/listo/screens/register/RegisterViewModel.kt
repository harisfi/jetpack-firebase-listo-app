package com.harisfi.listo.screens.register

import androidx.compose.runtime.mutableStateOf
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.REGISTER_SCREEN
import com.harisfi.listo.SETTINGS_SCREEN
import com.harisfi.listo.commons.ext.isValidEmail
import com.harisfi.listo.commons.ext.isValidPassword
import com.harisfi.listo.commons.ext.passwordMatches
import com.harisfi.listo.commons.snackbar.SnackbarManager
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountService: AccountService,
) : ListoViewModel() {
    var uiState = mutableStateOf(RegisterUiState())
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

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onRegisterClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

        launchCatching {
            accountService.linkAccount(email, password)
            openAndPopUp(SETTINGS_SCREEN, REGISTER_SCREEN)
        }
    }
}
