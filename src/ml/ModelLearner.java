package ml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import learner.Experience;
import learner.Learner;
import learner.features.ContinuousFeature;
import learner.features.Feature;
import learner.features.NominalFeature;
import learner.utils.Pair;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Implements a model backed by a Learner.
 * 
 * @author woodser
 */
public class ModelLearner implements Model {
  
  private Learner learner;
  
  public ModelLearner() {
    this.learner = new Learner();
  }

  @Override
  public void load(byte[] bytes) {
    this.learner = SerializationUtils.deserialize(bytes);
  }

  @Override
  public byte[] export() {
    return SerializationUtils.serialize(learner);
  }

  @Override
  public void train() {
    // nothing to do
  }

  @Override
  public void addTrainingInstance(Instance instance) {
    learner.learn(getExperience(instance));
  }

  @Override
  public void addTrainingInstances(Collection<Instance> instances) {
    for (Instance instance : instances) addTrainingInstance(instance);
  }

  @Override
  public Instance classify(Instance instance) {
    Experience experience = getExperience(instance);
    Map<Object, Double> distribution = learner.getDistribution(experience.getFeatures());
    instance.setDistribution(distribution);
    Pair<Object, Double> classification = learner.getClassification(distribution, 0);
    instance.setClassification(classification.getFirst());
    instance.setProbability(classification.getSecond());
    return instance;
  }

  @Override
  public Collection<Instance> classify(Collection<Instance> instances) {
    for (Instance instance : instances) classify(instance);
    return instances;
  }
  
  /**
   * Converts an instance to an experience.
   * 
   * @param instance is the instance to convert
   * @return Experience is the converted instance
   */
  public static Experience getExperience(Instance instance) {
    TreeSet<String> treeSet = new TreeSet<String>(instance.getFeatures().keySet());
    List<Feature> features = new ArrayList<Feature>();
    for (String featureName : treeSet) {
      Object val = instance.getFeatures().get(featureName);
      if (val instanceof Double) features.add(new ContinuousFeature((double) val));
      else features.add(new NominalFeature((String) val));
    }
    return new Experience(features, instance.getClassification());
  }
  
  /**
   * Converts an experience into an instance.
   * 
   * @param experience is the experience to convert
   * @return Instance is the converted experience
   */
  public static Instance getInstance(Experience experience) {
    // build features
    Map<String, Object> features = new HashMap<String, Object>();
    for (int i = 0; i < experience.getFeatures().size(); i++) {
      Object value = null;
      Feature feature = experience.getFeatures().get(i);
      if (feature instanceof ContinuousFeature) value = ((ContinuousFeature) feature).getVal();
      else if (feature instanceof NominalFeature) value = ((NominalFeature) feature).getVal();
      else throw new MlException("Unrecognized feature: " + feature.getClass());
      features.put("Feature " + i, value);
    }
    return new Instance(features, experience.getOutcome());
  }
}
