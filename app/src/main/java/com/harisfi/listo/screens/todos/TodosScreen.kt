package com.harisfi.listo.screens.todos

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FloatingActionButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harisfi.listo.commons.composable.ActionToolbar
import com.harisfi.listo.commons.ext.smallSpacer
import com.harisfi.listo.commons.ext.toolbarActions
import com.harisfi.listo.ui.theme.ListoTheme
import com.harisfi.listo.R.drawable as AppIcon
import com.harisfi.listo.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun TodosScreen(
    openScreen: (String) -> Unit,
    viewModel: TodosViewModel = hiltViewModel()
) {
    TodosScreenContent(
        onSettingsClick = viewModel::onSettingsClick,
        openScreen = openScreen
    )

    LaunchedEffect(viewModel) { }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TodosScreenContent(
    modifier: Modifier = Modifier,
    onSettingsClick: ((String) -> Unit) -> Unit,
    openScreen: (String) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            ActionToolbar(
                title = AppText.todos,
                modifier = Modifier.toolbarActions(),
                primaryActionIcon = AppIcon.ic_settings,
                primaryAction = { onSettingsClick(openScreen) }
            )

            Spacer(modifier = Modifier.smallSpacer())
        }
    }
}


@Preview(showBackground = true)
@ExperimentalMaterialApi
@Composable
fun TodosScreenPreview() {

    ListoTheme {
        TodosScreenContent(
            onSettingsClick = { },
            openScreen = { }
        )
    }
}