# CampusAuth_FirebaseDemo

Live demo of Firebase Authentication (email/password) for ISEP students: sign-up restricted to `@eleve.isep.fr`, email verification, password reset, profile and account management.

## Setup

1. Open the [Firebase console](https://console.firebase.google.com), create a project.
2. **Authentication > Sign-in method**: enable **Email/Password**.
3. *(optional)* **Authentication > Templates**: set the template language to any language (default in English).
4. **Project settings > Add app > Android**, package name `fr.isep.campusauth`.
5. Download `google-services.json` and put it in `app/` (it is git-ignored).

## Run

- Open the folder in Android Studio (latest stable), let Gradle sync, run `app` on a device or emulator (API 24+) with Google Play services and internet.
- Command line: the wrapper jar is not included, so run `gradle wrapper` once (or let Android Studio create it), then `./gradlew installDebug`.

## Demo scenario

Using a real `@eleve.isep.fr` mailbox:

1. **Rejected sign-up**: create an account with `test@gmail.com` → "Seules les adresses @eleve.isep.fr sont acceptées" (checked locally, no Firebase call).
2. **Accepted sign-up**: create an account with `prenom.nom@eleve.isep.fr` → user appears in the console (*Authentication > Users*), verification email sent.
3. **Verification gate**: the app stays on "Vérifie ton email"; tap "J'ai vérifié" → "Email pas encore vérifié". Click the email link, tap again → profile screen.
4. **Wrong password**: sign out, sign in with a wrong password → "Email ou mot de passe incorrect".
5. **Reset password**: type the email, tap "Mot de passe oublié", open the email, set a new password, sign in with it.
6. **Update display name**: enter a name, "Enregistrer" → "Nom mis à jour".
7. **Change password**: wrong current password → error (reauthentication fails); correct one → "Mot de passe modifié".
8. **Delete account**: enter the password, confirm the dialog → back to sign in, user gone from the console.
