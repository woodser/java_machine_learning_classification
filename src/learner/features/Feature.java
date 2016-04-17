package learner.features;

import java.io.Serializable;

public interface Feature extends Serializable {

	public double compare(Feature feature);
}
