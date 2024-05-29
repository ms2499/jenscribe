plugins {
	java
	id("org.springframework.boot") version "2.7.16"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.jenscribe"
version = "v0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
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
	implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.dom4j:dom4j:2.1.4")
	implementation("com.google.code.gson:gson:2.9.1")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Jar> {
	manifest {
		attributes["Class-Path"] = "/usr/tandem/javaexth11/lib/tdmext.jar " +
								   "/java/sam/enslib/testLib.jar"
		exclude("tdmext.jar")
	}
}