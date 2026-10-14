package com.plcoding.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.File
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension,
    extensionType: ExtensionType
) {
    commonExtension.buildFeatures.apply { buildConfig = true }

    val apiKey = gradleLocalProperties(rootDir, rootProject.providers).getProperty("API_KEY")
    when(extensionType) {
        ExtensionType.APPLICATION -> {
            this@configureBuildTypes.extensions.configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        configureReleaseBuildType(
                            defaultProguardFile = getDefaultProguardFile("proguard-android-optimize.txt"),
                            apiKey = apiKey
                        )
                    }
                }
            }
        }
        ExtensionType.LIBRARY -> {
            this@configureBuildTypes.extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        // A LIBRARY DOES NOT SHRINK ITSELF. R8 inside a library
                        // cannot see sibling modules -- those arrive only when
                        // the app is assembled -- so it reports them missing and
                        // asks to be told to ignore the project's own classes.
                        // Shrinking belongs to the base module, which sees
                        // everything; anything a library needs the app to honour
                        // goes in its consumer-rules.pro.
                        configureReleaseBuildType(
                            defaultProguardFile = null,
                            apiKey = apiKey,
                            minify = false,
                        )
                    }
                }
            }
        }
        ExtensionType.DYNAMIC_FEATURE -> {
            this@configureBuildTypes.extensions.configure<DynamicFeatureExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(apiKey)
                    }
                    release {
                        // NO DEFAULT PROGUARD FILE HERE. AGP rejects it in a
                        // dynamic feature, and the message says where it belongs:
                        //
                        //   Default file proguard-android-optimize.txt should
                        //   not be specified in this module. It can be specified
                        //   in the base module instead.
                        //
                        // :app already specifies it, and shrinking a dynamic
                        // feature is driven from there -- which is why
                        // minification was already off on this branch.
                        configureReleaseBuildType(
                            defaultProguardFile = null,
                            apiKey = apiKey,
                            minify = false,
                        )
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://runique.pl-coding.com:8080\"")
}

private fun BuildType.configureReleaseBuildType(
    defaultProguardFile: File?,
    apiKey: String,
    minify: Boolean = true,
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"https://runique.pl-coding.com:8080\"")

    isMinifyEnabled = minify
    if (defaultProguardFile != null) {
        proguardFiles(
            defaultProguardFile,
            "proguard-rules.pro"
        )
    }
}