plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.plcoding.analytics.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}