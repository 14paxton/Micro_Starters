package com.skeleton.modules;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

import java.util.Map;

@GraalPyModule("vader_sentiment.vader_sentiment") public interface VaderSentimentModule {
  SentimentIntensityAnalyzer SentimentIntensityAnalyzer();

  interface SentimentIntensityAnalyzer {
    Map<String, Double> polarity_scores(String text);
  }

}
