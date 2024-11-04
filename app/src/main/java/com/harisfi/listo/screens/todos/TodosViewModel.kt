package com.harisfi.listo.screens.todos

import com.harisfi.listo.SETTINGS_SCREEN
import com.harisfi.listo.screens.ListoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor() : ListoViewModel() {
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)
}