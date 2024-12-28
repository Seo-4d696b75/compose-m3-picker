import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
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
tasks.register("publishMavenCentralAPI") {
    dependsOn("archivePublication")
    doLast {
        val username = project.properties["SONATYPE_USERNAME"].toString()
        val password = project.properties["SONATYPE_PASSWORD"].toString()

        @OptIn(ExperimentalEncodingApi::class)
        val auth = Base64.encode("$username:$password".encodeToByteArray())

        exec {
            commandLine = listOf(
                "curl",
                "--request", "POST",
                "--verbose",
                "--header", "Authorization: Bearer $auth",
                "--form", "bundle=@build/distributions/lib.zip",
                "https://central.sonatype.com/api/v1/publisher/upload?publishingType=AUTOMATIC",
            )
        }
    }
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
