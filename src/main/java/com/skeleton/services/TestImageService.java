package com.skeleton.services;

import com.skeleton.modules.TestImageModule;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
class TestImageService {

  @Inject
  TestImageModule testImageModule;

  public void createTestImage() {
    testImageModule.create_test_image();
  }
}
