plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
    alias(libs.plugins.owasp)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jda)
    implementation(libs.logback)
    implementation(libs.dotenv)
    implementation(libs.hikari)
    implementation(libs.sqlite)
}

spotless {
    java {
        googleJavaFormat()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainClass = "org.example.Iapetus"
}

tasks.spotlessCheck {
    dependsOn(tasks.spotlessApply)
}
