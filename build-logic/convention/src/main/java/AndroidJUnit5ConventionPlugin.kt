import com.android.build.api.dsl.ApplicationExtension
import com.plcoding.convention.configureAndroidCompose
import com.plcoding.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class AndroidJUnit5ConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("runique.jvm.junit5")
            pluginManager.apply("de.mannodermaus.android-junit5")

            dependencies {
                "androidTestImplementation"(libs.findLibrary("junit5.api").get())
                "androidTestImplementation"(libs.findLibrary("junit5.params").get())
                "androidTestImplementation"(libs.findLibrary("junit5.android.test.compose").get())
                "debugImplementation"(libs.findLibrary("androidx.compose.ui.test.manifest").get())
                "androidTestRuntimeOnly"(libs.findLibrary("junit5.engine").get())

                "androidTestImplementation"(libs.findLibrary("assertk").get())
                "androidTestImplementation"(libs.findLibrary("coroutines.test").get())
                "androidTestImplementation"(libs.findLibrary("turbine").get())
            }
        }
    }
}