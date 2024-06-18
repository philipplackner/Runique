import com.android.build.api.dsl.ApplicationExtension
import com.plcoding.convention.configureAndroidCompose
import com.plcoding.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class JvmJUnit5ConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            tasks.withType<Test> {
                useJUnitPlatform()
            }
            dependencies {
                "testImplementation"(libs.findLibrary("junit5.api").get())
                "testImplementation"(libs.findLibrary("junit5.params").get())
                "testRuntimeOnly"(libs.findLibrary("junit5.engine").get())

                "testImplementation"(libs.findLibrary("assertk").get())
                "testImplementation"(libs.findLibrary("turbine").get())
            }
        }
    }
}