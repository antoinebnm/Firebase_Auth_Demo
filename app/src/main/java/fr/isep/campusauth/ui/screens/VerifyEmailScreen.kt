package fr.isep.campusauth.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.isep.campusauth.ui.AuthUiState
import fr.isep.campusauth.ui.components.AuthScreen
import fr.isep.campusauth.ui.components.LoadingButton
import fr.isep.campusauth.ui.components.Messages

@Composable
fun VerifyEmailScreen(
    state: AuthUiState,
    onReload: () -> Unit,
    onResend: () -> Unit,
    onSignOut: () -> Unit
) {
    AuthScreen(title = "Vérifie ton email") {
        Text("Un lien de vérification a été envoyé à ${state.user?.email}.")
        Text("Clique sur le lien dans l'email puis reviens ici.")
        LoadingButton(text = "J'ai vérifié", isLoading = state.isLoading, onClick = onReload)
        OutlinedButton(onClick = onResend, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text("Renvoyer l'email")
        }
        TextButton(onClick = onSignOut) { Text("Se déconnecter") }
        Messages(state.error, state.info)
    }
}
