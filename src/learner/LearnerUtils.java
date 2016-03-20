package learner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearnerUtils {

  /**
   * Computes confidence for the given distribution.
   * 
   * Confidence is determined by how decisive the best answer is over the next best answer.
   * 
   * @param distribution is an outcome distribution
   * @return double indicates how confident this distribution is
   */
  public static double getConfidence(Map<Object, Double> distribution) {
    double sum = 0;
    for (Double val : distribution.values()) {
      sum += val;
    }
    if (sum == 0) return 0;
    if (distribution.keySet().size() == 1) return 1;
    List<Double> values = new ArrayList<Double>(distribution.values());
    Collections.sort(values);
    Collections.reverse(values);
    return values.get(0) / sum - values.get(1) / sum;
  }
  
  /**
   * Aggregates a set of outcome distributions into a single distribution based on their confidence.
   * 
   * @param distributions are the outcome distributions to aggregate
   * @return Map<Object, Double> is the aggregated distribution
   */
  public static Map<Object, Double> aggregateDistributions(Collection<Map<Object, Double>> distributions) {
    Map<Object, Double> aggregated = new HashMap<Object, Double>();
    for (Map<Object, Double> distribution : distributions) {
      double confidence = getConfidence(distribution);
      for (Object outcome : distribution.keySet()) {
        Double score = aggregated.get(outcome);
        if (score == null) aggregated.put(outcome, confidence * distribution.get(outcome));
        else aggregated.put(outcome, score + confidence * distribution.get(outcome));
      }
    }
    return aggregated;
  }
}
