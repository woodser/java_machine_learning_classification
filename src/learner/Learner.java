package learner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import learner.features.Feature;

/**
 * Entity that learns from experience.
 * 
 * TODO: source control
 * TODO: ability to compare feature based on range supplied by training set
 * TODO: discard index combinations by informativeness
 * TODO: how to handle e.g. 52 features?
 * 
 * @author woodser
 */
public class Learner {
  
  // the learner's experiences to learn from
  private List<Experience> experiences;
  
  // the number of feature inputs
  private Integer featureSize;
  
  // combination cache
  private static Map<Integer, Set<Set<Integer>>> combinationCache = new HashMap<Integer, Set<Set<Integer>>>();

  /**
   * Constructs a learner with no experiences.
   */
	public Learner() {
		super();
		this.experiences = new ArrayList<Experience>();
	}
	
	/**
	 * Constructs a learner with the given experiences.
	 * 
	 * @param experiences are the learner's experiences
	 */
	public Learner(List<Experience> experiences) {
	  this.experiences = experiences;
	  
	  // enforce uniform feature size
	  for (Experience experience : experiences) {
	    if (featureSize == null) featureSize = experience.getFeatures().size();
	    else if (featureSize != experience.getFeatures().size()) throw new RuntimeException("Features must be uniform size");
	  }
	  if (featureSize == 0) throw new RuntimeException("Experiences must have at least one feature");
	}
	
	/**
	 * Adds an experience to learn from.
	 * 
	 * @param experience is the experience to learn from
	 */
	public void learn(Experience experience) {
	  if (experience.getFeatures().isEmpty()) throw new RuntimeException("Experience must have at least one feature");
	  else if (featureSize == null) featureSize = experience.getFeatures().size();
	  else if (featureSize != experience.getFeatures().size()) throw new RuntimeException("Features must be uniform size");
	  experiences.add(experience);
	}
	
	/**
   * Adds an experience to learn from.
   * 
   * @param features are the features of the experience
   * @param outcome is the outcome of the experience
   */
	public void learn(List<Feature> features, Object outcome) {
	  learn(new Experience(features, outcome));
	}
	
	/**
	 * Optimizes the learner for classification.
	 */
	public void optimize() {
	  if (experiences.isEmpty()) throw new RuntimeException("Learner must have experience");
	  getIndexCombinations(experiences.get(0).getFeatures().size());
	}
	
	/**
	 * Gets the outcome distribution for the given features based on past experience.
	 * 
	 * @param features are the features to get an outcome distribution for
	 * @return Map<Object, Double> is the outcome distribution for the features based on experience
	 */
	public Map<Object, Double> getDistribution(List<Feature> features)  {
		if (experiences.isEmpty()) throw new RuntimeException("Learner must have experience");
		if (featureSize != features.size()) throw new RuntimeException("Features must be uniform size");
		Set<Set<Integer>> combinations = getIndexCombinations(features.size());
		
		// compute outcome distribution for each feature combination
		Map<Set<Integer>, Map<Object, Double>> combinationDistributions = new HashMap<Set<Integer>, Map<Object, Double>>();
		for (Set<Integer> combination : combinations) {
			Map<Object, Double> combinationDistribution = new HashMap<Object, Double>();
			combinationDistributions.put(combination, combinationDistribution);
			for (Experience experience : experiences) {
				Object outcome = experience.getOutcome();
				double similarity = 1;
				for (int i : combination) {
					similarity *= features.get(i).compare(experience.getFeatures().get(i));
					if (similarity == 0) break;
				}
				if (Double.isNaN(similarity)) similarity = 0;	// check for underflow
				Double score = combinationDistribution.get(outcome);
				if (score == null) combinationDistribution.put(outcome, similarity);
				else combinationDistribution.put(outcome, score + similarity);
			}
		}
		
		// scale distributions by feature size
		int featureSum = 0;
		Set<Map<Object, Double>> sizeDistributions = new HashSet<Map<Object, Double>>();
		for (int i = 0; i < features.size(); i++) {
		  // aggregate by feature size
		  Set<Map<Object, Double>> aSizeDistributions = new HashSet<Map<Object, Double>>();
		  for (Set<Integer> combination : combinations) {
        if (combination.size() == i + 1) aSizeDistributions.add(combinationDistributions.get(combination));
      }
		  Map<Object, Double> aSizeDistribution = LearnerUtils.aggregateDistributions(aSizeDistributions);
		  sizeDistributions.add(aSizeDistribution);
		  
		  // scale by feature size
      featureSum += i + 1;
		  double sum = 0;
		  for (Double val : aSizeDistribution.values()) {
		    sum += val;
		  }
		  if (sum == 0) continue;
		  for (Object outcome : aSizeDistribution.keySet()) {
		    aSizeDistribution.put(outcome, aSizeDistribution.get(outcome) / sum * (double) featureSum);
		  }
		}
		
		// aggregate all distributions
	  return LearnerUtils.aggregateDistributions(sizeDistributions);
	}
	
	/**
	 * Gets the most probable outcome classification for the given features based on past experiences.
	 * 
	 * @param features are the features to get an outcome classification for
	 * @param minConfidence is the minimum confidence the distribution must have to return a classification
	 * @return Object is the outcome if the confidence exceeds the threshold
	 */
	public Object getClassification(List<Feature> features, double minConfidence) {
	  if (experiences.isEmpty()) throw new RuntimeException("Learner has no experience");
		Map<Object, Double> distribution = getDistribution(features);
		System.out.println(distribution);
		double confidence = LearnerUtils.getConfidence(distribution);
		if (confidence == 0 || confidence < minConfidence) return null;
		Object best = null;
		for (Object outcome : distribution.keySet()) {
		  if (best == null || distribution.get(outcome) > distribution.get(best)) best = outcome;
		}
		return best;
	}
  
  // ----------------------------- PRIVATE HELPERS ----------------------------
  
  /**
   * Gets all index combinations based on a number of inputs.
   * 
   * @param numInputs defines how many indices there are
   * @return Set<Set<Integer>> are all index combinations
   */
  public static Set<Set<Integer>> getIndexCombinations(int numInputs) {
    if (!combinationCache.containsKey(numInputs)) {
      Set<Integer> indices = new HashSet<Integer>();
      for (int i = 0; i < numInputs; i++) {
        indices.add(i);
      }
      Set<Set<Integer>> powerSet = powerSet(indices);
      powerSet.remove(new HashSet<Integer>());  // remove empty set
      
      // TODO: better way to optimize
      Set<Set<Integer>> toRemove = new HashSet<Set<Integer>>();
      int count = 0;
      for (Set<Integer> set : powerSet) {
        if (count++ % 25 == 0) continue;
        toRemove.add(set);
      }
      powerSet.removeAll(toRemove);
      
      combinationCache.put(numInputs, powerSet);
    }
    return combinationCache.get(numInputs);
  }
  
  /**
   * Builds a power set for the given set.
   * 
   * Credit: http://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
   * 
   * @param originalSet is the set to build a power set for
   * @return Set<Set<T>> is the power set of the original set
   */
  private static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
      Set<Set<T>> sets = new HashSet<Set<T>>();
      if (originalSet.isEmpty()) {
        sets.add(new HashSet<T>());
        return sets;
      }
      List<T> list = new ArrayList<T>(originalSet);
      T head = list.get(0);
      Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
      for (Set<T> set : powerSet(rest)) {
        Set<T> newSet = new HashSet<T>();
        newSet.add(head);
        newSet.addAll(set);
        sets.add(newSet);
        sets.add(set);
      }   
      return sets;
  }
}
