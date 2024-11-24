package com.harisfi.listo.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harisfi.listo.commons.composable.BasicButton
import com.harisfi.listo.commons.composable.BasicToolbar
import com.harisfi.listo.commons.composable.EmailField
import com.harisfi.listo.commons.composable.PasswordField
import com.harisfi.listo.commons.ext.basicButton
import com.harisfi.listo.commons.ext.fieldModifier
import com.harisfi.listo.ui.theme.ListoTheme
import com.harisfi.listo.R.string as AppText
import com.harisfi.listo.R.drawable as AppIcon

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignInClick = { viewModel.onSignInClick(openAndPopUp) }
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit
) {
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
                painter = painterResource(AppIcon.ic_asterisk),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Icon",
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(AppText.sign_in),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 32.sp
            )

            Text(
                text = stringResource(AppText.login_details),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(46.dp))
        }

        EmailField(uiState.email, onEmailChange, Modifier.fieldModifier())
        PasswordField(uiState.password, onPasswordChange, Modifier.fieldModifier())

        BasicButton(AppText.sign_in, Modifier.basicButton()) { onSignInClick() }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val uiState = LoginUiState(
        email = "email@test.com"
    )

    ListoTheme {
        LoginScreenContent(
            uiState = uiState,
            onEmailChange = { },
            onPasswordChange = { },
            onSignInClick = { }
        )
    }
}