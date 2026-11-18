package fr.isep.campusauth.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.isep.campusauth.R
import fr.isep.campusauth.ui.AuthUiState
import fr.isep.campusauth.ui.components.AuthScreen
import fr.isep.campusauth.ui.components.LoadingButton
import fr.isep.campusauth.ui.components.Messages
import fr.isep.campusauth.ui.components.PasswordField

@Composable
fun ProfileScreen(
    state: AuthUiState,
    onUpdateName: (name: String) -> Unit,
    onChangePassword: (currentPassword: String, newPassword: String) -> Unit,
    onDeleteAccount: (currentPassword: String) -> Unit,
    onSignOut: () -> Unit
) {
    val user = state.user ?: return
    var displayName by rememberSaveable { mutableStateOf(user.displayName.orEmpty()) }
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var deletePassword by rememberSaveable { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var lastAction by remember { mutableStateOf("") } // which button shows the spinner

    LaunchedEffect(state.info) { // clear password fields after a success
        if (state.info != null) { currentPassword = ""; newPassword = "" }
    }

    AuthScreen(title = stringResource(R.string.profile_title)) {
        Text(stringResource(R.string.profile_email, user.email.orEmpty()))
        Text(stringResource(R.string.profile_uid, user.uid), style = MaterialTheme.typography.bodySmall)
        Messages(state.error, state.info)

        HorizontalDivider()
        Text(stringResource(R.string.display_name_section), style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text(stringResource(R.string.display_name_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        LoadingButton(
            text = stringResource(R.string.save),
            isLoading = state.isLoading && lastAction == "name",
            enabled = !state.isLoading && displayName.isNotBlank(),
            onClick = { lastAction = "name"; onUpdateName(displayName) }
        )

        HorizontalDivider()
        Text(stringResource(R.string.change_password_section), style = MaterialTheme.typography.titleMedium)
        PasswordField(currentPassword, { currentPassword = it }, label = stringResource(R.string.current_password))
        PasswordField(newPassword, { newPassword = it }, label = stringResource(R.string.new_password))
        LoadingButton(
            text = stringResource(R.string.change_password_section),
            isLoading = state.isLoading && lastAction == "password",
            enabled = !state.isLoading && currentPassword.isNotBlank() && newPassword.isNotBlank(),
            onClick = { lastAction = "password"; onChangePassword(currentPassword, newPassword) }
        )

        HorizontalDivider()
        Text(stringResource(R.string.delete_account_section), style = MaterialTheme.typography.titleMedium)
        PasswordField(deletePassword, { deletePassword = it }, label = stringResource(R.string.current_password))
        LoadingButton(
            text = stringResource(R.string.delete_account_button),
            isLoading = state.isLoading && lastAction == "delete",
            enabled = !state.isLoading && deletePassword.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            onClick = { showDeleteDialog = true } // confirm first
        )

        HorizontalDivider()
        OutlinedButton(onClick = onSignOut, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.sign_out))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_dialog_title)) },
            text = { Text(stringResource(R.string.delete_dialog_text)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    lastAction = "delete"
                    onDeleteAccount(deletePassword)
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}
