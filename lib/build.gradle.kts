import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.seo4d696b75.compose.material3.picker"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeCompiler {
        val reportDir = layout.buildDirectory.dir("compose_compiler")
        reportsDestination = reportDir
        metricsDestination = reportDir
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.material3)
    api(libs.kotlinx.collections.immutable)
}

// zip all the files to be published before curl POST
tasks.register<Zip>("archivePublication") {
    dependsOn("publishMavenPublicationToTmpRepository")
    from(layout.buildDirectory.dir("tmp/com/seo4d696b75/compose/material3-picker/${libs.versions.publish.get()}"))
    into("/com/seo4d696b75/compose/material3-picker/${libs.versions.publish.get()}")
}

// TODO find official gradle plugin
// publish via Central Publisher API
// @see https://central.sonatype.com/api-doc
tasks.register<Exec>("publishMavenCentralAPI") {
    dependsOn("archivePublication")

    val username = project.properties["SONATYPE_USERNAME"].toString()
    val password = project.properties["SONATYPE_PASSWORD"].toString()

    @OptIn(ExperimentalEncodingApi::class)
    val auth = Base64.encode("$username:$password".encodeToByteArray())

    executable = "curl"
    args(
        listOf(
            "--request", "POST",
            "--verbose",
            "--header", "Authorization: Bearer $auth",
            "--form", "bundle=@build/distributions/lib.zip",
            "https://central.sonatype.com/api/v1/publisher/upload?publishingType=AUTOMATIC",
        )
    )
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("maven") {
                groupId = "com.seo4d696b75.compose"
                artifactId = "material3-picker"
                version = libs.versions.publish.get()
                from(components["release"])

                pom {
                    name = "Compose Material3 Picker"
                    description = "Android Jetpack Compose picker compatible with Material3."
                    url = "https://github.com/Seo-4d696b75/compose-m3-picker"
                    licenses {
                        license {
                            name = "The Apache Software License, Version 2.0"
                            url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                            distribution = "repo"
                        }
                    }
                    developers {
                        developer {
                            id = "seo4d696b75"
                            name = "Seo-4d696b75"
                            email = "s.kaoru509@gmail.com"
                        }
                    }
                    scm {
                        connection = "scm:git:https://github.com/Seo-4d696b75/compose-m3-picker.git"
                        developerConnection =
                            "scm:git:ssh://github.com/Seo-4d696b75/compose-m3-picker.git"
                        url = "https://github.com/Seo-4d696b75/compose-m3-picker"
                    }
                }
            }
        }
        repositories {
            maven {
                // maven-publish plugin is not used for publishing,
                // but only for generating artifact files
                name = "tmp"
                url = uri(layout.buildDirectory.dir("tmp"))
            }
        }
    }
    signing {
        sign(publishing.publications["maven"])
    }
}
