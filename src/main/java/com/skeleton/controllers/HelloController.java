package com.skeleton.controllers;

import com.skeleton.services.HelloService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/hello")
class HelloController {

  private final HelloService helloService;

  public HelloController(HelloService helloService) {
    this.helloService = helloService;
  }

  @Get
  @Produces(MediaType.TEXT_PLAIN)
  String index() {
    return helloService.hello("World");
  }
}
