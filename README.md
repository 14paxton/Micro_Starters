# Install

1) install java GraalVM , if haven't install sdkman
   ```shell
    sdk install java 24.0.1-graal
   ```
2) can use sdk env (uses .sdkmanrc file) to set a java version
   ```shell
    sdk env
   ```

# Build

```shell
    ./gradlew build
```

- using the following to generate reflection meta-data

```shell
java -agentlib:native-image-agent=config-output-dir=./META-INF/native-image -jar your-micronaut-app.jar
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

# Lambda

## Make Call

### Local

```shell
 curl -d '{"to": {"email":"14paxton@gmail.com" , "name":"brandon paxton"}}' \
     -H "Content-Type: application/json" \
     -X POST http://localhost:8181/test
```

### AWS

### CLI

#### Deploy Code

```shell
aws lambda update-function-code --function-name GraalVM-Mail --zip-file fileb://./build/libs/changeMe-0.1-optimized-lambda.zip
```

### AWS Test

```shell
curl -v 'https://o2ecgfgw7meoucnh7hvffxb2pi0hendb.lambda-url.us-east-1.on.aws/?campaign=test' \
-H 'content-type: application/json'
```

```json
{
  "queryStringParameters": {
    "campaign": "test"
  }
}
```

#### Using Body

```json
{
  "path": "/test",
  "httpMethod": "GET",
  "headers": {
    "Accept": "application/json"
  },
  "body": "{\"email\":\"email@gmail.com\", \"name\":\"Example\", \"campaign\":\"TEST\"}"
}
```

## Deploy

### Build jar

```shell
 ./gradlew buildNativeLambda
```

- > Optimized
    ```shell
    ./gradlew :optimizedBuildNativeLambda
    ```

## API Gateway

### CURL

#### Single email

```shell
curl -X GET  'https://4lw29o2bw6.execute-api.us-east-1.amazonaws.com/default/test' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -d '{"email":"email@gmail.com"}'
```


# Resources


## Micronaut Documentation

- [User Guide](https://docs.micronaut.io/latest/guide/index.html)
- [API Reference](https://docs.micronaut.io/latest/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/latest/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://gradleup.com/shadow/)

## AWS 

- [Micronaut AWS Docs](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#lambda)
- [Micronaut Lambda Tutorials](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#lambdaTutorials)
- [Micronaut AWS Runtimes](https://micronaut-projects.github.io/micronaut-aws/latest/guide/#customRuntimes)

- [AWS Lambda Runtime](https://docs.aws.amazon.com/lambda/latest/dg/lambda-runtimes.html)

## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)

## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)
