package com.skeleton.services;

import com.skeleton.modules.PygalModule;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class PygalService {
  @Inject
  PygalModule pygal;

  public String createSVG() {
    PygalModule.StackedBar stackedBar = pygal.StackedBar();
    stackedBar.add("Fibonacci", new int[]{0, 1, 1, 2, 3, 5, 8});
    PygalModule.Svg svg = stackedBar.render();
    return svg.decode();
  }
}
