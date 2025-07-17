package com.skeleton;

import com.skeleton.controllers.SentimentAnalysisController;
import io.micronaut.core.annotation.ReflectionConfig;
import io.micronaut.core.annotation.TypeHint.AccessType;
import kotlin.coroutines.intrinsics.CoroutineSingletons;

@ReflectionConfig(type = SentimentAnalysisController.class, accessType = AccessType.ALL_DECLARED_METHODS)
@ReflectionConfig(type = SentimentAnalysisController.class, accessType = AccessType.ALL_DECLARED_CONSTRUCTORS)
@ReflectionConfig(type = SentimentAnalysisController.class, accessType = AccessType.ALL_DECLARED_FIELDS)
@ReflectionConfig(type = CoroutineSingletons.class, accessType = AccessType.ALL_DECLARED_METHODS)
@ReflectionConfig(type = CoroutineSingletons.class, accessType = AccessType.ALL_DECLARED_CONSTRUCTORS)
@ReflectionConfig(type = CoroutineSingletons.class, accessType = AccessType.ALL_DECLARED_FIELDS)
public class GraalConfig {
}
