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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

const val STUDENT_DOMAIN = "@eleve.isep.fr"

class FirebaseAuthRepository : AuthRepository {

    private val auth: FirebaseAuth by lazy { Firebase.auth } // created on first use

    // TODO step 1 emit the current user with callbackFlow + AuthStateListener and remove it in awaitClose
    override val authState: Flow<FirebaseUser?> = flowOf(null)

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        // TODO step 2 reject emails not ending with STUDENT_DOMAIN before calling firebase
        // TODO step 2 create the user with createUserWithEmailAndPassword then sendEmailVerification
        return notImplemented(2)
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        // TODO step 3 sign in with signInWithEmailAndPassword inside safeCall
        return notImplemented(3)
    }

    override suspend fun sendVerificationEmail(): Result<Unit> = safeCall {
        currentUser().sendEmailVerification().await()
    }

    override suspend fun reloadUser(): Result<Boolean> {
        // TODO step 4 reload the current user then return auth.currentUser?.isEmailVerified
        return notImplemented(4)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        // TODO step 5 send a reset email with sendPasswordResetEmail
        return notImplemented(5)
    }

    override suspend fun updateDisplayName(name: String): Result<Unit> {
        // TODO step 5 update the profile with userProfileChangeRequest { displayName = name }
        return notImplemented(5)
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        // TODO step 5 reauthenticate then updatePassword
        return notImplemented(5)
    }

    override suspend fun deleteAccount(currentPassword: String): Result<Unit> {
        // TODO step 5 reauthenticate then delete the user
        return notImplemented(5)
    }

    override fun signOut() {
        // TODO step 5 sign out from firebase
    }

    private suspend fun reauthenticate(password: String) {
        // TODO step 5 build a credential with EmailAuthProvider.getCredential(email, password) and reauthenticate
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

    // placeholder shown in the ui until the step is done
    @Suppress("UNUSED_PARAMETER")
    private fun <T> notImplemented(step: Int): Result<T> = Result.failure(AuthError.NotImplemented)
}
