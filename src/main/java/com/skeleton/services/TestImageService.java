package com.skeleton.services;

import com.skeleton.modules.TestImageModule;
import jakarta.inject.Singleton;

@Singleton
public class TestImageService implements TestImageModule {

  public void createTestImage() {
    create_test_image();
  }

  @Override
  public void create_test_image() {
    // Implementation will be provided by GraalPy
  }
}
