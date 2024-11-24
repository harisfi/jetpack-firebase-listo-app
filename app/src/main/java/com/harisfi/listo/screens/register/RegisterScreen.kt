package com.harisfi.listo.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harisfi.listo.R
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.commons.composable.*
import com.harisfi.listo.commons.ext.basicButton
import com.harisfi.listo.commons.ext.fieldModifier
import com.harisfi.listo.ui.theme.ListoTheme

@Composable
fun RegisterScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    RegisterScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onRegisterClick = { viewModel.onRegisterClick(openAndPopUp) }
    )
}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val fieldModifier = Modifier.fieldModifier()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fieldModifier(),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_asterisk),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Icon",
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(AppText.create_account),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp
            )

            Text(
                text = stringResource(AppText.account_details),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(32.dp))
        }

        EmailField(uiState.email, onEmailChange, fieldModifier)
        PasswordField(uiState.password, onPasswordChange, fieldModifier)
        RepeatPasswordField(uiState.repeatPassword, onRepeatPasswordChange, fieldModifier)

        BasicButton(AppText.create_account, Modifier.basicButton()) {
            onRegisterClick()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val uiState = RegisterUiState(
        email = "email@test.com"
    )

    ListoTheme {
        RegisterScreenContent(
            uiState = uiState,
            onEmailChange = { },
            onPasswordChange = { },
            onRepeatPasswordChange = { },
            onRegisterClick = { }
        )
    }
}