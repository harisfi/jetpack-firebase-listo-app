package com.harisfi.listo.screens.edit_todo

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
@ExperimentalMaterialApi
fun EditTodoScreen(
    popUpScreen: () -> Unit,
    viewModel: EditTodoViewModel = hiltViewModel()
) {}