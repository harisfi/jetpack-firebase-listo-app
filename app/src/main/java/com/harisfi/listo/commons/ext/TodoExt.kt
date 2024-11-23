package com.harisfi.listo.commons.ext

import com.google.android.gms.tasks.Task
import com.harisfi.listo.models.Todo

fun Todo?.hasDueDate(): Boolean {
    return this?.dueDate.orEmpty().isNotBlank()
}

fun Todo?.hasDueTime(): Boolean {
    return this?.dueTime.orEmpty().isNotBlank()
}