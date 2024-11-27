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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch
import java.io.File
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText

@Composable
fun EditTodoScreen(
    popUpScreen: () -> Unit,
    openScreen: (String) -> Unit,
    sharedViewModel: SharedViewModel,
    viewModel: EditTodoViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val todo by viewModel.todo
    val imageFile by sharedViewModel.imageFile
    val context = LocalContext.current

    val onDoneClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.onDoneClick(popUpScreen, context)
            sharedViewModel.setCapturedImageFile(null)
        }
    }

    EditTodoScreenContent(
        todo = todo,
        imageFile = imageFile,
        onDoneClick = onDoneClick,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onOpenCameraClick = { viewModel.onOpenCameraClick(openScreen) },
        onImageChange = viewModel::onImageChange
    )
}

@Composable
fun EditTodoScreenContent(
    modifier: Modifier = Modifier,
    todo: Todo,
    imageFile: File?,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onOpenCameraClick: () -> Unit,
    onImageChange: (File) -> Unit
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

        if (imageFile != null) {
            onImageChange(imageFile)
        }

        if (imageFile != null || todo.imgUrl.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).height(64.dp).width(64.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageFile?.toURI()?.toString() ?: todo.imgUrl)
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

                Column {
                    BasicButton(AppText.change_picture, Modifier.basicButton()) {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                    BasicButton(AppText.remove_picture, Modifier.basicButton()) {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }
            }
        } else {
            BasicButton(AppText.add_picture, Modifier.basicButton()) {
                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
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
            imageFile = null,
            onDoneClick = { },
            onTitleChange = { },
            onDescriptionChange = { },
            onOpenCameraClick = { },
            onImageChange = { }
        )
    }
}