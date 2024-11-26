package com.harisfi.listo

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.harisfi.listo.commons.snackbar.SnackbarManager
import com.harisfi.listo.screens.SharedViewModel
import com.harisfi.listo.screens.camera.CameraScreen
import com.harisfi.listo.screens.edit_todo.EditTodoScreen
import com.harisfi.listo.screens.login.LoginScreen
import com.harisfi.listo.screens.register.RegisterScreen
import com.harisfi.listo.screens.settings.SettingsScreen
import com.harisfi.listo.screens.splash.SplashScreen
import com.harisfi.listo.screens.todos.TodosScreen
import com.harisfi.listo.ui.theme.ListoTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun ListoApp(
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    ListoTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    ListoGraph(appState, sharedViewModel)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        ListoAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun NavGraphBuilder.ListoGraph(appState: ListoAppState, sharedViewModel: SharedViewModel) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(REGISTER_SCREEN) {
        RegisterScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(TODOS_SCREEN) { TodosScreen(openScreen = { route -> appState.navigate(route) }) }

    composable(CAMERA_SCREEN) { CameraScreen(popUpScreen = { appState.popUp() }, sharedViewModel) }

    composable(
        route = "$EDIT_TODO_SCREEN$TODO_ID_ARG",
        arguments = listOf(navArgument(TODO_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        EditTodoScreen(
            popUpScreen = { appState.popUp() },
            openScreen = { route -> appState.navigate(route) },
            sharedViewModel
        )
    }
}