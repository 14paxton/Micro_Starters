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

@Singleton @Replaces(GraalPyContextBuilderFactory.class)
public class CustomGraalPyContextBuilderFactory implements GraalPyContextBuilderFactory {
  @Value("${graalpy.vfs.resource-directory}")
  private String resourceDirectory;

  private static final Logger LOG = LoggerFactory.getLogger(CustomGraalPyContextBuilderFactory.class);

  @Override
  public Context.Builder createBuilder() {
    LOG.debug("creating custom GraalPyContext");
    Engine engine = Engine.create();

    VirtualFileSystem vfs = VirtualFileSystem.newBuilder()
                                             .resourceDirectory(resourceDirectory)
                                             .build();

    return GraalPyResources.contextBuilder(vfs)
                           .engine(engine)
                           .allowNativeAccess(true)
                           .allowCreateProcess(true)
                           .allowExperimentalOptions(true)
                           .option("python.IsolateNativeModules", "false")
                           .option("python.WarnExperimentalFeatures", "false");

  }
}