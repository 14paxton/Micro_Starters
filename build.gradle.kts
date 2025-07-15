import PipInstall.PackageName.*
import PipInstall.resolvePackages
import PipInstall.wheelOsStandard
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.micronaut.gradle.docker.NativeImageDockerfile
import org.graalvm.buildtools.gradle.dsl.NativeImageOptions


// *************************************************************************************************************************************
// Version Variables *******************************************************************************************************************

val micronautVersion: String by project
val graalPythonVersion: String by project
val graalVersion: String by project
val port: String by project
val jvmVersion: String by project
val javaVersion = JavaVersion.toVersion(jvmVersion)
val graalJvmVendor = JvmVendorSpec.ORACLE
val groupName = "com.skeleton"
val mainClassName = "$groupName.Application"

// END Version Variables ***************************************************************************************************************
// *************************************************************************************************************************************


plugins {
  id("io.micronaut.application") version "4.5.4"
  id("org.graalvm.python") version "24.2.1"
  id("com.gradleup.shadow") version "8.3.8"
  id("io.micronaut.aot") version "4.5.3"
  kotlin("jvm") version "2.2.0"
}

group = groupName
version = "0.1"

repositories {
  mavenLocal()
  mavenCentral()
}

application {
  mainClass.set(mainClassName)
  applicationDefaultJvmArgs = listOf("-Dpolyglot.engine.WarnInterpreterOnly=false",
                                     "-Dpolyglot.log.file=Log/truffle.log",
                                     "--enable-native-access=org.graalvm.truffle",
                                     "-Dpolyglot.engine.WarnVirtualThreadSupport=false")
}

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(jvmVersion))
    vendor.set(graalJvmVendor)
  }
}


// *************************************************************************************************************************************
// PYTHON LIBRARIES Import *************************************************************************************************************

val localPackageInstallPathList: Set<String> = resolvePackages(rootDir,
                                                               listOf(PADDLEPADDLE, PADDLEOCR, SCIPY, PANDAS, SCIKIT_LEARN, SHAPELY, TIKTOKEN))
val packagesForPipToPull: Set<String> = setOf(
        "numpy>=1.26.4",
        // "python-dotenv>=1.1.1",
        // "tqdm>=4.67.1",
        // "PyYAML>=6.0.2",
        // "pydantic>=2.11.7",
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
    add(wheelOsStandard)
    addAll(packagesForPipToPull)
    // addAll(localPackageInstallPathList)
  })
}

// END PYTHON LIBRARIES Import *********************************************************************************************************
// *************************************************************************************************************************************


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

fun NativeImageOptions.configureNativeBinary(imageName: String, fallbackEnabled: Boolean) {
  this.imageName.set(imageName)
  richOutput.set(true)
  verbose.set(true)
  fallback.set(fallbackEnabled)
  mainClass.set(mainClassName)
  resources.autodetect()
  javaLauncher.set(javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(jvmVersion))
    vendor.set(graalJvmVendor)
  })
}

// END GraalVM options *****************************************************************************************************************
// *************************************************************************************************************************************


// *************************************************************************************************************************************
// Dockerfile.graalpy-vfs instructions *************************************************************************************************

tasks.named<NativeImageDockerfile>("optimizedDockerfileNative") {
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

tasks.withType<ShadowJar> {
  isZip64 = true
}


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


// This explicitly tells Gradle that processResources and processTestResources tasks depend on the graalPyResources task, ensuring proper task
// ordering.
tasks.named("processResources") {
  dependsOn("graalPyResources")
}
tasks.named("processTestResources") {
  dependsOn("graalPyResources")
}

tasks.named("test") {
  dependsOn("graalPyResources")
}

// This tells Gradle to include duplicate resources rather than failing the build when it encounters them.
tasks.withType<ProcessResources> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

sourceSets {
  main {
    resources {
      srcDir("build/generated/graalpy/resources")
    }
  }
  test {
    resources {
      srcDir("build/generated/graalpy/resources")
    }
  }
}