import org.springframework.boot.gradle.tasks.aot.AbstractAot
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.10.1"
}

group = "de.cofinpro"
version = "0.6.6-SNAPSHOT"
val dockerHubRepo = "wisskirchenj/"

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
//    buildpacks.set(listOf("paketobuildpacks/java:beta"))
    buildpacks.set(listOf("paketobuildpacks/java-native-image:beta"))
    builder.set("paketobuildpacks/builder-jammy-buildpackless-tiny")
    imageName.set(dockerHubRepo + rootProject.name + ":" + version)
    createdDate.set("now")
}