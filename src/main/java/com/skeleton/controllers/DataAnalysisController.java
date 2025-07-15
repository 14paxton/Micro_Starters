package com.skeleton.controllers;

import com.skeleton.services.DataAnalysisService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

@Controller
public class DataAnalysisController {
  private final DataAnalysisService dataAnalysisService;

  public DataAnalysisController(DataAnalysisService dataAnalysisService) {
    this.dataAnalysisService = dataAnalysisService;
  }

  @Get
  @View("index")
  public void index() {
  }

  @Post(value = "/data_analysis", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.TEXT_PLAIN)
  @ExecuteOn(TaskExecutors.IO)
  String analyzeCsvMulti(StreamingFileUpload file) {
    try {
      return dataAnalysisService.mean(file);
    }
    catch (Exception e) {
      throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
