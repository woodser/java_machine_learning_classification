package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ml.Instance;
import ml.MlUtils;
import ml.Model;
import ml.ModelLearner;

public class Main {
  
  private static final String CSV_PATH = "resources/abalone.csv";
  //private static final String CSV_PATH = "resources/iris_flowers.csv";
  //private static final String CSV_PATH = "resources/salary.csv";

  public static void main(String[] args) throws FileNotFoundException, IOException {
    // load instances and train
    List<Instance> instances = MlUtils.loadInstances(new File(CSV_PATH), false);
    Model model = new ModelLearner();
    model.addTrainingInstances(instances);
    model.train();
    
    // test import/export
    long start = System.currentTimeMillis();
    model.load(model.export());
    System.out.println("Time to export/load: " + (System.currentTimeMillis() - start));
    
    // test model performance
    int right = 0;
    int wrong = 0;
    for (Instance instance : instances) {
      Instance test = new Instance(instance.getFeatures());
      model.classify(test);
      //if (test.getProbability() < .75) continue;
      if (test.getClassification().equals(instance.getClassification())) right++;
      else wrong++;
    }
    System.out.println("Final accuracy: " + (double) right / (double) (right + wrong));
  }
}