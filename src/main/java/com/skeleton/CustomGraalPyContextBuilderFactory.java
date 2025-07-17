package com.skeleton;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Value;
import io.micronaut.graal.graalpy.GraalPyContextBuilderFactory;
import jakarta.inject.Singleton;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.python.embedding.GraalPyResources;
import org.graalvm.python.embedding.VirtualFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Replaces(GraalPyContextBuilderFactory.class)
public class CustomGraalPyContextBuilderFactory implements GraalPyContextBuilderFactory {
  @Value("${graalpy.vfs.resource-directory}")
  String resourceDirectory;
  private static final Logger LOG = LoggerFactory.getLogger(CustomGraalPyContextBuilderFactory.class);

  @Override
  public Context.Builder createBuilder() {
    LOG.debug("***getting custom GraalPyContext");

    return GraalPyResources.contextBuilder(VirtualFileSystem.newBuilder()
                                                            .resourceDirectory(resourceDirectory)
                                                            .build())
                           .engine(Engine.create())
                           .allowNativeAccess(true)
                           .allowCreateProcess(true)
                           .allowExperimentalOptions(true)
                           .option("python.IsolateNativeModules", "false")
                           .option("python.NativeModules", "true")
                           .option("python.WarnExperimentalFeatures", "false");
  }
}