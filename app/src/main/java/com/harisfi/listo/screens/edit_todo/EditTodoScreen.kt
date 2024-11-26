package com.harisfi.listo.screens.edit_todo

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.harisfi.listo.commons.composable.BasicButton
import com.harisfi.listo.commons.composable.BasicField
import com.harisfi.listo.commons.composable.Textarea
import com.harisfi.listo.commons.ext.basicButton
import com.harisfi.listo.commons.ext.fieldModifier
import com.harisfi.listo.models.Todo
import com.harisfi.listo.screens.SharedViewModel
import com.harisfi.listo.ui.theme.ListoTheme
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText

@Composable
fun EditTodoScreen(
    popUpScreen: () -> Unit,
    openScreen: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: EditTodoViewModel = hiltViewModel(),
) {
    val todo by viewModel.todo
    val imageUrl by sharedViewModel.imageUrl

    EditTodoScreenContent(
        todo = todo,
        imageUrl = imageUrl,
        onDoneClick = { viewModel.onDoneClick(popUpScreen) },
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onOpenCameraClick = { viewModel.onOpenCameraClick(openScreen) }
    )
}

@Composable
fun EditTodoScreenContent(
    modifier: Modifier = Modifier,
    todo: Todo,
    imageUrl: String,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onOpenCameraClick: () -> Unit
) {
    val imageLoader = LocalContext.current.imageLoader.newBuilder()
        .logger(DebugLogger())
        .build()

    val context = LocalContext.current

    // Define the launcher within the Composable
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onOpenCameraClick()
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .padding(12.dp, 0.dp, 12.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(AppText.edit_todo),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 32.sp
            )

            FilledTonalIconButton(onClick = { onDoneClick() }) {
                Icon(
                    painter = painterResource(AppIcon.ic_check),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Settings"
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        val fieldModifier = Modifier.fieldModifier()
        BasicField(AppText.title, todo.title, onTitleChange, fieldModifier)
        Textarea(AppText.description, todo.description, onDescriptionChange, fieldModifier)

        BasicButton(AppText.open_camera, Modifier.basicButton()) {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data("https://res.cloudinary.com/dsuxc1c2p/image/upload/v1732444905/awmpxcyz4py81_uu6ont.jpg")
//                .crossfade(true)
//                .build(),
//            contentDescription = "Todo Image",
//            placeholder = painterResource(AppIcon.ic_image),
//            error = painterResource(AppIcon.ic_broken_image),
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .clip(RectangleShape)
//                .fillMaxHeight()
//                .fillMaxWidth()
//                .padding(12.dp, 0.dp, 12.dp, 0.dp),
//            imageLoader = imageLoader
//        )

        if (todo.imgUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(todo.imgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Todo Image",
                placeholder = painterResource(AppIcon.ic_image),
                error = painterResource(AppIcon.ic_broken_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RectangleShape)
                    .height(128.dp)
                    .padding(12.dp, 0.dp, 0.dp, 0.dp),
                imageLoader = imageLoader
            )
        }

        if (imageUrl.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(12.dp).height(64.dp).width(64.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
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
}

@Preview(showBackground = true)
@Composable
fun EditTodoScreenPreview() {
    val todo = Todo(
        title = "Todo title",
        description = "Todo description"
    )

    ListoTheme {
        EditTodoScreenContent(
            todo = todo,
            imageUrl = "url",
            onDoneClick = { },
            onTitleChange = { },
            onDescriptionChange = { },
            onOpenCameraClick = { }
        )
    }
}