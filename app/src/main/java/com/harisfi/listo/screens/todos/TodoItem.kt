package com.harisfi.listo.screens.todos

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.harisfi.listo.models.Todo
import com.harisfi.listo.R.drawable as AppIcon

@Composable
fun TodoItem(
    todo: Todo,
    onCheckChange: () -> Unit,
    onActionClick: (String) -> Unit
) {
    val imageLoader = LocalContext.current.imageLoader.newBuilder()
        .logger(DebugLogger())
        .build()

    Card(
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = { onCheckChange() },
                modifier = Modifier.padding(8.dp, 0.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text =
                    if (todo.title.length > 64) todo.title.take(32) + "..."
                    else todo.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration =
                    if (todo.completed) TextDecoration.LineThrough
                    else TextDecoration.None
                )

                if (todo.description.isNotEmpty()) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    ) { // Adjust alpha as needed
                        Text(
                            text =
                            if (todo.description.length > 64) todo.description.take(64) + "..."
                            else todo.description,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            textDecoration =
                            if (todo.completed) TextDecoration.LineThrough
                            else TextDecoration.None
                        )
                    }
                }

                if (todo.imgUrl.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(0.dp, 12.dp, 0.dp, 0.dp)
                            .height(64.dp)
                            .width(64.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(todo.imgUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Todo Image",
                            placeholder = painterResource(AppIcon.ic_image),
                            error = painterResource(AppIcon.ic_broken_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            imageLoader = imageLoader
                        )
                    }
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
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Flag"
                )
            }
        }
    }
}
