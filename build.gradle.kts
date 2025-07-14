import PipInstall.PackageName.*
import PipInstall.resolvePackages
import PipInstall.wheelOsStandard
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.micronaut.gradle.docker.NativeImageDockerfile
import org.graalvm.buildtools.gradle.dsl.NativeImageOptions

// *************************************************************************************************************************************
// Version Variables *******************************************************************************************************************

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
val packagesForPipToPull: Set<String> = setOf("numpy>=1.26.4",
                                              "python-dotenv>=1.1.1",
                                              "tqdm>=4.67.1",
                                              "PyYAML>=6.0.2",
                                              "pydantic>=2.11.7",
                                              "pillow>=11.3.0")

graalPy {

  // resourceDirectory.set("GRAALPY-VFS/com/skeleton")
  // resourceDirectory.set("org.graalvm.python.vfs")

  packages.set(buildSet {
    add("--prefer-binary")
    add(wheelOsStandard)
    addAll(packagesForPipToPull)
    addAll(localPackageInstallPathList)
  })
}

// END PYTHON LIBRARIES Import *********************************************************************************************************
// *************************************************************************************************************************************


dependencies {
  compileOnly("io.micronaut:micronaut-http-client")

  implementation("org.graalvm.polyglot:polyglot:$graalPythonVersion")
  implementation("org.graalvm.polyglot:python:$graalPythonVersion")

  implementation("io.micronaut.views:micronaut-views-thymeleaf")
  implementation("io.micronaut:micronaut-http-server-netty")
  implementation("io.micronaut.graal-languages:micronaut-graalpy")
  implementation("io.micronaut.serde:micronaut-serde-jackson")

  runtimeOnly("org.yaml:snakeyaml")
  runtimeOnly("ch.qos.logback:logback-classic")

  testImplementation("io.micronaut:micronaut-http-client")
  testImplementation("io.micronaut.test:micronaut-test-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-api")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  annotationProcessor("io.micronaut:micronaut-http-validation")
  annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
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
  mainClass.set(mainClass)
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
// *************************************************************************************************************************************


// *************************************************************************************************************************************
// Build Performance Optimizations *****************************************************************************************************

// Enable build caching and parallel execution
gradle.startParameter.isBuildCacheEnabled = true

// Configure test tasks for better performance
tasks.withType<Test>()
        .configureEach {
          useJUnitPlatform()

          maxParallelForks = (Runtime.getRuntime()
                                      .availableProcessors() / 2).coerceAtLeast(1)

          testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = false
          }

          // Optional: enable caching unless explicitly disabled
          outputs.cacheIf { true }
        }
