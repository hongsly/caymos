package ccs.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import ccs.Debug;

public class CCS implements CCSInterface {
	// final static int width = 1000, height = 180;

	private ArrayList<SolutionType> forwardSolutionTypes;

	// decomposed, indexed by forward solution type
	private ArrayList<OrientedCCS> orientedCCS;

	private ArrayList<Interval> unionIntervals;

	private double min = Double.POSITIVE_INFINITY,
			max = Double.NEGATIVE_INFINITY;

	public CCS() {
		forwardSolutionTypes = new ArrayList<SolutionType>();
		orientedCCS = new ArrayList<OrientedCCS>();
	}

	public Collection<OrientedCCS> getOrientedCCSs() {
		return orientedCCS;
	}

	public OrientedCCS getOrientedCCS(SolutionType type) {
		int index;
		for (index = 0; index < forwardSolutionTypes.size(); ++index) {
			SolutionType t = forwardSolutionTypes.get(index);
			if (t.equals(type))
				break;
		}
		if (index >= forwardSolutionTypes.size())
			return null;
		// int index = forwardSolutionTypes.indexOf(type);
		return orientedCCS.get(index);
	}

	public void addOrientedCCS(OrientedCCS o) {
		if (Graph.DEBUG)
			for (SolutionType t : forwardSolutionTypes)
				assert (!t.equals(o.forwardSolutionType));
		forwardSolutionTypes.add(o.forwardSolutionType);
		orientedCCS.add(o);
		double min = o.getMin(), max = o.getMax();
		this.min = (min < this.min ? min : this.min);
		this.max = (max > this.max ? max : this.max);
	}

	public int getNumOfSolutionTypes() {
		return forwardSolutionTypes.size();
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < forwardSolutionTypes.size(); ++i) {
			s += forwardSolutionTypes.get(i).toString() + ":\n";
			s += orientedCCS.get(i).toString() + "\n";
		}
		return s;
	}

	public void generateUnionIntervals() {
		unionIntervals = new ArrayList<Interval>();
		ArrayList<Interval> l = new ArrayList<Interval>();
		for (int i = 0; i < forwardSolutionTypes.size(); ++i) {
			l.addAll(orientedCCS.get(i).intervals);
		}
		if (l.isEmpty())
			return;
		// System.out.println(l);
		Collections.sort(l);
		System.out.println("intervals to union: " + l);
		unionIntervals.add(l.get(0));
		for (int i = 0; i < l.size(); ++i) {
			int lastIndex = unionIntervals.size() - 1;
			Interval cur = l.get(i);
			Interval last = unionIntervals.get(lastIndex);
			if (cur.hasIntersection(last)) {
				unionIntervals.set(lastIndex, cur.union(last));
			} else {
				unionIntervals.add(cur);
			}
		}
	}

	public ArrayList<Interval> getIntervals() {
		return unionIntervals;
	}

	public String printUnionIntervals() {
		return unionIntervals.toString();
	}

	public static CCS generateCCS(TreeDecomp td) {
		boolean b = td.isLow();
		assert (b);
		b = td.constructionSequenceGenerated();
		assert (b);
		// for low: reverse solve for each extreme graph
		// TODO: generate oriented for each fwd sol type
		// ArrayList<java.lang.Double> candidate = new
		// ArrayList<java.lang.Double>();
		// SolutionType forward = getForwardSolutionType();
		HashMap<SolutionType, ArrayList<Double>> candidates = new HashMap<SolutionType, ArrayList<Double>>();
		ArrayList<SolutionType> types = SolutionType.generateSolutionTypes(td
				.getNumOfConstructStep());
		for (SolutionType type : types) {
			candidates.put(type, new ArrayList<Double>());
		}

		for (int i = 0; i < td.getNumOfConstructStep(); ++i) {
			TreeDecomp t = td.getExtremeGraph(i);
			// Debug.warnMsg(i+"th extreme graph:"+t);
			Interval interval = td.extremeInterval(td.getConstructionStep(i));
			Debug.msg(i + "th extreme Interval:" + interval);

			for (double length : interval.toArray()) {
				// added Accuracy ...
				// ??? May return duplicate result, Why? -- come from
				// reflection?
				ArrayList<Graph> solutions = t.tryRealize(length);
				// Debug.warnMsg("extreme l:"+length+"; extreme solution size:"+solutions.size());

				for (Graph g : solutions) {
					// System.out.println(g.printPoints());
					double distance = g.distance(td.getBaseNonedge());
					SolutionType extremeSolType = td.getForwardSolutionType(g,
							i);
					for (SolutionType type : types) {
						if (type.compatible(extremeSolType)) {
							ArrayList<Double> typeCandidates = candidates
									.get(type);
							boolean addit = true;
							for (double d : typeCandidates) {
								if (Math.abs(d - distance) < TreeDecomp.ACCURACY) {
									addit = false;
									break;
								}
							}
							if (addit) {
								Debug.msg("for type " + type
										+ ", add solution of " + length
										+ " to candidates: " + distance);
								typeCandidates.add(distance);
							}
						}
					}
				}
			}

		}
		CCS ccs = new CCS();
		for (SolutionType type : types) {
			ArrayList<Double> typeCandidates = candidates.get(type);
			if (typeCandidates.isEmpty())
				continue;
			Collections.sort(typeCandidates);
			Debug.msg("for type " + type + ", sorted candidates:"
					+ typeCandidates);

			// Check candidate points & set up intervals
			OrientedCCS orientedCCS = td.genOrientedCCSFromCandidates(
					typeCandidates, type);
			if (orientedCCS.isEmpty())
				continue;
			Debug.msg("oriented ccs:" + orientedCCS + "\n");
			ccs.addOrientedCCS(orientedCCS);
		}
		Debug.msg(ccs);
		ccs.generateUnionIntervals();
		Debug.msg("overall: " + ccs.printUnionIntervals() + "\n");
		System.out.println();
		return ccs;
	}
}
