package com.skeleton;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

@Controller("/pygal")
class PygalController {
  @Inject
  PygalModule pygal;

  @ExecuteOn(TaskExecutors.BLOCKING)
  @Get
  @Produces("image/svg+xml")
  public String index() {
    PygalModule.StackedBar stackedBar = pygal.StackedBar();
    stackedBar.add("Fibonacci", new int[]{0, 1, 1, 2, 3, 5, 8});
    PygalModule.Svg svg = stackedBar.render();
    return svg.decode();
  }

}
