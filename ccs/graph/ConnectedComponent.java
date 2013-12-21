package ccs.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import ccs.Debug;
import ccs.TreeDecompModel;

public class ConnectedComponent extends ContinuousMotion {

	public ConnectedComponent(TreeDecomp t) {
		super(t);
	}

	@Override
	public Node startNode() {
		assert (!isEmpty());
		return get(0);
	}

	@Override
	public Node endNode() {
		return startNode();
	}

	public static ConnectedComponent findComponentOf(TreeDecomp t, Graph g1) {
		double startl = g1.distance(t.getBaseNonedge());
		SolutionType startType = t.getForwardSolutionType(g1);
		OrientedCCS startO = t.ccs.getOrientedCCS(startType);
		Interval startI = startO.getContainingInterval(startl);

		assert (startI != null);

		ConnectedComponent component = new ConnectedComponent(t);

		// Using the entire interval as start node
		double endl = startI.toArray()[0];
		startl = startI.toArray()[1];
		Node startNode = new Node(startl, endl, startI, startType);

		component.add(startNode);

		SolutionType type = startType;

		while (true) {
			Debug.msg("from end point: " + endl);

			// Flip the orientation of extreme construction step
			Graph g = t.tryRealize(endl, type);
			SolutionType extremeType = t.getForwardSolutionType(g);
			if (!extremeType.isExtreme()) {
				Debug.warnMsg(extremeType + "");
				Debug.warnMsg(endl+"");
				for (int i = 0; i < TreeDecompModel.getInstance()
						.getNumOfConstructStep(); ++i)
					Debug.msg(i
							+ ":"
							+ TreeDecompModel.getInstance()
									.getConstructionStep(i));

			}
			assert (extremeType.isExtreme());
			int zeroIndex = extremeType.indexOfZero();
			type.flipOrientation(zeroIndex);

			OrientedCCS o = t.ccs.getOrientedCCS(type);
			assert (o != null);
			Interval curI = o.getContainingInterval(endl);
			assert (curI != null);

			if (o == startO && curI == startI) {
				Debug.msg("BACK TO START. ");
				// Only contains the start node once.
				/* addNode(startl, startI, startO, 0); */
				return component;
			}

			// continue finding
			startl = endl;
			if (Math.abs(endl - curI.lower) < TreeDecomp.ACCURACY) {
				endl = curI.upper;
			} else {
				assert (Math.abs(endl - curI.upper) < TreeDecomp.ACCURACY);
				endl = curI.lower;
			}
			Node n = new Node(startl, endl, curI, type);
			component.add(n);
		}
	}

	/**
	 * @return a list of all connected components of t
	 */
	public static ArrayList<ConnectedComponent> findAllComponents(TreeDecomp t) {
		ArrayList<ConnectedComponent> list = new ArrayList<ConnectedComponent>();

		// 1. list every intervals from every Occs.
		LinkedList<TwoTuple<SolutionType, Interval>> intervalList = new LinkedList<TwoTuple<SolutionType, Interval>>();
		for (OrientedCCS oc : t.ccs.getOrientedCCSs()) {
			SolutionType type = oc.getSolutionType().clone();
			for (Interval i : oc.getIntervals()) {
				intervalList.add(new TwoTuple<SolutionType, Interval>(type, i));
			}
		}

		// 2. for each interval:
		while (!intervalList.isEmpty()) {
			// (1) gen component.
			TwoTuple<SolutionType, Interval> orientedInt = intervalList
					.getFirst();
			SolutionType type = orientedInt.getFirst();
			Interval inv = orientedInt.getSecond();
			double lf = (inv.lower + inv.upper) / 2;
			Graph realization = t.solve(lf, type);
			assert (realization != null);

			Debug.msg("remove first oriented interval:" + orientedInt);

			ConnectedComponent component = findComponentOf(t, realization);
			list.add(component);

			// (2) remove the intervals passed by the component from the list of
			// intervals
			for (Node n : component) {
				TwoTuple<SolutionType, Interval> nTuple = new TwoTuple<SolutionType, Interval>(
						n.getSolutionType().clone(), n.getInterval());
				Debug.msg("interval list:" + intervalList);
				Debug.msg("interval to remove:" + nTuple);
				boolean b = intervalList.remove(nTuple);
				assert (b);
			}
		}

		return list;
	}

	/**
	 * TODO: Equal components can have different order in nodes.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ContinuousMotion))
			return false;
		ContinuousMotion that = (ContinuousMotion) obj;
		if (size() != that.size())
			return false;

		Node thisFirst = this.get(0);
		if (!that.contains(thisFirst))
			return false;
		ListIterator<Node> iterThis = this.listIterator(), iterThat = that
				.listIterator(that.indexOf(thisFirst));
		while (iterThis.hasNext()) {
			if (!iterThat.hasNext())
				iterThat = that.listIterator();
			if (!iterThis.next().equals(iterThat.next()))
				return false;
		}
		return true;
	}
}
