package com.skeleton;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;

@Controller("/hello")
class HelloController {

  @Inject
  HelloModule hello;

  @Get
  @Produces(MediaType.TEXT_PLAIN)
  String index() {
    return hello.hello("World");
  }
}
