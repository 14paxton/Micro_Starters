package com.skeleton;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@Property(name = "micronaut.server.port", value = "-1")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelloControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void testHelloResponse() {
    String response = client.toBlocking()
                            .retrieve(HttpRequest.GET("/hello"));
    assertEquals("Hello World", response);
  }
}
