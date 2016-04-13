package test.learner;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import learner.Learner;
import learner.features.NominalFeature;

/**
 * Test the learner.
 * 
 * @author woodser
 */
public class TestLogicOperators extends Learner {

  @Test
  public void testSimpleXOR() {
    Learner learner = new Learner();
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("false")}), Outcome.B);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("false")}), Outcome.A);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("true")}), Outcome.A);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("true")}), Outcome.B);
    assertEquals(Outcome.B, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("false")}), 0).getFirst());
    assertEquals(Outcome.A, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("false")}), 0).getFirst());
    assertEquals(Outcome.A, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("true")}), 0).getFirst());
    assertEquals(Outcome.B, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("true")}), 0).getFirst());
  }
  
  @Test
  public void testTrickyXOR() {
    Learner learner = new Learner();
    for (int i = 0; i < 100000; i++) {
          learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("false")}), Outcome.B);
    }
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("false")}), Outcome.A);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("true")}), Outcome.A);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("true")}), Outcome.B);
    assertEquals(Outcome.B, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("false")}), 0).getFirst());
    assertEquals(Outcome.A, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("false")}), 0).getFirst());
    assertEquals(Outcome.A, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("true")}), 0).getFirst());
    assertEquals(Outcome.B, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("true")}), 0).getFirst());
  }
  
  @Test
  public void TestAnd() {
    Learner learner = new Learner();
    for (int i = 0; i < 100000; i++) {
          learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("false")}), false);
    }
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("true")}), false);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("false")}), false);
    learner.learn(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("true")}), true);
    assertEquals(false, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("false")}), 0).getFirst());
    assertEquals(false, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("false")}), 0).getFirst());
    assertEquals(false, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("false"), new NominalFeature("true")}), 0).getFirst());
    assertEquals(true, learner.getClassification(Arrays.asList(new NominalFeature[] {new NominalFeature("true"), new NominalFeature("true")}), 0).getFirst());
  }
  
  private enum Outcome {
    A, B
  }
}
