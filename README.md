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


