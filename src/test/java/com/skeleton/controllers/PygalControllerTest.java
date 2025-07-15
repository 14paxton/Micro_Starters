package com.skeleton.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class PygalControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void testPygalResponse() {
    String response = client.toBlocking()
                            .retrieve(HttpRequest.GET("/pygal"));
    assertTrue(response.contains("<svg xmlns"), "SVG namespace missing");
    assertTrue(response.contains("<title>Pygal</title>"), "Title missing");
    assertTrue(response.contains("<g class=\"graph stackedbar-graph vertical\">"), "Graph group missing");
  }
}