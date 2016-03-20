package learner.features;

/**
 * Represents a nominal feature.
 * 
 * @author woodser
 */
public class NominalFeature implements Feature {
	
	private String val;

	public NominalFeature(String val) {
		super();
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	// TODO: comparison of nulls
	@Override
	public double compare(Feature feature) {
		if (!(feature instanceof NominalFeature)) throw new RuntimeException("Cannot compare different features: " + feature.getClass());
		if (val.equals(((NominalFeature) feature).getVal())) return 1f;
		return 0;
	}
}
