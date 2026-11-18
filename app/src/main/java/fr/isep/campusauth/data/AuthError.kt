package fr.isep.campusauth.data

import androidx.annotation.StringRes
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import fr.isep.campusauth.R

sealed class AuthError(@StringRes val messageRes: Int) : Exception() {
    object NotStudentEmail : AuthError(R.string.error_not_student_email)
    object InvalidCredentials : AuthError(R.string.error_invalid_credentials)
    object EmailAlreadyUsed : AuthError(R.string.error_email_already_used)
    object WeakPassword : AuthError(R.string.error_weak_password)
    object RecentLoginRequired : AuthError(R.string.error_recent_login_required)
    object Network : AuthError(R.string.error_network)
    object Unknown : AuthError(R.string.error_unknown)
    object NotImplemented : AuthError(R.string.error_not_implemented) // starter placeholder
}

// firebase exception to app error
fun Throwable.toAuthError(): AuthError = when (this) {
    is AuthError -> this
    // TODO step 3 map each FirebaseAuth exception to its AuthError (weak password first, it extends invalid credentials)
    else -> AuthError.Unknown
}
