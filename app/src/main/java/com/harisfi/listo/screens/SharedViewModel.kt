package com.harisfi.listo.screens

import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ListoViewModel() {
    val imageUrl = mutableStateOf("")

    fun setCapturedImageUrl(url: String) {
        imageUrl.value = url
    }
}