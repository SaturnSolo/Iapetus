plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
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
    implementation(libs.junitjupiter)
    implementation(libs.hikari)
    implementation(libs.sqlite)
}

spotless {
    java {
        eclipse().configFile(project.rootDir.resolve("config/editorconfig.xml"))
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.Iapetus"
}
