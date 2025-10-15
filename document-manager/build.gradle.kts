/*
 * Copyright (c) 2024-2025 European Commission
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.jk1.license.filter.ExcludeTransitiveDependenciesFilter
import com.github.jk1.license.filter.LicenseBundleNormalizer
import com.github.jk1.license.filter.ReduceDuplicateLicensesFilter
import com.github.jk1.license.render.InventoryMarkdownReportRenderer
import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import java.util.Locale

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependency.license.report)
    alias(libs.plugins.dependencycheck)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kover)
}

val NAMESPACE: String by project
val GROUP: String by project
val POM_SCM_URL: String by project
val POM_DESCRIPTION: String by project

android {
    namespace = NAMESPACE
    group = GROUP
    compileSdk = 34

    defaultConfig {
        minSdk = 26

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        testApplicationId = "$NAMESPACE.test"
//        testHandleProfiling = true
//        testFunctionalTest = true

        consumerProguardFiles("consumer-rules.pro")

    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }

    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}")
            excludes += listOf("/META-INF/versions/9/OSGI-INF/MANIFEST.MF")
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    kotlinOptions {
        jvmTarget = libs.versions.java.get()
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.time.ExperimentalTime",
            "-opt-in=kotlin.ExperimentalApi"
        )
    }
}

dependencies {
    // Multipaz library
    api(libs.multipaz) {
        exclude(group = "org.bouncycastle")
        exclude(group = "io.ktor")
    }

    implementation(libs.kotlinx.io.core)
    implementation(libs.kotlinx.io.bytestring)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)

    // CBOR
    implementation(libs.cbor)
    implementation(libs.cose)

    // sd-jwt-vc
    implementation(libs.eudi.sd.jwt.vc.kt)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.nimbus.jose.jwt)

    implementation(libs.bouncy.castle.prov)
    implementation(libs.bouncy.castle.pkix)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
    testImplementation(libs.json)
    testImplementation(libs.kotlinx.coroutines.test)
}

// Dependency check

dependencyCheck {
    formats = listOf("XML", "HTML")
    System.getenv("NVD_API_KEY")?.let {
        nvd.apiKey = it
    }
}

// Dokka generation

tasks.dokkaGfm.configure {
    val outputDir = file("$rootDir/docs")
    doFirst { delete(outputDir) }
    outputDirectory.set(outputDir)
}

tasks.register<Jar>("dokkaHtmlJar") {
    group = "documentation"
    description = "Assembles a jar archive containing the HTML documentation."
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}

tasks.register<Jar>("dokkaJavadocJar") {
    group = "documentation"
    description = "Assembles a jar archive containing the Javadoc documentation."
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

// Third-party licenses report

licenseReport {
    unionParentPomLicenses = false
    filters = arrayOf(
        LicenseBundleNormalizer(),
        ReduceDuplicateLicensesFilter(),
        ExcludeTransitiveDependenciesFilter()
    )
    configurations = arrayOf("releaseRuntimeClasspath")
    excludeBoms = true
    excludeOwnGroup = true
    renderers = arrayOf(InventoryMarkdownReportRenderer("licenses.md", POM_DESCRIPTION))
}

tasks.generateLicenseReport.configure {
    doLast {
        copy {
            from(layout.buildDirectory.file("reports/dependency-license/licenses.md"))
            into(rootDir)
        }
    }
}

// Build documentation and license report
tasks.register<Task>("buildDocumentation") {
    group = "documentation"
    description = "Builds the documentation and license report."
    dependsOn("dokkaGfm", "generateLicenseReport")
}
tasks.assemble.configure {
    finalizedBy("buildDocumentation")
}

// Publish

mavenPublishing {
    configure(
        AndroidMultiVariantLibrary(
            sourcesJar = true,
            publishJavadocJar = true,
            setOf("release")
        )
    )
    pom {
        ciManagement {
            system = "github"
            url = "${POM_SCM_URL}/actions"
        }
    }
}
// handle java.lang.UnsupportedOperationException: PermittedSubclasses requires ASM9
// when publishing module
afterEvaluate {
    tasks.named("javaDocReleaseGeneration").configure {
        enabled = false
    }
}



fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
