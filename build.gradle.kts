plugins {
	java
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "de.cofinpro"
version = "0.5-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(19))
	}
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
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
	runtimeOnly("com.h2database:h2:2.1.214")
	
    testImplementation("org.apache.commons:commons-text:1.10.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok:1.18.24")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}
