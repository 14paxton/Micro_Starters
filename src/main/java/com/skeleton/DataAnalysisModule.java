package com.skeleton;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

import java.util.List;

@GraalPyModule("data_analysis")
public interface DataAnalysisModule {
    List<Double> mean(String csv);
}
