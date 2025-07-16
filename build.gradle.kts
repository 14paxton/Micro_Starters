plugins {
  id("io.micronaut.application") version "4.5.4"
  id("com.gradleup.shadow") version "8.3.8"
  id("io.micronaut.aot") version "4.5.3"
  kotlin("jvm") version "2.2.0"
  id("org.graalvm.python") version "24.2.1"
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
val graalPythonVersion: String by project

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

//** building on mac ** //
// buildscript {
//    dependencies {
//        classpath("com.github.docker-java:docker-java-transport-httpclient5:3.4.0") {
//            because("M1 macs need a later version of JNA")
//        }
//    }
//}

// shadowJar {
//    archiveBaseName.set('shadow') // Set the base name of the jar
//    archiveClassifier.set('')
//    archiveVersion.set('')
//}


dependencies {
  // Micronaut implementation
  implementation("io.micronaut:micronaut-http-server-netty")
  implementation("io.micronaut.graal-languages:micronaut-graalpy")
  implementation("io.micronaut.serde:micronaut-serde-jackson")
  implementation("io.micronaut.views:micronaut-views-thymeleaf")

  // Runtime dependencies
  runtimeOnly("org.yaml:snakeyaml")
  runtimeOnly("ch.qos.logback:logback-classic")

  // Test dependencies
  testImplementation("io.micronaut:micronaut-http-client")
  testImplementation("io.micronaut.test:micronaut-test-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  // Annotation processors
  annotationProcessor("io.micronaut:micronaut-http-validation")
  annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
  compileOnly("io.micronaut:micronaut-http-client")

  implementation("org.graalvm.polyglot:polyglot:$graalPythonVersion")
  implementation("org.graalvm.polyglot:python:$graalPythonVersion")
}


// *************************************************************************************************************************************
// Micronaut Gradle Plugin options : https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#_micronaut_library_plugin ****

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

// END Micronaut ***********************************************************************************************************************
// *************************************************************************************************************************************


// *************************************************************************************************************************************
// GraalVM Gradle Plugin options : https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html ******************************

graalvmNative {
  toolchainDetection = false
  binaries {
    named("main") {
      configureNativeBinary("nativeChangeMe", fallbackEnabled = false)
    }

    named("optimized") {
      configureNativeBinary("optimizedNativeChangeMe", fallbackEnabled = true)
    }
  }
}

fun org.graalvm.buildtools.gradle.dsl.NativeImageOptions.configureNativeBinary(imageName: String, fallbackEnabled: Boolean) {
  this.imageName.set(imageName)
  richOutput.set(true)
  verbose.set(true)
  fallback.set(fallbackEnabled)
  mainClass.set(mainClassName)
  resources.autodetect()
  buildArgs.add("--verbose")
  javaLauncher.set(javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(jvmVersion))
    vendor.set(graalJvmVendor)
  })
}

// END GraalVM options *****************************************************************************************************************
// *************************************************************************************************************************************


// *************************************************************************************************************************************
// Dockerfile.graalpy-vfs instructions *************************************************************************************************

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("optimizedDockerfileNative") {
  jdkVersion.set(jvmVersion)
  graalImage.set("container-registry.oracle.com/graalvm/native-image:$graalVersion")
  baseImage.set("container-registry.oracle.com/graalvm/native-image:$graalVersion")
  exposedPorts.set(setOf(port.toInt()))
}

// END Dockerfile.graalpy-vfs **********************************************************************************************************
// *************************************************************************************************************************************


tasks.withType<Jar> {
  isZip64 = true
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
  isZip64 = true
}


// *************************************************************************************************************************************
// PYTHON LIBRARIES Import *************************************************************************************************************

val localPackageInstallPathList: Set<String> = PipInstall.resolvePackages(rootDir,
                                                                          listOf(PipInstall.PackageName.PADDLEPADDLE,
                                                                                 PipInstall.PackageName.PADDLEOCR,
                                                                                 PipInstall.PackageName.SCIPY,
                                                                                 PipInstall.PackageName.PANDAS,
                                                                                 PipInstall.PackageName.SCIKIT_LEARN,
                                                                                 PipInstall.PackageName.SHAPELY,
                                                                                 PipInstall.PackageName.TIKTOKEN))
val packagesForPipToPull: Set<String> = setOf(
        // "python-dotenv>=1.1.1",
        // "tqdm>=4.67.1",
        // "PyYAML>=6.0.2",
        // "pydantic>=2.11.7",
        "numpy>=1.26.4",
        "pillow>=11.3.0",
        "pygal",
        "vader-sentiment==3.2.1.1",
        "requests"
                                             )

graalPy {

  // resourceDirectory.set("GRAALPY-VFS/com/skeleton")
  // resourceDirectory.set("org.graalvm.python.vfs")

  packages.set(buildSet {
    add("--prefer-binary")
    add(PipInstall.wheelOsStandard)
    addAll(packagesForPipToPull)
    // addAll(localPackageInstallPathList)
  })
}

// END PYTHON LIBRARIES Import *********************************************************************************************************
// *************************************************************************************************************************************


//*************************************************************************************************************************
// Python Resources For Local .VENV **************************************************************************************

val cleanVenv by tasks.registering(Delete::class) {
  delete(layout.projectDirectory.dir(".venv"))

  doLast {
    println("deleted .venv directory")
  }
}

tasks.register<Copy>("copyVenvResources") {
  group = "python"
  description = "Cleans then copies GraalPy venv resources to .venv"
  dependsOn("graalPyResources", cleanVenv)

  from(layout.buildDirectory.dir("generated/graalpy/resources/org.graalvm.python.vfs/venv")) {
    include("**/*")
  }
  into(layout.projectDirectory.dir(".venv"))

  doLast {
    println("Copied GraalPy venv resources to .venv directory")
  }
}


tasks.named("graalPyResources") {
  finalizedBy("copyVenvResources")
}

// END Python Resources For Local .VENV ***********************************************************************************************