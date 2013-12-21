package ccs;

import java.util.ArrayList;

import ccs.graph.Interval;
import ccs.graph.Graph;
import ccs.graph.Point2D;
import ccs.graph.SolutionType;
import ccs.graph.TreeDecomp;
import ccs.graph.Vertex;

public class Trace {
	Vertex vertex;
	Interval in;
	SolutionType type;
	TreeDecomp td;
	// ArrayList<Point2D> points = new ArrayList<Point2D>();
	ArrayList<TracePoint> tracePoints;

	boolean lowerClosed = false, upperClosed = false;

	public String toString() {
		return "type:" + type + ", interval:" + in + ", " + tracePoints;
	}

	public Trace(Vertex v, Interval i, SolutionType t, TreeDecomp td) {
		vertex = v;
		in = i;
		type = t;
		this.td = td;

		tracePoints = new ArrayList<TracePoint>();
	}

	public boolean canAddPoint(double lf, SolutionType t) {
		return t.equals(type) && in.contains(lf);
	}

	public void addPoint(Point2D p, double lf) {
		// add points according to sorted l_f
		assert (in.contains(lf));

		if (!lowerClosed && lf - in.lower < 3.0) {
			// TODO: translate this; awkward. connect trace with tdModel.
			Graph g = td.tryRealize(in.lower, type);
			g = TreeDecompModel.getInstance().transformG(g);
			tracePoints.add(0, new TracePoint(in.lower, g.getPoint(vertex)));
			lowerClosed = true;
			System.out.println(in + " lower closed: " + g.getPoint(vertex));
			return;
		} else if (!upperClosed && in.upper - lf < 3.0) {
			Graph g = td.tryRealize(in.upper, type);
			g = TreeDecompModel.getInstance().transformG(g);
			tracePoints.add(tracePoints.size(),
					new TracePoint(in.upper, g.getPoint(vertex)));
			upperClosed = true;
			System.out.println(in + " upper closed: " + g.getPoint(vertex));
			return;
		}

		final double MIN_DISTANCE = 1;
		int i = 0;
		for (; i < tracePoints.size(); ++i) {
			TracePoint curP = tracePoints.get(i);
			if (curP.point.distance(p) <= MIN_DISTANCE)
				return;
			if (curP.lf > lf)
				break;
		}
		tracePoints.add(i, new TracePoint(lf, p));
	}
}

class TracePoint implements Comparable<TracePoint> {

	double lf;
	Point2D point;

	public TracePoint(double l, Point2D p) {
		lf = l;
		point = p;
	}

	public int compareTo(TracePoint that) {
		if (this.lf == that.lf)
			return 0;
		if (this.lf < that.lf)
			return -1;
		else
			return 1;
	}

	public String toString() {
		return lf + ":" + point;
	}

}
