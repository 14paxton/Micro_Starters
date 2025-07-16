package com.skeleton.services;

import com.skeleton.modules.VaderSentimentModule;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class SentimentAnalysisService {
  @Inject
  VaderSentimentModule vaderSentimentModule;

  public Map<String, Double> polarityScores(String text){
    try {
      VaderSentimentModule.SentimentIntensityAnalyzer sentimentAnalysis = vaderSentimentModule.SentimentIntensityAnalyzer();
      return sentimentAnalysis.polarity_scores(text);
    }
    catch (Exception e) {
      throw new RuntimeException("Error analyzing sentiment: " + e.getMessage(), e);
    }
  }
}
