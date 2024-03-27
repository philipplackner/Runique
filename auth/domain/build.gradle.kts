plugins {
    alias(libs.plugins.runique.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}