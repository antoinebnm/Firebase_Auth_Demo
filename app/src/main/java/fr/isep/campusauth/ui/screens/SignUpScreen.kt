package fr.isep.campusauth.ui.screens

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import fr.isep.campusauth.data.STUDENT_DOMAIN
import fr.isep.campusauth.ui.AuthUiState
import fr.isep.campusauth.ui.components.AuthScreen
import fr.isep.campusauth.ui.components.EmailField
import fr.isep.campusauth.ui.components.LoadingButton
import fr.isep.campusauth.ui.components.Messages
import fr.isep.campusauth.ui.components.PasswordField

@Composable
fun SignUpScreen(
    state: AuthUiState,
    onSignUp: (email: String, password: String) -> Unit,
    onGoToSignIn: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    AuthScreen(title = "Créer un compte") {
        Text("Réservé aux adresses $STUDENT_DOMAIN")
        EmailField(email) { email = it }
        PasswordField(password, { password = it })
        LoadingButton(
            text = "Créer mon compte",
            isLoading = state.isLoading,
            enabled = email.isNotBlank() && password.isNotBlank(),
            onClick = { onSignUp(email, password) }
        )
        TextButton(onClick = onGoToSignIn) { Text("Déjà un compte ? Se connecter") }
        Messages(state.error, state.info)
    }
}
