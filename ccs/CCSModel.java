package ccs;

import java.util.ArrayList;
import java.util.Collection;

import ccs.graph.CCS;
import ccs.graph.CCSInterface;
import ccs.graph.Interval;
import ccs.graph.OrientedCCS;
import ccs.graph.Pair;
import ccs.graph.SolutionType;

public class CCSModel {
	private static CCSModel me;

	// private TreeDecompModel tdModel;
	// private ControlPanel control;

	private CCSModel() {
		typesToDraw = new ArrayList<SolutionType>();
	}

	public static CCSModel getInstance() {
		if (me == null)
			me = new CCSModel();
		return me;
	}

	private CCS ccs;
	private OrientedCCS occs;
	private boolean isOriented;
	private double curVal = -1;

	private ArrayList<SolutionType> typesToDraw; // o-ccs along a path

	ArrayList<ConfigPanel> markedPoints = new ArrayList<ConfigPanel>();

	public Collection<ConfigPanel> getMarkedPoints() {
		return markedPoints;
	}

	/**
	 * Set current ccs, oriented/nonoriented according to control specification.
	 */
	public void refresh() {
		TreeDecompModel tdModel = TreeDecompModel.getInstance();
		ControlPanel control = ControlPanel.getInstance();

		CCS nccs = tdModel.getCCS();
		if (control.isOrientedCCS()) {
			isOriented = true;
			occs = nccs.getOrientedCCS(tdModel.getForwardSolutionType());
		} else {
			isOriented = false;
			occs = null;
		}
		ccs = nccs;
		typesToDraw.clear();
	}

	public Pair<CCSInterface> getCCS() {
		return new Pair<CCSInterface>(ccs, occs);
	}

	public SolutionType getSolutionType() {
		if (isOriented)
			return occs.getSolutionType();
		else
			return null;
	}

	public Collection<SolutionType> getTypesToDraw() {
		return typesToDraw;
	}

	/**
	 * 
	 * @return the interval in ccs containing val. If val is not in current ccs,
	 *         return null
	 */
	public Interval contains(double val) {
		ArrayList<Interval> union;
		if (isOriented)
			union = occs.getIntervals();
		else
			union = ccs.getIntervals();
		for (Interval i : union)
			if (i.contains(val))
				return i;
		return null;
	}

	public boolean isGenerated() {
		return ccs != null;
	}

	public boolean isOriented() {
		return isOriented;
	}

	public double getCurrent() {
		return curVal;
	}

	public void setCurrent(double val) {
		assert (contains(val) != null);
		curVal = val;
	}

	/**
	 * Reset current Cayley config from current realization
	 */
	public void refreshCurrent() {
		double length = TreeDecompModel.getInstance().getBaseNoneedgeLength();
		setCurrent(length);
	}

	public double getMin() {
		if (isOriented)
			return occs.getMin();
		else
			return ccs.getMin();
	}

	public double getMax() {
		if (isOriented)
			return occs.getMax();
		else
			return ccs.getMax();
	}

	public void addMarkedPoint(ConfigPanel c) {
		if (!markedPoints.contains(c)) {
			markedPoints.add(c);
		}
	}

	public void removeMarkedPoint(ConfigPanel c) {
		if (markedPoints.contains(c)) {
			markedPoints.remove(c);
		}
	}

	public void addType(SolutionType t) {
		//assert (!isOriented);
		assert (((CCS) ccs).getOrientedCCS(t) != null);
		if (!typesToDraw.contains(t))
			typesToDraw.add(t);
	}

	public void removeType(SolutionType t) {
		typesToDraw.remove(t);
	}

	public void clearTypes() {
		typesToDraw.clear();
	}

	/*
	 * Refresh CCS according to current orientation. Only necessary if
	 * isOriented ??
	 * 
	 * // public void refreshCCS() { // init(main.td.ccs); // // if (isOriented)
	 * { // // SolutionType type = tdModel.getForwardSolutionType(); // //
	 * addType(type); // initCCS(); // } // repaint(); // }
	 */

}
