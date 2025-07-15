package com.skeleton.services;

import com.skeleton.modules.TestImageModule;
import jakarta.inject.Singleton;

@Singleton
public class TestImageService {

  private final TestImageModule testImageModule;

  public TestImageService(TestImageModule testImageModule) {
    this.testImageModule = testImageModule;
  }

  public void createTestImage(String output_path) {
    testImageModule.create_test_image(output_path);
  }
}
