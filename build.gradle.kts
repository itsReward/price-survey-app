plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.pricesurvey"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.postgresql:postgresql")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")
    implementation("org.flywaydb:flyway-core")
    implementation("io.springfox:springfox-boot-starter:3.0.0")


    // Google OAuth
    implementation("com.google.api-client:google-api-client:1.34.1")
    implementation("com.google.apis:google-api-services-oauth2:v2-rev20200213-2.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Make sure bootJar task is explicitly configured
tasks.bootJar {
    archiveFileName.set("app.jar")

    // Print information about the output
    doLast {
        println("====== Boot JAR created ======")
        println("JAR file: ${archiveFile.get().asFile.absolutePath}")
        println("JAR file exists: ${archiveFile.get().asFile.exists()}")
        println("JAR file size: ${archiveFile.get().asFile.length()} bytes")
        println("==============================")
    }
}

// Explicitly disable the plain jar task to avoid confusion
tasks.jar {
    enabled = false
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}


// Add a task to print build info
tasks.register("printBuildInfo") {
    doLast {
        println("====== Build Information ======")
        println("Project: ${project.name}")
        println("Group: ${project.group}")
        println("Version: ${project.version}")
        println("Build directory: ${project.buildDir}")
        println("==============================")
    }
}

// Make bootJar depend on printBuildInfo
tasks.bootJar {
    dependsOn("printBuildInfo")
}