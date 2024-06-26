import org.springframework.boot.gradle.tasks.aot.AbstractAot
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.graalvm.buildtools.native") version "0.10.2"
}

group = "de.cofinpro"
version = "0.7.0-SNAPSHOT"
val dockerHubRepo = "wisskirchenj/"

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(22))
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    val commonsVersion = "1.11.0"
    testImplementation("org.apache.commons:commons-text:${commonsVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

graalvmNative {
    testSupport.set(false)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<AbstractAot>("processTestAot") {
    enabled = false
}

tasks.named<BootBuildImage>("bootBuildImage") {
//    buildpacks.set(listOf("paketobuildpacks/java:latest"))
    buildpacks.set(listOf("paketobuildpacks/java-native-image:latest"))
    builder.set("paketobuildpacks/builder-jammy-buildpackless-tiny")
    environment.put("BP_NATIVE_IMAGE_BUILD_ARGUMENTS", "-H:-AddAllFileSystemProviders")
    imageName.set(dockerHubRepo + rootProject.name + ":" + version)
    createdDate.set("now")
}