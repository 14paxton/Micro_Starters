package com.skeleton.services;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class TestImageServiceTest {

  @Inject
  TestImageService testImageService;

  @Test
  void testModuleInjection() {
    assertNotNull(testImageService, "TestImageModule should be injected");
  }

  @Test
  void testCreateTestImage() throws IOException {
    String date = LocalDateTime.now()
                               .toString();
    Files.createDirectories(java.nio.file.Paths.get("./testOutput"));
    String filePath = String.format("./testOutput/test_image_%s.png", date);

    assertDoesNotThrow(() -> testImageService.createTestImage(filePath),
                       "createTestImage should execute without throwing exceptions");
  }
}