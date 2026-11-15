package fr.isep.campusauth.data

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

const val STUDENT_DOMAIN = "@eleve.isep.fr"

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser) } // fires on sign in and sign out
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) } // remove when collection stops
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> = safeCall {
        if (!email.endsWith(STUDENT_DOMAIN, ignoreCase = true)) throw AuthError.NotStudentEmail // no firebase call
        auth.createUserWithEmailAndPassword(email, password).await() // also signs the user in
        currentUser().sendEmailVerification().await()
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> = safeCall {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendVerificationEmail(): Result<Unit> = safeCall {
        currentUser().sendEmailVerification().await()
    }

    override suspend fun reloadUser(): Result<Boolean> =
        safeCall { currentUser().reload().await() } // fetch fresh user data from server
            .map { auth.currentUser?.isEmailVerified == true }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = safeCall {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun updateDisplayName(name: String): Result<Unit> = safeCall {
        currentUser().updateProfile(userProfileChangeRequest { displayName = name }).await()
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> = safeCall {
        reauthenticate(currentPassword) // sensitive action needs a recent login
        currentUser().updatePassword(newPassword).await()
    }

    override suspend fun deleteAccount(currentPassword: String): Result<Unit> = safeCall {
        reauthenticate(currentPassword)
        currentUser().delete().await() // auth state becomes null
    }

    override fun signOut() = auth.signOut()

    private suspend fun reauthenticate(password: String) {
        val user = currentUser()
        val credential = EmailAuthProvider.getCredential(user.email.orEmpty(), password)
        user.reauthenticate(credential).await()
    }

    private fun currentUser(): FirebaseUser = auth.currentUser ?: throw AuthError.Unknown

    // runs a firebase call and wraps errors in Result
    private suspend fun safeCall(block: suspend () -> Unit): Result<Unit> = try {
        block()
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e // keep coroutine cancellation working
    } catch (e: Exception) {
        Result.failure(e.toAuthError())
    }
}
