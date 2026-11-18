package fr.isep.campusauth.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.isep.campusauth.R
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
    AuthScreen(title = stringResource(R.string.verify_title)) {
        Text(stringResource(R.string.verify_sent_to, state.user?.email.orEmpty()))
        Text(stringResource(R.string.verify_instructions))
        LoadingButton(text = stringResource(R.string.verify_done), isLoading = state.isLoading, onClick = onReload)
        OutlinedButton(onClick = onResend, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.verify_resend))
        }
        TextButton(onClick = onSignOut) { Text(stringResource(R.string.sign_out)) }
        Messages(state.error, state.info)
    }
}
