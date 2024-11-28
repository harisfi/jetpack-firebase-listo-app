package com.harisfi.listo

import android.content.Context
import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.cloudinary.android.MediaManager
import com.harisfi.listo.commons.snackbar.SnackbarManager
import com.harisfi.listo.commons.snackbar.SnackbarMessage.Companion.toMessage
import com.harisfi.listo.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class ListoAppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
    context: Context
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                snackbarHostState.showSnackbar(text)
                snackbarManager.clearSnackbarState()
            }
        }

        val config = mutableMapOf<String, Any>()
        config["cloud_name"] = Helper.getConfigValue(context, "cloud_name")!!
        config["api_key"] = Helper.getConfigValue(context, "api_key")!!
        config["api_secret"] = Helper.getConfigValue(context, "api_secret")!!
        MediaManager.init(context, config)
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}
