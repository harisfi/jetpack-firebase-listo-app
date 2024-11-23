package com.harisfi.listo.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuthException
import com.harisfi.listo.SPLASH_SCREEN
import com.harisfi.listo.TODOS_SCREEN
import com.harisfi.listo.models.services.AccountService
import com.harisfi.listo.models.services.ConfigurationService
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService
) : ListoViewModel() {
    val showError = mutableStateOf(false)

    init {
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser) openAndPopUp(TODOS_SCREEN, SPLASH_SCREEN)
        else createAnonymousAccount(openAndPopUp)
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching(snackbar = false) {
            try {
                accountService.createAnonymousAccount()
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            openAndPopUp(TODOS_SCREEN, SPLASH_SCREEN)
        }
    }
}