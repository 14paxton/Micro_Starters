plugins {
  id("io.micronaut.application") version "4.5.4"
  id("com.gradleup.shadow") version "8.3.8"
  id("io.micronaut.aot") version "4.5.3"
  kotlin("jvm") version "2.2.0"
}

// *************************************************************************************************************************************
// Version Variables *******************************************************************************************************************

val graalVersion: String by project
val port: String by project
val jvmVersion: String by project
val javaVersion = JavaVersion.toVersion(jvmVersion)
val graalJvmVendor = JvmVendorSpec.ORACLE
val groupName = "com.skeleton"
val mainClassName = "$groupName.Application"

// END Version Variables ***************************************************************************************************************
// *************************************************************************************************************************************

group = groupName
version = "0.1"

repositories {
  mavenLocal()
  mavenCentral()
}

application {
  mainClass.set(mainClassName)
}

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(jvmVersion))
    vendor.set(graalJvmVendor)
  }
}

// shadowJar {
//    archiveBaseName.set('shadow') // Set the base name of the jar
//    archiveClassifier.set('')
//    archiveVersion.set('')
//}


dependencies {
  // Core implementation dependencies
  implementation("io.micronaut:micronaut-http-client-jdk")
  implementation("io.micronaut:micronaut-http-server-netty")
  implementation("io.micronaut.serde:micronaut-serde-jackson")

  // Compile-only dependencies
  compileOnly("io.micronaut:micronaut-http-client")

  // Runtime dependencies
  runtimeOnly("org.yaml:snakeyaml")
  runtimeOnly("ch.qos.logback:logback-classic")

  // Annotation processors
  annotationProcessor("io.micronaut:micronaut-http-validation")
  annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
  annotationProcessor("io.micronaut.serde:micronaut-serde-processor")

  // *** For ARM MacOS Silicone *** //
  // - Netty native DNS resolver for MacOS (ARM & Intel)
  //    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.109.Final")
  // - Docker Java client (optional: only if using Docker/Testcontainers)
  //    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.4.0")
}

micronaut {
  runtime("netty")
  testRuntime("junit5")
  processing {
    incremental(true)
    annotations("$groupName.*")
  }
  aot {
    configFile = file("gradle/micronaut-aot.properties")
  }
}




