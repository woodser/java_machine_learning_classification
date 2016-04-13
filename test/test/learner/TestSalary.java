package test.learner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import learner.Experience;
import learner.Learner;
import learner.features.ContinuousFeature;
import learner.features.Feature;
import learner.features.NominalFeature;
import learner.utils.Pair;

/**
 * Main class to train and test a Learner.
 * 
 * @author woodser
 */
public class TestSalary {
	
	private static final double PERCENT_TRAINING = .64;
	private static final double MIN_CONFIDENCE = 0;
	private static final double REPEAT = 1;

	/**
	 * Program entry.
	 * 
	 * @param args are not used
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// read training / test data
		List<List<String>> rows = importCsv(new File("resources/salary.csv"));
		
		// determine number of training instances vs test instances
		int numTraining = (int) Math.round(PERCENT_TRAINING * rows.size());
		
		// track precision, accuracy, and recall
		int right = 0, wrong = 0, unknown = 0;
		
		// perform experiment many times
		long start = System.currentTimeMillis();
		for (int i = 0; i < REPEAT; i++) {
		
			// randomize training and test set
			Collections.shuffle(rows, new Random(System.nanoTime()));
			
			// train a Learner with training instances
			long time = System.currentTimeMillis();
			Learner learner = new Learner();
			for (int j = 0; j < numTraining; j++) {
				learner.learn(getExperience(rows.get(j)));
			}
			System.out.println("Done collecting experience: " + (System.currentTimeMillis() - time) + " ms");
			
			// optimize the learner
			time = System.currentTimeMillis();
			learner.optimize();
			System.out.println("Done optimizing: " + (System.currentTimeMillis() - time) + " ms");
			
			// test Learner's outcome classification
			int up = 0;
			int count = 0;
			for (int j = numTraining; j < rows.size(); j++) {
			  // get results
				Object actual = getOutcome(rows.get(j));
				Pair<Object, Double> classification = learner.getClassification(getFeatures(rows.get(j)), MIN_CONFIDENCE);
				Object expected = classification.getFirst();
				
				// measure results
				if (expected == null) unknown++;
				else if (expected.equals(actual)) right++;
				else wrong++;
				double recall = (double) (right + wrong) / (double) (right + wrong + unknown);
		    double precision = (double) right / (double) (right + wrong);
		    double accuracy = (double) right / (double) (right + wrong + unknown);
		    
		    // print results
		    if (expected != null) {
		      if (((String) expected).trim().equals("<=50K")) up++;
	        count++;
	        System.out.println("Actual: " + actual + ", expected: " + expected + ", " + (float) up / (float) count);
		    }
				System.out.println("Right: " + right + ", wrong: " + wrong + ", unknown: " + unknown);
		    System.out.println("Precision: " + precision + ", accuracy: " + accuracy + ", recall: " + recall);
			}
		}
		
		// print results
		double recall = (double) (right + wrong) / (double) (right + wrong + unknown);
		double precision = (double) right / (double) (right + wrong);
		double accuracy = (double) right / (double) (right + wrong + unknown);
		System.out.println("Done in " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Right: " + right + ", wrong: " + wrong + ", unknown: " + unknown);
		System.out.println("Precision: " + precision + ", accuracy: " + accuracy + ", recall: " + recall);
	}
	
	/**
	 * Imports a CSV file.
	 * 
	 * @param file points to the CSV file
	 * @return List<List<String>> are the rows and values of the CSV file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static List<List<String>> importCsv(File file) throws FileNotFoundException, IOException {
		List<List<String>> rows = new ArrayList<List<String>>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	rows.add(Arrays.asList(line.split(",")));
		    }
		}
		return rows;
	}
	
	/**
	 * Gets a training instance from the given row.
	 * 
	 * @param row is a row in the CSV training/test data
	 * @return Experience is a training instance comprised of features and an outcome
	 */
	private static Experience getExperience(List<String> row) {
		return new Experience(getFeatures(row), getOutcome(row));
	}
	
	/**
	 * Extracts features from the given row.
	 * 
	 * @param row is a row in the CSV training/test data
	 * @return List<Feature> are the extracted features
	 */
	private static List<Feature> getFeatures(List<String> row) {
		List<Feature> features = new ArrayList<Feature>();
    features.add(new ContinuousFeature(Double.parseDouble(row.get(0))));
		features.add(new NominalFeature(row.get(1)));
		features.add(new ContinuousFeature(Double.parseDouble(row.get(2))));
    features.add(new NominalFeature(row.get(3)));
    features.add(new ContinuousFeature(Double.parseDouble(row.get(4))));
    features.add(new NominalFeature(row.get(5)));
    features.add(new NominalFeature(row.get(6)));
    features.add(new NominalFeature(row.get(7)));
    features.add(new NominalFeature(row.get(8)));
    features.add(new NominalFeature(row.get(9)));
    features.add(new ContinuousFeature(Double.parseDouble(row.get(10))));
    features.add(new ContinuousFeature(Double.parseDouble(row.get(11))));
    features.add(new ContinuousFeature(Double.parseDouble(row.get(12))));
    features.add(new NominalFeature(row.get(13)));
		return features;
	}
	
	/**
	 * Returns the outcome of the given row. 
	 * 
	 * @param row is a row in the CSV training/test data
	 * @return Outcome is the outcome indicated in the row
	 */
	private static Object getOutcome(List<String> row) {
	  return row.get(14);
	}
}
