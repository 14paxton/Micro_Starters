package com.skeleton;

@io.micronaut.graal.graalpy.annotations.GraalPyModule("hello")
public interface HelloModule {
    String hello(String txt);
}