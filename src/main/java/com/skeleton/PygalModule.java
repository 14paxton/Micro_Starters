package com.skeleton;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

@GraalPyModule("pygal")
public interface PygalModule {
  StackedBar StackedBar();

  interface StackedBar {
    void add(String title, int[] i);

    PygalModule.Svg render();
  }

  interface Svg {
    String decode();
  }
}