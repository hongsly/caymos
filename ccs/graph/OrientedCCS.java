package ccs.graph;

import java.util.ArrayList;

public class OrientedCCS implements CCSInterface{
	SolutionType forwardSolutionType;
	ArrayList<Interval> intervals;

	// private void sort() {
	// Collections.sort(intervals);
	// }
	
	public SolutionType getSolutionType(){
		return forwardSolutionType;
	}
	
	public Interval contains(double val) {
		for (Interval i : intervals) {
			if (i.contains(val))
				return i;
		}
		return null;
	}

	public ArrayList<Interval> getIntervals() {
		return intervals;
	}

	public double getMin() {
		if (intervals.isEmpty())
			return java.lang.Double.NEGATIVE_INFINITY;
		else
			return intervals.get(0).lower;
	}

	public boolean isEmpty() {
		return intervals.isEmpty();
	}

	public double getMax() {
		if (intervals.isEmpty())
			return java.lang.Double.POSITIVE_INFINITY;
		else
			return intervals.get(intervals.size() - 1).upper;
	}

	public OrientedCCS(SolutionType type) {
		this.forwardSolutionType = type;
		intervals = new ArrayList<Interval>();
	}

	// append interval at the end
	public void appendInterval(double lower, double upper) {
		assert (lower > getMin());
		intervals.add(new Interval(lower, upper));
	}

	// TODO: arraylist + binary search?
	public Interval getContainingInterval(double value) {
		for (Interval interval : intervals) {
			if (interval.contains(value))
				return interval;
		}
		return null;
	}

	@Override
	public String toString() {
		String s = "";
		for (Interval interval : intervals) {
			s += interval.toString();
		}
		return s;
	}
}