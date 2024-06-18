plugins {
    alias(libs.plugins.runique.jvm.library)
    alias(libs.plugins.runique.jvm.junit5)
}

dependencies {
    implementation(projects.core.domain)
}