package ccs;

import java.util.ArrayList;
import java.util.Collection;

import ccs.graph.Graph;
import ccs.graph.SolutionType;
import ccs.graph.TreeDecomp;
import ccs.graph.Vertex;

public class FlippingModel {
	private FlippingModel() {
		toFlip = new ArrayList<Vertex>();
	}

	private static FlippingModel me = new FlippingModel();

	public static FlippingModel getInstance() {
		return me;
	}

	private TreeDecompModel tdModel = TreeDecompModel.getInstance();

	/**
	 * Vertices to flip orientations
	 */
	private ArrayList<Vertex> toFlip;

	public void clear() {
		toFlip.clear();
	}

	public Collection<Vertex> getVerteicsToFlip() {
		return toFlip;
	}

	public void doFlip() {
		/*
		 * assert (getStatus() == Status.generated || getStatus() ==
		 * Status.picking);
		 */
		SolutionType t = tdModel.getForwardSolutionType();
		for (Vertex v : toFlip)
			t.flipOrientation(tdModel.isStepVertex(v));

		Graph g = tdModel.tryRealize(t);
		if (g != null) {
			tdModel.setPoints(g);
		} else {
			// TODO: say not possible
		}
		clear();
	}

	/**
	 * Add v to flip list if it is not already there. If v is in list, remove v
	 */
	public void flipVertex(Vertex v) {
		if (toFlip.contains(v)) {
			toFlip.remove(v);
		} else {
			int index = tdModel.isStepVertex(v);
			if (index >= 0) {
				toFlip.add(v);
			}
		}
	}

}
