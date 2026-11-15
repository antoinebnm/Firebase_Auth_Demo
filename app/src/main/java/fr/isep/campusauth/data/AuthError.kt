package fr.isep.campusauth.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

sealed class AuthError(override val message: String) : Exception(message) {
    object NotStudentEmail : AuthError("Seules les adresses @eleve.isep.fr sont acceptées")
    object InvalidCredentials : AuthError("Email ou mot de passe incorrect")
    object EmailAlreadyUsed : AuthError("Un compte existe déjà avec cet email")
    object WeakPassword : AuthError("Mot de passe trop faible (6 caractères minimum)")
    object RecentLoginRequired : AuthError("Reconnecte-toi puis réessaie")
    object Network : AuthError("Pas de connexion internet")
    object Unknown : AuthError("Une erreur est survenue")
}

// firebase exception to app error
fun Throwable.toAuthError(): AuthError = when (this) {
    is AuthError -> this
    is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword // before invalid credentials (subclass)
    is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
    is FirebaseAuthInvalidUserException -> AuthError.InvalidCredentials
    is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyUsed
    is FirebaseAuthRecentLoginRequiredException -> AuthError.RecentLoginRequired
    is FirebaseNetworkException -> AuthError.Network
    else -> AuthError.Unknown
}
