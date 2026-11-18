package fr.isep.campusauth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isep.campusauth.R
import fr.isep.campusauth.data.AuthError
import fr.isep.campusauth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.authState.collect { user ->
                _uiState.update { it.copy(user = user, isEmailVerified = user?.isEmailVerified == true) }
            }
        }
    }

    fun signUp(email: String, password: String) = launchAction(
        action = { repository.signUp(email.trim(), password) },
        onSuccess = { R.string.info_account_created }
    )

    fun signIn(email: String, password: String) = launchAction(
        action = { repository.signIn(email.trim(), password) }
    )

    fun sendPasswordReset(email: String) = launchAction(
        action = { repository.sendPasswordReset(email.trim()) },
        onSuccess = { R.string.info_reset_sent }
    )

    fun resendVerificationEmail() = launchAction(
        action = { repository.sendVerificationEmail() },
        onSuccess = { R.string.info_verification_resent }
    )

    fun reloadUser() = launchAction(
        action = { repository.reloadUser() },
        onSuccess = { verified ->
            _uiState.update { it.copy(isEmailVerified = verified) } // switches to profile if true
            if (verified) R.string.info_email_verified else R.string.info_email_not_verified
        }
    )

    fun updateDisplayName(name: String) = launchAction(
        action = { repository.updateDisplayName(name.trim()) },
        onSuccess = { R.string.info_name_updated }
    )

    fun changePassword(currentPassword: String, newPassword: String) = launchAction(
        action = { repository.changePassword(currentPassword, newPassword) },
        onSuccess = { R.string.info_password_changed }
    )

    fun deleteAccount(currentPassword: String) = launchAction(
        action = { repository.deleteAccount(currentPassword) },
        onSuccess = { R.string.info_account_deleted }
    )

    fun signOut() {
        repository.signOut()
        clearMessages()
    }

    fun clearMessages() = _uiState.update { it.copy(error = null, info = null) }

    // loading on then result to error or info
    private fun <T> launchAction(
        action: suspend () -> Result<T>,
        onSuccess: (T) -> Int? = { null } // info string resource
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, info = null) }
            action().fold(
                onSuccess = { value ->
                    val message = onSuccess(value)
                    _uiState.update { it.copy(isLoading = false, info = message) }
                },
                onFailure = { e ->
                    val message = (e as? AuthError ?: AuthError.Unknown).messageRes
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
            )
        }
    }
}
