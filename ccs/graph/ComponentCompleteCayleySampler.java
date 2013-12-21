package ccs.graph;

import java.util.AbstractList;
import java.util.ArrayList;

public class ComponentCompleteCayleySampler implements NodeSampler<ArrayList<Double>> {
	int dimension;
	int samplesPerNode;
	TreeDecomp t;

	public ComponentCompleteCayleySampler(TreeDecomp t, int spn) {
		assert (spn > 0);
		this.t = t;
		dimension = t.getNumOfConstructStep();
		samplesPerNode = spn;
	}

	// ???
	public int getSamplePointDimension() {
		return dimension;
	}

	public AbstractList<SamplePoint<ArrayList<Double>>> sample(Node n) {
		ArrayList<SamplePoint<ArrayList<Double>>> list = new ArrayList<SamplePoint<ArrayList<Double>>>();

		for (int i = 0; i < samplesPerNode; ++i) {
			ArrayList<Double> l = new ArrayList<Double>();

			double percentage = (double) i / samplesPerNode;
			double cayley = n.sampleCayleyAt(percentage);
			Graph g = t.tryRealize(cayley, n.getSolutionType());
			assert (g != null);
			for (Edge e : t.getCanonicalBaseNonedges()) {
				double le = g.distance(e);
				l.add(le);
			}
			list.add(new SamplePoint<ArrayList<Double>>(l));
		}

		return list;
	}

}