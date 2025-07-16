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
  // Core implementation dependencies
  implementation("io.micronaut:micronaut-http-client-jdk")
  implementation("io.micronaut:micronaut-http-server-netty")
  implementation("io.micronaut.serde:micronaut-serde-jackson")
  /** From GraalVM Guide **/
  implementation("jakarta.validation:jakarta.validation-api")

  // Compile-only dependencies
  compileOnly("io.micronaut:micronaut-http-client")

  // Runtime dependencies
  runtimeOnly("org.yaml:snakeyaml")
  runtimeOnly("ch.qos.logback:logback-classic")

  // Annotation processors
  annotationProcessor("io.micronaut:micronaut-http-validation")
  annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
  annotationProcessor("io.micronaut.serde:micronaut-serde-processor")

  // Test dependencies
  testImplementation("io.micronaut:micronaut-http-client")
  testImplementation("io.micronaut.test:micronaut-test-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  // *** AWS *** //
  implementation("io.micronaut.aws:micronaut-aws-lambda-events-serde")
  /*** Not With Controller ***/
  // implementation("com.amazonaws:aws-lambda-java-events")
  //*For writing raw Lambda functions with minimal Micronaut overhead*
  //   implementation("io.micronaut.aws:micronaut-function-aws")
  //*Required for GraalVM Native Image Lambda deployments*
  //   implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime")


  /*** email ***/
  // implementation("io.micronaut.email:micronaut-email-sendgrid")

  /*** reactive streams ***/
  // implementation("io.micronaut.reactor:micronaut-reactor")

  /*** from aws micronaut docs ***/
  //    implementation("io.micronaut:micronaut-http-client")

  /*** remove dependency for macos ***/
  //    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.2.0.Alpha4")
  //    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.3.1")
}


// *************************************************************************************************************************************
// Micronaut Gradle Plugin options : https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#_micronaut_library_plugin ****

micronaut {
  //*** test local and running normal lambda ***//
  //    runtime("lambda_java")

  //** GraalVM buildNativeLambda **//
  runtime("lambda_provided")
  nativeLambda {
    lambdaRuntimeClassName = " io.micronaut.function.aws.runtime.MicronautLambdaRuntime"
  }
  //***********************************//

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
  metadataRepository {
    enabled.set(true)
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
// Dockerfile *************************************************************************************************


// tasks.named("nativeCompile") {
//    classpathJar = layout.projectDirectory.file("build/libs/shadow.jar")
//}

// tasks.named("dockerfileNative") {
//   baseImage = "amazonlinux:2023"
//   jdkVersion = "21"
//   args("-XX:MaximumHeapSizePercent=80",
//        "-Dio.netty.allocator.numDirectArenas=0",
//        "-Dio.netty.noPreferDirect=true")
// }

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
  jdkVersion = "21"
  jdkVersion.set(jvmVersion)
  graalImage.set("container-registry.oracle.com/graalvm/native-image:$graalVersion")
  baseImage.set("amazonlinux:2023")
  exposedPorts.set(setOf(port.toInt()))
  args(
          "-XX:MaximumHeapSizePercent=80",
          "-Dio.netty.allocator.numDirectArenas=0",
          "-Dio.netty.noPreferDirect=true"
      )
}

// END Dockerfile **********************************************************************************************************
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
