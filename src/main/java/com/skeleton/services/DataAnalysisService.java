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
    return dataAnalysisModule.mean(new String(file.asInputStream()
                                                  .readAllBytes(), StandardCharsets.UTF_8))
                             .toString();
  }

}
