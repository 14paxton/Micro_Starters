package com.skeleton;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

@GraalPyModule("pygal")
interface PygalModule {
  StackedBar StackedBar();

  interface StackedBar {
    void add(String title, int[] i);

    com.skeleton.PygalModule.Svg render();
  }

  interface Svg {
    String decode();
  }
}