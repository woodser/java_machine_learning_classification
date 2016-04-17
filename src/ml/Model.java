package ml;

import java.util.Collection;

/**
 * Defines a logistic regression machine learning model interface.
 * 
 * @author woodser
 */
public interface Model {

  /**
   * Imports an existing model, replacing the state of this model.
   * 
   * @param bytes is the model to import represented as a byte[]
   */
  public void load(byte[] bytes);
  
  /**
   * Exports this model to a byte[].
   * 
   * @return byte[] is the state of this model
   */
  public byte[] export();
  
  /**
   * Trains this model with its existing instances.
   */
  public void train();
  
  /**
   * Adds a training instance to this model.
   * 
   * @param instance is the training instance to add
   */
  public void addTrainingInstance(Instance instance);
  
  /**
   * Adds training instances to this model.
   * 
   * @param instances are the training instances to add
   */
  public void addTrainingInstances(Collection<Instance> instances);
  
  /**
   * Classifies an instance, altering its internal state.
   * 
   * @param instance is the instance to classify
   * @return Instance is a reference to the classified instance for convenience
   */
  public Instance classify(Instance instance);
  
  /**
   * Classifies instances, altering their internal state.
   * 
   * @param instances are the instances to classify
   * @return Collection<Instance> is a reference to the classified instances for convenience
   */
  public Collection<Instance> classify(Collection<Instance> instances);
}
