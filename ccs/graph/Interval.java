package ccs.graph;

//temporarily for double
//closed intervals
public class Interval implements Comparable<Interval> {
	public double lower;
	public double upper;

	public Interval(double lower, double upper) {
		super();
		assert (lower <= upper);
		this.lower = lower;
		this.upper = upper;
	}

	public double getLength() {
		return upper - lower;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Interval))
			return false;
		Interval that = (Interval) o;
		return (this.lower == that.lower && this.upper == that.upper);
	}

	// sort by lower
	public int compareTo(Interval that) {
		if (this.equals(that))
			return 0;
		if (this.lower < that.lower)
			return -1;
		else if (this.lower == that.lower && this.upper < that.upper)
			return -1;
		else
			return 1;
	}

	public boolean hasIntersection(Interval that) {
		if (this.upper < that.lower || this.lower > that.upper)
			return false;
		return true;
	}

	public static boolean hasIntersection(Interval i1, Interval i2) {
		return i1.hasIntersection(i2);
	}

	public static Interval intersect(Interval i1, Interval i2) {
		assert (hasIntersection(i1, i2));
		double lower = Math.max(i1.lower, i2.lower);
		double upper = Math.min(i1.upper, i2.upper);
		return new Interval(lower, upper);
	}

	public Interval union(Interval that) {
		assert hasIntersection(that);
		double lower = Math.min(this.lower, that.lower);
		double upper = Math.max(this.upper, that.upper);
		return new Interval(lower, upper);

	}

	public static Interval union(Interval i1, Interval i2) {
		assert (hasIntersection(i1, i2));
		double lower = Math.min(i1.lower, i2.lower);
		double upper = Math.max(i1.upper, i2.upper);
		return new Interval(lower, upper);

	}

	public boolean contains(double v) {
		// added accuracy
		if (Math.abs(lower - v) < TreeDecomp.ACCURACY)
			return true;
		if (Math.abs(upper - v) < TreeDecomp.ACCURACY)
			return true;
		return v >= lower && v <= upper;
	}

	public double[] toArray() {
		double arr[] = { lower, upper };
		return arr;
	}

	@Override
	public String toString() {
		return "[" + lower + ", " + upper + "]";
	}

}
