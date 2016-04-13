package ml;

import java.util.Map;

/**
 * Represents a logistic regression machine learning instance.
 * 
 * An instance is described by a set of features, each with their own String
 * or Double value.  An instance may have a final classification and associated
 * probability, and may also have a distribution of possible classifications
 * and their probabilities.
 * 
 * @author woodser
 */
public class Instance {

  private Map<String, Object> features;     // features describing the instance
  private Object classification;            // final classification of the instance
  private Double probability;               // probability of the final classification
  private Map<Object, Double> distribution; // probability of possible classifications
  
  public Instance(Map<String, Object> features) {
    this.features = features;
  }
  
  public Instance(Map<String, Object> features, Object classification) {
    this.features = features;
    this.classification = classification;
  }
  
  public Map<String, Object> getFeatures() {
    return features;
  }
  
  public void setFeatures(Map<String, Object> features) {
    this.features = features;
  }
  
  public Object getClassification() {
    return classification;
  }
  
  public void setClassification(Object classification) {
    this.classification = classification;
  }
  
  public Double getProbability() {
    return probability;
  }
  
  public void setProbability(Double probability) {
    this.probability = probability;
  }

  public Map<Object, Double> getDistribution() {
    return distribution;
  }

  public void setDistribution(Map<Object, Double> distribution) {
    this.distribution = distribution;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Features=" + features);
    sb.append(", classification=" + classification);
    sb.append(", confidence=" + probability);
    sb.append(", distribution=" + distribution);
    return sb.toString();
  }
}
