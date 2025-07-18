package com.skeleton.services;

import com.skeleton.modules.DataAnalysisModule;
import io.micronaut.http.multipart.StreamingFileUpload;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class DataAnalysisService {

  @Inject
  DataAnalysisModule dataAnalysisModule;

  public String mean(StreamingFileUpload file) throws IOException {
    try (var inputStream = file.asInputStream()) {
      byte[] bytes = inputStream.readAllBytes();
      String csvContent = new String(bytes, StandardCharsets.UTF_8);
      return dataAnalysisModule.mean(csvContent).toString();
    }
  }

}
