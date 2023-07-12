plugins {
    kotlin("jvm")
}

dependencies {
    // Kotlin
    implementation(libs.bundles.kotlin)
    // AstraLibs
    implementation(libs.minecraft.astralibs.ktxcore)
    implementation(libs.minecraft.astralibs.orm)
    implementation(libs.minecraft.astralibs.di)
    implementation(libs.minecraft.astralibs.spigot.gui)
    implementation(libs.minecraft.astralibs.spigot.core)
    // Spigot dependencies
    compileOnly(libs.minecraft.paper.api)
    // Local
    implementation(project(mapOf("path" to ":api")))
    implementation(project(":plugin"))
    implementation(project(":api"))
}
