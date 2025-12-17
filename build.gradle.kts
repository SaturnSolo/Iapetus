plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta17"
    application
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://m2.dv8tion.net/releases") }
}

dependencies {
    implementation("net.dv8tion:JDA:5.2.2")
    implementation("ch.qos.logback:logback-classic:1.5.20")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("org.json:json:20240303")
    // implementation("org.junit.jupiter:junit-jupiter-engine:6.0.0")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.apache.maven.plugins:maven-jar-plugin:4.0.0-beta-1")
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(18)
    }
}

application {
    mainClass = "org.example.Main"
}
