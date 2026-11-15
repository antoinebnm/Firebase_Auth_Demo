package fr.isep.campusauth.data

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<FirebaseUser?> // null when signed out

    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun sendVerificationEmail(): Result<Unit>
    suspend fun reloadUser(): Result<Boolean> // true if email verified
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun updateDisplayName(name: String): Result<Unit>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun deleteAccount(currentPassword: String): Result<Unit>
    fun signOut()
}
