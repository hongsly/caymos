package ccs.graph;

import java.util.AbstractList;

public interface NodeSampler<E> {
	// public int getSamplePointDimension();

	public AbstractList<SamplePoint<E>> sample(Node n);
}
