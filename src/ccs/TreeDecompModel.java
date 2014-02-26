/*
 This file is part of CayMos. 

 CayMos is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CayMos is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ccs;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ccs.graph.CCS;
import ccs.graph.Cluster;
import ccs.graph.ConnectedComponent;
import ccs.graph.ConstructionStep;
import ccs.graph.ContinuousMotion;
import ccs.graph.Edge;
import ccs.graph.Graph;
import ccs.graph.Point2D;
import ccs.graph.SolutionType;
import ccs.graph.TreeDecomp;
import ccs.graph.Vector2D;
import ccs.graph.Vertex;

public class TreeDecompModel {
	private Graph graph = new Graph();
	private TreeDecomp td; // when upadte the graph, set td to null;

	private static TreeDecompModel me;

	// private ControlPanel control = ControlPanel.getInstance();

	private TreeDecompModel() {
	}

	public static TreeDecompModel getInstance() {
		if (me == null)
			me = new TreeDecompModel();
		return me;
	}

	public Graph getGraph() {
		// return graph rotated to specified axis.
		if (origin != null && xAxis != null) {
			Graph gg = graph.clone();

			Point2D curXP = gg.getPoint(xAxis);
			Point2D curOP = gg.getPoint(origin);
			Vector2D rotateV = curXP.minus(curOP);

			AffineTransform r = new AffineTransform();
			r.rotate(rotateV.x(), rotateV.y());
			try {
				r.invert();
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}
			gg.transformVertices(r);

			curOP = gg.getPoint(origin);
			// Debug.warnMsg("original op:" + originP + ", current op:" +
			// curOP);
			AffineTransform t = new AffineTransform();
			Vector2D translateV = originP.minus(curOP);
			t.translate(translateV.x(), translateV.y());

			gg.transformVertices(t);
			return gg;
		}

		return graph;
	}

	public TreeDecomp getTd() {
		return me.td;
	}

	public void clearTd() {
		me.td = null;
		me.origin = null;
		me.originP = null;
		me.xAxis = null;
	}

	/**
	 * @return whether current graph is 1-dof td
	 */
	public boolean isTd() {
		return me.td != null;
	}

	public boolean isLow() {
		assert (isTd());
		return me.td.isLow();
	}

	public boolean is1Path() {
		assert (isTd());
		return me.td.is1Path();
	}

	public boolean isTriangleFree() {
		assert (isTd());
		return me.td.isTriangleFree();
	}

	public void refresh() {
		me.clearTd();
		// Step 1. test if it is td
		TreeDecomp t = new TreeDecomp(me.graph);
		if (t.is1DofTreeDecomposable()) {
			me.td = t;
		} else {
			me.td = null;
			return;
		}

		// Step 2. test 1-path / t-free / low
		// if (!me.td.constructionSequenceGenerated())
		me.td.generateConstructionSequence();
		me.td.normalizeBaseNonEdge();

		//Debug.msg(me.td+"");
		FloatingPanels.getInstance().refresh();
	}

	public void writeToStream(ObjectOutputStream o) throws IOException {
		graph.writeToStream(o);
	}

	public void readFromStream(ObjectInputStream in) throws IOException {
		try {
			graph = Graph.readFromStream(in);
		} catch (ClassNotFoundException c) {
			Debug.warnMsg("ListGraph class not found.");
			c.printStackTrace();
			return;
		}
		refresh();
	}

	public CCS getCCS() {
		if (!td.ccsGenerated())
			td.generateCCS();
		return td.ccs;
	}

	public SolutionType getForwardSolutionType() {
		return td.getForwardSolutionType();
	}

	public SolutionType getForwardSolutionType(Graph g) {
		return td.getForwardSolutionType(g);
	}

	public Graph tryRealize(Double val, SolutionType forward) {
		return td.tryRealize(val, forward);
	}

	public ArrayList<Graph> tryRealize(Double val) {
		return td.tryRealize(val);
	}

	public Graph tryRealize(SolutionType t) {
		return td.tryRealize(t);
	}

	public void setPoints(Graph g) {
		graph.setPoints(g);
	}

	public double getBaseNoneedgeLength() {
		return td.getBaseNoneedgeLength();
	}

	public Graph getGraphClone() {
		return graph.clone();
	}

	public Edge getBaseNonedge() {
		return td.getBaseNonedge();
	}

	public Cluster getClusterBetween(Vertex v, Vertex u) {
		return td.getClusterBetween(v, u);
	}

	public int indexOf(Cluster c) {
		return td.indexOf(c);
	}

	public ConstructionStep getConstructionStep(int stepNum) {
		return td.getConstructionStep(stepNum);
	}

	public boolean isAdjacent(Vertex v, Vertex u) {
		return graph.isAdjacent(v, u);
	}

	public Vertex getVertex(Point2D clickP, int clickprecision) {
		return getGraph().getVertex(clickP, clickprecision);
	}

	public int isStepVertex(Vertex v) {
		return td.isStepVertex(v);
	}

	public void addVertex(Point2D clickP) {
		graph.addVertex(clickP);
	}

	public void removeVertex(Vertex v) {
		graph.removeVertex(v);
	}

	public void addEdge(Vertex v, Vertex u) {
		graph.addEdge(v, u);
	}

	public void removeEdge(Vertex v, Vertex u) {
		graph.removeEdge(v, u);
	}

	public void setPoint(Vertex selectedV, int x, int y) {
		graph.setPoint(selectedV, x, y);
	}

	public Point2D getPoint(Vertex v) {
		return getGraph().getPoint(v);
	}

	public ContinuousMotion findPath(Graph startRealization,
			Graph endRealization) {
		return td.findPath(startRealization, endRealization);
	}

	public ContinuousMotion findPath(Double startCayleyConfig,
			Double endCayleyConfig) {
		return td.findPath(startCayleyConfig, endCayleyConfig);
	}

	public ConnectedComponent genComponent() {
		return td.findPathFrom(graph);
	}

	public ConnectedComponent genComponent(Graph g) {
		return td.findPathFrom(g);
	}

	public int getNumOfConstructStep() {
		return td.getNumOfConstructStep();
	}

	public Iterable<Edge> getCanonicalBaseNonedges() {
		return td.getCanonicalBaseNonedges();
	}

	private Vertex origin;
	private Point2D originP;
	private Vertex xAxis;

	public void setAxis(Vertex o, Vertex x) {
		origin = o;
		originP = graph.getPoint(o);
		xAxis = x;
	}

	// TODO: remove this.
	Graph transformG(Graph g) {
		if (origin != null && xAxis != null) {
			Graph gg = g.clone();

			Point2D curXP = gg.getPoint(xAxis);
			Point2D curOP = gg.getPoint(origin);
			Vector2D rotateV = curXP.minus(curOP);

			AffineTransform r = new AffineTransform();
			r.rotate(rotateV.x(), rotateV.y());
			try {
				r.invert();
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}
			gg.transformVertices(r);

			curOP = gg.getPoint(origin);
			// Debug.warnMsg("original op:" + originP + ", current op:" +
			// curOP);
			AffineTransform t = new AffineTransform();
			Vector2D translateV = originP.minus(curOP);
			t.translate(translateV.x(), translateV.y());

			gg.transformVertices(t);
			return gg;
		}
		return g;
	}
}
