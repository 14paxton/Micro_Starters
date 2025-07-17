pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
  plugins {
    id("io.micronaut.application") version "4.5.4"
    id("com.gradleup.shadow") version "8.3.8"
    id("io.micronaut.aot") version "4.5.3"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    id("org.graalvm.python") version "24.2.1"
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}


rootProject.name = "changeMe"

buildCache {
  local {
    isEnabled = true
  }
}