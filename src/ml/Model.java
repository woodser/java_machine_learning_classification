package ml;

import java.util.Collection;

/**
 * Defines a logistic regression machine learning model interface.
 * 
 * @author woodser
 */
public interface Model {

  public void load(byte[] bytes);
  
  public byte[] export();
  
  public void train();
  
  public void addTrainingInstance(Instance instance);
  
  public void addTrainingInstances(Collection<Instance> instances);
  
  public void classify(Instance instance);
  
  public void classify(Collection<Instance> instances);
}
