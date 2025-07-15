package com.skeleton;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false, environments = "test")
@Property(name = "micronaut.server.port", value = "-1")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestImageModuleTest {

  @Inject
  TestImageModule testImageModule;

  @Test
  void testModuleInjection() {
    assertNotNull(testImageModule, "TestImageModule should be injected");
  }

  @Test
  void testCreateTestImage() {
    assertDoesNotThrow(() -> testImageModule.create_test_image(),
                       "create_test_image should execute without throwing exceptions");
  }
}