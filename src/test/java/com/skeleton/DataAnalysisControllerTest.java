package com.skeleton;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class DataAnalysisControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void testIndexEndpoint() {
    HttpRequest<?> request = HttpRequest.GET("/");
    HttpResponse<String> response = client.toBlocking()
                                          .exchange(request, String.class);

    assertEquals(HttpStatus.OK, response.getStatus());
  }

  @Test
  void testAnalyzeCsvMulti_ValidCsv() {
    // Prepare test CSV data
    String csvContent = "value1,value2,value3\n1.0,2.0,3.0\n4.0,5.0,6.0\n7.0,8.0,9.0";

    // Create multipart body (no poolSize parameter needed)
    MultipartBody body = MultipartBody.builder()
                                      .addPart("file", "test.csv", csvContent.getBytes(StandardCharsets.UTF_8))
                                      .build();

    HttpRequest<?> request = HttpRequest.POST("/data_analysis", body)
                                        .contentType(MediaType.MULTIPART_FORM_DATA);

    HttpResponse<String> response = client.toBlocking()
                                          .exchange(request, String.class);

    assertEquals(HttpStatus.OK, response.getStatus());
    assertNotNull(response.body());
    // The response should contain the string representation of the mean calculation result
    assertFalse(response.body()
                        .isEmpty());
  }

  @Test
  void testAnalyzeCsvMulti_EmptyCsv() {
    String csvContent = "";

    MultipartBody body = MultipartBody.builder()
                                      .addPart("file", "empty.csv", csvContent.getBytes(StandardCharsets.UTF_8))
                                      .build();

    HttpRequest<?> request = HttpRequest.POST("/data_analysis", body)
                                        .contentType(MediaType.MULTIPART_FORM_DATA);

    // This might throw an exception or return an error response
    assertThrows(HttpClientResponseException.class, () ->
                         client.toBlocking()
                               .exchange(request, String.class)
                );
  }

  @Test
  void testAnalyzeCsvMulti_WithHeaders() {
    String csvContent = "Column1,Column2,Column3\n1.1,2.2,3.3\n4.4,5.5,6.6";

    MultipartBody body = MultipartBody.builder()
                                      .addPart("file", "with_headers.csv", csvContent.getBytes(StandardCharsets.UTF_8))
                                      .build();

    HttpRequest<?> request = HttpRequest.POST("/data_analysis", body)
                                        .contentType(MediaType.MULTIPART_FORM_DATA);

    HttpResponse<String> response = client.toBlocking()
                                          .exchange(request, String.class);

    assertEquals(HttpStatus.OK, response.getStatus());
    assertNotNull(response.body());
  }

  @Test
  void testAnalyzeCsvMulti_MissingFile() {
    // Test what happens when file parameter is missing - send a regular POST without multipart
    HttpRequest<?> request = HttpRequest.POST("/data_analysis", "")
                                        .contentType(MediaType.TEXT_PLAIN);

    // Should throw HttpClientResponseException since file parameter is required
    assertThrows(HttpClientResponseException.class, () ->
                         client.toBlocking()
                               .exchange(request, String.class)
                );
  }

  @Test
  void testAnalyzeCsvMulti_WrongParameterName() {
    // Test what happens when the file is uploaded with wrong parameter name
    String csvContent = "1.0,2.0,3.0\n4.0,5.0,6.0";

    MultipartBody body = MultipartBody.builder()
                                      .addPart("wrongName", "test.csv", csvContent.getBytes(StandardCharsets.UTF_8))
                                      .build();

    HttpRequest<?> request = HttpRequest.POST("/data_analysis", body)
                                        .contentType(MediaType.MULTIPART_FORM_DATA);

    // Should throw HttpClientResponseException since the controller expects "file" parameter
    assertThrows(HttpClientResponseException.class, () ->
                         client.toBlocking()
                               .exchange(request, String.class)
                );
  }
}
