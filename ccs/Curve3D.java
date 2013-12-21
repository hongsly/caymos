package ccs;

import java.awt.Color;
import java.util.Collection;

import ccs.graph.Graph;
import ccs.graph.TwoTuple;

class Point3D {
	public int x, y, z;

	public Point3D(int X, int Y, int Z) {
		x = X;
		y = Y;
		z = Z;
	}

	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
}

class Edge {
	public int a, b;

	public Edge(int A, int B) {
		a = A;
		b = B;
	}

	public String toString() {
		return "(" + a + "," + b + ")";
	}
}

// TODO:
// With graph : static curve. no current value.
// With MotionModel : curve with current value
public interface Curve3D {
	public void refresh(ccs.graph.Edge edges[]);

	public Point3D getVertex(int i);

	public Edge getEdge(int i);

	/**
	 * Emphasize a point on the curve.
	 * 
	 * @param g
	 *            A realization along the curve that need to be emphasized in
	 *            display
	 */
	public boolean setFocalPoint(Graph g, Color c);
	
	/**
	 * @return The point emphasized on the curve.
	 */
	public Collection<TwoTuple<Point3D, Color>> getFocalPoints();

	/**
	 * @return the number of vertices
	 */
	public int size();

}
