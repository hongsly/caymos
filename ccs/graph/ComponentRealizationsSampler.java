package ccs.graph;

import java.util.AbstractList;
import java.util.ArrayList;

public class ComponentRealizationsSampler implements NodeSampler<Graph> {
	private int samplesPerNode;
	private TreeDecomp t;

	public ComponentRealizationsSampler(TreeDecomp t, int spn) {
		assert (spn > 0);
		this.t = t;
		samplesPerNode = spn;
	}
	
	final double ACCURACY = 2;

	public AbstractList<SamplePoint<Graph>> sample(Node n) {
		ArrayList<SamplePoint<Graph>> list = new ArrayList<SamplePoint<Graph>>();
		
		int spn = Math.max(samplesPerNode, (int)(n.getLength()/ACCURACY));

		for (int i = 0; i < spn; ++i) {
			double percentage = (double) i / spn;
			double cayley = n.sampleCayleyAt(percentage);

			Graph g = t.tryRealize(cayley, n.getSolutionType());
			assert (g != null);
			/*// ad-hoc modification
			double pp = percentage;
			while(g==null){
				pp -= 0.0001;
				cayley = n.sampleCayleyAt(pp);
				g = t.tryRealize(cayley,n.getSolutionType());
			}*/
			list.add(new SamplePoint<Graph>(g));
			
		}

		return list;
	}

}
