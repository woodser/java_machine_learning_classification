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

/**
 * Main class to train and test a Learner.
 * 
 * @author woodser
 */
public class TestAbalone {
	
	private static final double PERCENT_TRAINING = .75;
	private static final double MIN_CONFIDENCE = .15;
	private static final double REPEAT = 3;
	private static final int RING_LENIENCY = 1;

	/**
	 * Program entry.
	 * 
	 * @param args are not used
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// read training / test data
		List<List<String>> rows = importCsv(new File("resources/abalone.csv"));
		
		// determine number of training instances vs test instances
		int numTraining = (int) Math.round(PERCENT_TRAINING * rows.size());
		
		// track precision, accuracy, and recall
		int right = 0, wrong = 0, unknown = 0;
		
		// perform experiment many times
		long time = 0;
		for (int i = 0; i < REPEAT; i++) {
		
			// randomize training and test set
			Collections.shuffle(rows, new Random(System.nanoTime()));
			
			// train a Learner with training instances
			Learner learner = new Learner();
			for (int j = 0; j < numTraining; j++) {
				learner.learn(getExperience(rows.get(j)));
			}
			
			// test Learner's outcome classification
			for (int j = numTraining; j < rows.size(); j++) {
				Integer actual = getOutcome(rows.get(j));
				long start = System.currentTimeMillis();
				Integer expected = (Integer) learner.getClassification(getFeatures(rows.get(j)), MIN_CONFIDENCE);
				time += System.currentTimeMillis() - start;
				if (expected == null) unknown++;
				else if (Math.abs(actual - expected) <= RING_LENIENCY) right++;
				else wrong++;
			}
		}
		
		// print results
		double recall = (double) (right + wrong) / (double) (right + wrong + unknown);
		double precision = (double) right / (double) (right + wrong);
		double accuracy = (double) right / (double) (right + wrong + unknown);
		System.out.println("Done in " + time + " ms");
		System.out.println("Right: " + right + ", wrong: " + wrong + ", unknown: " + unknown);
		System.out.println("Accuracy: " + accuracy + ", precision: " + precision + ", recall: " + recall);
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
		String sex = row.get(0);
		double length = Double.parseDouble(row.get(1));
    double diameter = Double.parseDouble(row.get(2));
    double height = Double.parseDouble(row.get(3));
//    double whole_height = Double.parseDouble(row.get(4));
//    double shucked_height = Double.parseDouble(row.get(5));
    double viscera_weight = Double.parseDouble(row.get(6));
    double shell_weight = Double.parseDouble(row.get(7));
		List<Feature> features = new ArrayList<Feature>();
		features.add(new NominalFeature(sex));
		features.add(new ContinuousFeature(length));
		features.add(new ContinuousFeature(diameter));
		features.add(new ContinuousFeature(height));
//		features.add(new ContinuousFeature(whole_height));
//		features.add(new ContinuousFeature(shucked_height));
		features.add(new ContinuousFeature(viscera_weight));
		features.add(new ContinuousFeature(shell_weight));
		return features;
	}
	
	/**
	 * Returns the outcome of the given row. 
	 * 
	 * @param row is a row in the CSV training/test data
	 * @return Outcome is the outcome indicated in the row
	 */
	private static Integer getOutcome(List<String> row) {
	  return Integer.parseInt(row.get(8));
	}
}
