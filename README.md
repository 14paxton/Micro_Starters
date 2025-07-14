# Install

1) install java GraalVM , if haven't install sdkman
   ```shell
    sdk install java 24.0.1-graal
   ```
2) can use sdk env (uses .sdkmanrc file) to set a java version
   ```shell
    sdk env
   ```
3) with Gradle wrapper, no need to install anything else, wouldn't necessarily need python installed either

## MacOs

```shell
brew install jpeg;
brew install libtiff libjpeg libpng freetype;
brew install pkg-config;
```

# Build

```shell
    ./gradlew build
```

# Test

   ```shell
     ./gradlew test
   ```

# Run

  ```shell
    ./gradlew run
  ```

# NativeCompile

## Build

  ```shell
    ./gradlew nativeCompile
  ```

- output for native image will be in ```build/native/nativeCompile/```

## Optimized Native Compile

### Build

```shell
    ./gradlew nativeOptimizedCompile
```

- output for native image will be in ```build/native/nativeOptimizedCompile/```

### Run

   ```shell
        ./build/native/nativeOptimizedCompile/optimizedNativeNameplateDataLogger
   ```

- view at [http://localhost:8181/ ](http://localhost:8181/ )

### Create Docker File and Image from GraalVm Native Image

#### Build File and Image

   ```shell
     ./gradlew optimizedDockerfileNative
   ```

- Dockerfile for build will be `build/docker/native-optimized/DockerfileNative`

#### Run Image

   ```shell
        docker run  --name nameplatedatalogger --rm -p 8181:8181 nameplate-data-logger:latest
   ```

## Micronaut 4.8.3 Documentation

- [User Guide](https://docs.micronaut.io/4.8.3/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.8.3/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.8.3/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://gradleup.com/shadow/)

## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)

## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)

## Feature graalpy documentation

- [Micronaut GraalPy Extension documentation](https://micronaut-projects.github.io/micronaut-graal-languages/latest/guide/)

- [https://graalvm.org/python](https://graalvm.org/python)



