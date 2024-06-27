plugins {
    alias(libs.plugins.runique.jvm.library)
    alias(libs.plugins.runique.jvm.junit5)
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)
    implementation(projects.run.domain)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.junit5.api)
    implementation(libs.coroutines.test)
}