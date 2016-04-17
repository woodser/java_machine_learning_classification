package learner;

import java.io.Serializable;
import java.util.List;

import learner.features.Feature;

/**
 * Represents an experience that a learner can learn from.
 * 
 * @author woodser
 */
public class Experience implements Serializable {

  private static final long serialVersionUID = -1794404088095011912L;
  private List<Feature> features;
  private Object outcome;
  
  public Experience(List<Feature> features, Object outcome) {
    super();
    this.features = features;
    this.outcome = outcome;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public Object getOutcome() {
    return outcome;
  }

  public void setOutcome(Object outcome) {
    this.outcome = outcome;
  }
}
