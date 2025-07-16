package com.skeleton.controllers;

import com.skeleton.services.PygalService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

@Controller("/pygal")
class PygalController {

  @Inject
  PygalService pygalService;

  @ExecuteOn(TaskExecutors.BLOCKING)
  @Get
  @Produces("image/svg+xml")
  public String index() {
    return pygalService.createSVG();
  }

}
