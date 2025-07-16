package com.skeleton;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@Tag("smoke")
class ChangeMeTest {
  private final EmbeddedApplication<?> application;

  ChangeMeTest(EmbeddedApplication<?> application) {
    this.application = application;
  }

  @Test
  void applicationStarts() {
    assertTrue(application.isRunning(), "Application should be running");
  }
}
