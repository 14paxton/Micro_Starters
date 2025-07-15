package com.skeleton;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;
import jakarta.inject.Inject;

import java.util.Map;

@Controller("/sentiment")
public class SentimentAnalysisController {

  @Inject
  VaderSentimentModule vaderSentiment;

  @Get
  @View("sentiment")
  public void index() {
    // Return empty model or add logging
  }

  @Get(value = "/analyze")
  @ExecuteOn(TaskExecutors.BLOCKING)
  public Map<String, Double> analyze(@QueryValue String text) {
    if (text == null || text.trim()
                            .isEmpty()) {
      throw new IllegalArgumentException("Text parameter is required");
    }

    try {
      VaderSentimentModule.SentimentIntensityAnalyzer sentimentAnalysis = vaderSentiment.SentimentIntensityAnalyzer();
      return sentimentAnalysis.polarity_scores(text);
    }
    catch (Exception e) {
      throw new RuntimeException("Error analyzing sentiment: " + e.getMessage(), e);
    }
  }
}