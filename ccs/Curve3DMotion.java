package ccs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ccs.graph.ContinuousMotion;
import ccs.graph.ContinuousMotionSamples;
import ccs.graph.Graph;
import ccs.graph.SamplePoint;
import ccs.graph.TwoTuple;

public class Curve3DMotion implements Curve3D {

	private Point3D[] vertices;
	private Edge[] edges;

	/**
	 * Color for each 3D point
	 */
	private Color[] colors;

	/**
	 * Which continuous motion do I correspond to. 0: getMotion(); 1: getM1();
	 * 2: getM2();
	 */
	private int which = 0;

	public Point3D getCurrentPoint() {
		MotionModel mModel = MotionModel.getInstance();

		ContinuousMotion m;
		Graph g = null;

		switch (which) {
		case 0:
			// m = mModel.getMotion();
			g = mModel.getSpinnerModel().getValue().getValue();
			break;
		case 1:
			g = mModel.getSpinnerModel1().getValue().getValue();
			break;
		case 2:
			g = mModel.getSpinnerModel2().getValue().getValue();
			break;
		}
		return graphToPoint(g);
		// Debug.msg("I'm refreshed! " + spinner.getPercentage() + curPoint);
	}

	private ccs.graph.Edge projectingEdges[] = new ccs.graph.Edge[3];

	/**
	 * @param which
	 *            Which continuous motion do I correspond to. 0: getMotion(); 1:
	 *            getM1(); 2: getM2();
	 */
	public Curve3DMotion(int which) {
		this.which = which;
	}

	/**
	 * Generate a curve corresponding to current connected component, projected
	 * onto the given 3 edges
	 * 
	 * @param es
	 *            the given edges to do projection
	 */
	public void refresh(ccs.graph.Edge es[]) {
		assert (es.length >= 3);
		projectingEdges[0] = es[0];
		projectingEdges[1] = es[1];
		projectingEdges[2] = es[2];

		MotionModel mModel = MotionModel.getInstance();
		ContinuousMotionSamples<Graph> samples = null;
		// choose the model to use
		switch (which) {
		case 0:
			samples = mModel.getMotionSamples();
			break;
		case 1:
			samples = mModel.getMotionSamples1();
			break;
		case 2:
			samples = mModel.getMotionSamples2();
			break;
		}

		vertices = new Point3D[samples.size()];

		if (ControlPanel.getInstance().isTracingMotion() == 2)
			edges = new Edge[vertices.length - 1];
		else
			edges = new Edge[vertices.length];
		colors = new Color[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
			SamplePoint<Graph> p = samples.get(i);
			Graph g = p.getValue();
			vertices[i] = graphToPoint(g);

			if (i < edges.length)
				edges[i] = new Edge(i, (i + 1) % vertices.length);

			// TODO: awkward. maybe place this somewhere else.
			colors[i] = TreeDecompModel.getInstance().getForwardSolutionType(g)
					.getColor().brighter();
		}

		// backg = backbuffer.getGraphics();
		// drawWireframe(backg);
	}

	private Point3D graphToPoint(Graph g) {
		double x = g.distance(projectingEdges[0]);
		double y = g.distance(projectingEdges[1]);
		double z = g.distance(projectingEdges[2]);
		return new Point3D((int) x, (int) y, (int) z);
	}

	public Point3D getVertex(int i) {
		return vertices[i];
	}

	public Edge getEdge(int i) {
		return edges[i];
	}

	public int size() {
		return vertices.length;
	}
	
	public int edgeSize(){
		return edges.length;
	}

	public String toString() {
		return Arrays.toString(vertices);
	}

	private ArrayList<TwoTuple<Point3D, Color>> spPoints = new ArrayList<TwoTuple<Point3D, Color>>();

	// private Point3D spPoint;

	public boolean setFocalPoint(Graph g, Color c) {
		Point3D p = graphToPoint(g);
		spPoints.add(new TwoTuple<Point3D, Color>(p, c));
		return true;
	}

	public Collection<TwoTuple<Point3D, Color>> getFocalPoints() {
		// return spPoint;
		return spPoints;
	}

	public Color getColor(int j) {
		return colors[j];
	}
}
