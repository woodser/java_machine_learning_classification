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

/**
 * Main class to train and test a Learner.
 * 
 * @author woodser
 */
public class TestIrisFlowers {
	
	private static final double PERCENT_TRAINING = .8;
	private static final double MIN_CONFIDENCE = 0;
	private static final double REPEAT = 100;

	/**
	 * Program entry.
	 * 
	 * @param args are not used
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// read training / test data
		List<List<String>> rows = importCsv(new File("resources/iris_flowers.csv"));
		
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
			Learner learner = new Learner();
			for (int j = 0; j < numTraining; j++) {
				learner.learn(getExperience(rows.get(j)));
			}
			
			// test Learner's outcome classification
			for (int j = numTraining; j < rows.size(); j++) {
				Object actual = getOutcome(rows.get(j));
				Object expected = learner.getClassification(getFeatures(rows.get(j)), MIN_CONFIDENCE);
				if (expected == null) unknown++;
				else if (expected.equals(actual)) right++;
				else wrong++;
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
		double col0 = Double.parseDouble(row.get(0));
		double col1 = Double.parseDouble(row.get(1));
		double col2 = Double.parseDouble(row.get(2));
		double col3 = Double.parseDouble(row.get(3));
		List<Feature> features = new ArrayList<Feature>();
		features.add(new ContinuousFeature(col0 / col1));
		features.add(new ContinuousFeature(col2 / col3));
		features.add(new ContinuousFeature((col0 / col1) / (col2 / col3)));
		features.add(new ContinuousFeature(col0));
		features.add(new ContinuousFeature(col1));
		features.add(new ContinuousFeature(col2));
		features.add(new ContinuousFeature(col3));
		return features;
	}
	
	/**
	 * Returns the outcome of the given row. 
	 * 
	 * @param row is a row in the CSV training/test data
	 * @return Outcome is the outcome indicated in the row
	 */
	private static Object getOutcome(List<String> row) {
	  return row.get(4);
	}
}
