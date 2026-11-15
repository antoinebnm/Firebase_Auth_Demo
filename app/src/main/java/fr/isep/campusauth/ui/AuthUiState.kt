package fr.isep.campusauth.ui

import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val user: FirebaseUser? = null,
    val isEmailVerified: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val info: String? = null // success message
)
