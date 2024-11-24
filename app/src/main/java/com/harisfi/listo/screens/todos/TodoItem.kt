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
import com.harisfi.listo.models.Todo

@Composable
fun TodoItem(
    todo: Todo,
    onCheckChange: () -> Unit,
    onActionClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(12.dp),
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = { onCheckChange() },
                modifier = Modifier.padding(8.dp, 0.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyMedium
                )

                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) { // Adjust alpha as needed
                    Text(
                        text = if (todo.description.length > 64) todo.description.take(64) + "..." else todo.description,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp
                    )
                }
            }

            IconButton(
                onClick = { onActionClick("Edit todo") }
            ) {
                Icon(
                    painter = painterResource(AppIcon.ic_edit),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Edit"
                )
            }

            IconButton(
                onClick = { onActionClick("Delete todo") }
            ) {
                Icon(
                    painter = painterResource(AppIcon.ic_delete),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Flag"
                )
            }
        }
    }
}
