package ml;

/**
 * Represents an exception working with machine learning.
 * 
 * @author woodser
 */
public class MlException extends RuntimeException {

  private static final long serialVersionUID = -949711410379407128L;

  public MlException(String msg) {
    super(msg);
  }
}
