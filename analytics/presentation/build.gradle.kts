plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.plcoding.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}