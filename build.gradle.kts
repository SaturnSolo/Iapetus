plugins {
    id("java")
    alias(libs.plugins.shadow)
    application
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://m2.dv8tion.net/releases") }
}

dependencies {
    implementation(libs.jda)
    implementation(libs.logback)
    implementation(libs.dotenv)
    implementation(libs.json)
    implementation(libs.junitjupiter)
    implementation(libs.hikari)
    implementation(libs.sqlite)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(18)
    }
}

application {
    mainClass = "org.example.Main"
}
