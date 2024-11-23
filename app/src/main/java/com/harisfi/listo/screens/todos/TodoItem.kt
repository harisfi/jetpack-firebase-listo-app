package com.harisfi.listo.screens.todos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.commons.composable.DropdownContextMenu
import com.harisfi.listo.commons.ext.contextMenu
import com.harisfi.listo.commons.ext.hasDueDate
import com.harisfi.listo.commons.ext.hasDueTime
import com.harisfi.listo.models.Todo
import com.harisfi.listo.ui.theme.DarkOrange
import java.lang.StringBuilder

@Composable
fun TodoItem(
    todo: Todo,
    options: List<String>,
    onCheckChange: () -> Unit,
    onActionClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = { onCheckChange() },
                modifier = Modifier.padding(8.dp, 0.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium
                )

                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) { // Adjust alpha as needed
                    Text(
                        text = getDueDateAndTime(todo),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp
                    )
                }
            }

            if (todo.flag) {
                Icon(
                    painter = painterResource(AppIcon.ic_flag),
                    tint = DarkOrange,
                    contentDescription = "Flag"
                )
            }

            DropdownContextMenu(options, Modifier.contextMenu(), onActionClick)
        }
    }
}

private fun getDueDateAndTime(todo: Todo): String {
    val stringBuilder = StringBuilder("")

    if (todo.hasDueDate()) {
        stringBuilder.append(todo.dueDate)
        stringBuilder.append(" ")
    }

    if (todo.hasDueTime()) {
        stringBuilder.append("at ")
        stringBuilder.append(todo.dueTime)
    }

    return stringBuilder.toString()
}