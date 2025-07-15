package com.skeleton.services;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

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
  void testCreateTestImage() {
    assertDoesNotThrow(() -> testImageService.createTestImage(),
                       "create_test_image should execute without throwing exceptions");
  }
}