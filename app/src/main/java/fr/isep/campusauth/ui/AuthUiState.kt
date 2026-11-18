package fr.isep.campusauth.ui

import androidx.annotation.StringRes
import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val user: FirebaseUser? = null,
    val isEmailVerified: Boolean = false,
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null, // string resource id
    @StringRes val info: Int? = null // success message
)
