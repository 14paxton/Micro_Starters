package com.skeleton;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
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

  }

  @Get(value = "/analyze")
  @ExecuteOn(TaskExecutors.BLOCKING)
  public Map<String, Double> answer(String text) {
    VaderSentimentModule.SentimentIntensityAnalyzer sentimentAnalysis = vaderSentiment.SentimentIntensityAnalyzer();
    return sentimentAnalysis.polarity_scores(text);
  }
}
