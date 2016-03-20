package learner.features;

/**
 * Represents a continuous feature.
 * 
 * @author woodser
 */
public class ContinuousFeature implements Feature {
	
	private double val;

	public ContinuousFeature(double val) {
		super();
		this.val = val;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	// TODO: this implementation needs improved especially with nulls
	@Override
	public double compare(Feature feature) {
		if (!(feature instanceof ContinuousFeature)) throw new RuntimeException("Cannot compare different features: " + feature.getClass());
		double diffPercent = Math.max(0, Math.abs(val) - Math.abs(val - ((ContinuousFeature) feature).getVal())) / val;
		return diffPercent * diffPercent;
	}
}
