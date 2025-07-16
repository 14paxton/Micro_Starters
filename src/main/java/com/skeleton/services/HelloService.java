package com.skeleton.services;

import com.skeleton.modules.HelloModule;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class HelloService {

  @Inject
  HelloModule helloModule;

  public String hello(String text) {
    return helloModule.hello(text);
  }
}
