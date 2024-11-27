package com.harisfi.listo.screens

import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ListoViewModel() {
    val imageFile = mutableStateOf<File?>(null)

    fun setCapturedImageFile(file: File?) {
        imageFile.value = file
    }
}