plugins {
    `kotlin-dsl`
}

group = "com.plcoding.runique.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin) // include only during compile
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "runique.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}