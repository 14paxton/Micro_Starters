package com.skeleton;

import com.skeleton.controllers.SentimentAnalysisController;

@io.micronaut.core.annotation.ReflectionConfig(type = SentimentAnalysisController.class, accessType = io.micronaut.core.annotation.TypeHint.AccessType.ALL_DECLARED_METHODS)
@io.micronaut.core.annotation.ReflectionConfig(type = SentimentAnalysisController.class, accessType = io.micronaut.core.annotation.TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS)
@io.micronaut.core.annotation.ReflectionConfig(type = SentimentAnalysisController.class, accessType = io.micronaut.core.annotation.TypeHint.AccessType.ALL_DECLARED_FIELDS)
public class GraalConfig {
}
