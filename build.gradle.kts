// plugins declared once here then applied in app
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // TODO step 0 apply the google services plugin (libs.plugins.google.services)
}
