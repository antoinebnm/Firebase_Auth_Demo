package fr.isep.campusauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import fr.isep.campusauth.data.FirebaseAuthRepository
import fr.isep.campusauth.ui.AuthViewModel
import fr.isep.campusauth.ui.screens.ProfileScreen
import fr.isep.campusauth.ui.screens.SignInScreen
import fr.isep.campusauth.ui.screens.SignUpScreen
import fr.isep.campusauth.ui.screens.VerifyEmailScreen

class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels {
        viewModelFactory { initializer { AuthViewModel(FirebaseAuthRepository()) } } // manual injection
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { padding ->
                    Box(Modifier.padding(padding).imePadding()) {
                        CampusAuthApp(viewModel)
                    }
                }
            }
        }
    }
}

// screen chosen from auth state no navigation library
@Composable
private fun CampusAuthApp(viewModel: AuthViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showSignUp by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.user) { if (state.user != null) showSignUp = false } // back to sign in after sign out

    when {
        state.user == null && showSignUp -> SignUpScreen(
            state = state,
            onSignUp = viewModel::signUp,
            onGoToSignIn = { showSignUp = false; viewModel.clearMessages() }
        )
        state.user == null -> SignInScreen(
            state = state,
            onSignIn = viewModel::signIn,
            onForgotPassword = viewModel::sendPasswordReset,
            onGoToSignUp = { showSignUp = true; viewModel.clearMessages() }
        )
        !state.isEmailVerified -> VerifyEmailScreen(
            state = state,
            onReload = viewModel::reloadUser,
            onResend = viewModel::resendVerificationEmail,
            onSignOut = viewModel::signOut
        )
        else -> ProfileScreen(
            state = state,
            onUpdateName = viewModel::updateDisplayName,
            onChangePassword = viewModel::changePassword,
            onDeleteAccount = viewModel::deleteAccount,
            onSignOut = viewModel::signOut
        )
    }
}
