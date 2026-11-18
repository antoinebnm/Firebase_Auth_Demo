# CampusAuth_FirebaseDemo: starter

Exercise from the Firebase Authentication talk. The UI, navigation, ViewModel, state and error classes are complete; you only write the **Firebase Auth** logic.

- Your code goes in `data/FirebaseAuthRepository.kt` and `data/AuthError.kt`, plus two Gradle lines for step 0
- Every gap is marked `// TODO step N`: open **View > Tool Windows > TODO** in Android Studio to see them grouped
- An unfinished step shows "Not implemented yet: see the TODO steps" under the form instead of crashing
- Stuck? Compare with the full version: `git diff starter solution -- app/src/main/java`

The app compiles and runs right after cloning; it just stays on the sign-in screen.

## Step 0: setup

1. In the [Firebase console](https://console.firebase.google.com): create a project, enable **Authentication > Sign-in method > Email/Password**
2. Add an Android app with package `fr.isep.campusauth`, download `google-services.json` into `app/` (already in `.gitignore`, never commit it)
3. Apply the `google-services` plugin in `build.gradle.kts` and `app/build.gradle.kts` (the alias already exists in `libs.versions.toml`)

✅ Gradle sync succeeds, the app launches on "Connexion"

## Step 1: auth state

Implement `authState` with `callbackFlow`, an `AuthStateListener` that emits `currentUser`, and `awaitClose` to remove it.

✅ No visible change yet (no user); routing starts working at step 2

## Step 2: sign up

In `signUp`: reject emails that don't end with `STUDENT_DOMAIN` **before** calling Firebase (`throw AuthError.NotStudentEmail` inside `safeCall`), then `createUserWithEmailAndPassword` and `sendEmailVerification`.

✅ `x@gmail.com` → "Seules les adresses @eleve.isep.fr sont acceptées", nothing appears in the console

✅ `prenom.nom@eleve.isep.fr` → "Vérifie ton email" screen, user listed in **Authentication > Users**, email received

## Step 3: sign in and errors

Implement `signIn`, then complete `toAuthError()` so each Firebase exception maps to its `AuthError`.

✅ Sign out (step 5) or reinstall, then sign in with a wrong password → "Email ou mot de passe incorrect"

✅ Sign up again with the same email → "Un compte existe déjà avec cet email"; password `123` → "Mot de passe trop faible"

## Step 4: verification gate

Implement `reloadUser()`: `reload()` the current user, then return `auth.currentUser?.isEmailVerified == true`.

✅ "J'ai vérifié" before clicking the link → "Email pas encore vérifié"

✅ After clicking the link → profile screen

## Step 5: account management

Implement `signOut`, `sendPasswordReset`, `updateDisplayName`, `reauthenticate`, then `changePassword` and `deleteAccount` (both call `reauthenticate` first).

✅ "Mot de passe oublié" → reset email received, new password works

✅ Display name saved → "Nom mis à jour"

✅ Change password with a wrong current password → error; with the right one → "Mot de passe modifié"

✅ Delete account → back on sign in, user gone from the console Users tab

✅ "Se déconnecter" → back on sign in
