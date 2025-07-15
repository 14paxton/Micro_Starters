package com.skeleton;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(transactional = false, environments = "test")
@Property(name = "micronaut.server.port", value = "-1")
class SentimentAnalysisControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void testAnalyzeResponse() {
    HttpRequest<?> requestHappy = HttpRequest.GET("/sentiment/analyze?text=happy");
    HttpRequest<?> requestSad = HttpRequest.GET("/sentiment/analyze?text=sad");

    Map<String, Double> responseHappy = client.toBlocking()
                                              .retrieve(requestHappy, Argument.mapOf(String.class, Double.class));

    assertTrue(responseHappy.get("compound") > 0.1, "Expected positive sentiment for 'happy'");

    Map<String, Double> responseSad = client.toBlocking()
                                            .retrieve(requestSad, Argument.mapOf(String.class, Double.class));

    assertTrue(responseSad.get("compound") < -0.1, "Expected negative sentiment for 'sad'");

  }
}
