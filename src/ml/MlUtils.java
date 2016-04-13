package ml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Collection of utilities for working with logistic regression machine learning.
 * 
 * @author woodser
 */
public class MlUtils {
  
  /**
   * Loads instance data from a CSV file.
   * 
   * @param file is the CSV file to load instances from
   * @param headers indicate whether or not to treat the first row as headers
   * @return List<Instances> are the imported instances
   * @throws IOException 
   * @throws FileNotFoundException 
   */
  public static List<Instance> loadInstances(File file, boolean headers) throws FileNotFoundException, IOException {
    return getInstances(importCsv(file), headers);
  }

  /**
   * Converts a table of strings into a list of instances.
   * 
   * The last column is assumed to be the classification.
   * 
   * @param table includes rows and columns of data
   * @param headers indicates if the first row should be treated as headers
   * @return List<Instance> are the instances created from the table
   */
  public static List<Instance> getInstances(List<List<String>> table, boolean headers) {
    List<Instance> instances = new ArrayList<Instance>();
    List<String> headerRow = headers ? table.get(0) : null;
    for (int rowIdx = 0; rowIdx < table.size(); rowIdx++) {
      if (rowIdx == 0 && headers) continue;
      List<String> row = table.get(rowIdx);
      Instance instance = new Instance(new HashMap<String, Object>());
      instances.add(instance);
      for (int colIdx = 0; colIdx < table.get(rowIdx).size(); colIdx++) {
        if (colIdx < row.size() - 1) {
          Object value = getValue(row.get(colIdx));
          if (value == null) continue;
          String featureName = headers ? headerRow.get(colIdx) : "Feature " + colIdx;
          instance.getFeatures().put(featureName, value);
        } else {
          instance.setClassification(row.get(colIdx));
        }
      }
    }
    return instances;
  }
  
  /**
   * Imports a CSV file.
   * 
   * @param file points to the CSV file
   * @return List<List<String>> are the rows and columns of the CSV file
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static List<List<String>> importCsv(File file) throws FileNotFoundException, IOException {
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
   * Returns a value for the given string which will be Double or String.
   * 
   * @param str is the string to get a value for
   * @return Object is Double or String depending on the contents of the string
   */
  private static Object getValue(String str) {
    Double val = getDouble(str);
    return val == null ? str : val;
  }
  
  /**
   * Returns a Double for the given string if it represents a number.
   * 
   * @param str is the string to get a Double for
   * @return Double if the string represents a number, null otherwise
   */
  private static Double getDouble(String val) {
    try {
      return Double.parseDouble(val);
    } catch (Exception e) {
      return null;
    }
  }
}
