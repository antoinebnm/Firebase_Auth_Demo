package fr.isep.campusauth.ui.screens

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import fr.isep.campusauth.R
import fr.isep.campusauth.ui.AuthUiState
import fr.isep.campusauth.ui.components.AuthScreen
import fr.isep.campusauth.ui.components.EmailField
import fr.isep.campusauth.ui.components.LoadingButton
import fr.isep.campusauth.ui.components.Messages
import fr.isep.campusauth.ui.components.PasswordField

@Composable
fun SignInScreen(
    state: AuthUiState,
    onSignIn: (email: String, password: String) -> Unit,
    onForgotPassword: (email: String) -> Unit,
    onGoToSignUp: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    AuthScreen(title = stringResource(R.string.sign_in_title)) {
        EmailField(email) { email = it }
        PasswordField(password, { password = it })
        LoadingButton(
            text = stringResource(R.string.sign_in_button),
            isLoading = state.isLoading,
            enabled = email.isNotBlank() && password.isNotBlank(),
            onClick = { onSignIn(email, password) }
        )
        TextButton(onClick = { onForgotPassword(email) }, enabled = email.isNotBlank()) {
            Text(stringResource(R.string.forgot_password))
        }
        TextButton(onClick = onGoToSignUp) { Text(stringResource(R.string.go_to_sign_up)) }
        Messages(state.error, state.info)
    }
}
