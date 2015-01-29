package ccs.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import exceptions.InconsecutiveIndicesException;

/**
 * The combinatorics of a graph underlying a linkage. Implemented using adjacent-list.<br>
 * @author Menghan
 */
public class LinkageGraph extends AbstractGraph<Vertex> {

	/**
	 * list of vertices
	 */
	private ArrayList<Vertex> vertices;

	/**
	 * adjacent-lists indexed by vertices
	 */
	private HashMap<Vertex, LinkedList<Vertex>> vNeighbors;

	/**
	 * Indicates whether the vertices in this graph have consecutive indices
	 */
	private boolean isConsecutiveIndex = true;

	/**
	 * Returns whether the vertices in this graph have consecutive indices
	 * @return <code>true</code> if the vertices in this graph have consecutive indices
	 */
	public boolean isConsecutiveIndex() {
		return isConsecutiveIndex;
	}

	public LinkageGraph() {
		this.vertices = new ArrayList<Vertex>();
		this.vNeighbors = new HashMap<Vertex, LinkedList<Vertex>>();
	}

	/**
	 * Constructs a copy of the given graph. The new instance will share the same set of vertices
	 * with the original graph.
	 * @param g
	 */
	public LinkageGraph(LinkageGraph g) {
		this.vertices = new ArrayList<Vertex>(g.vertices);
		this.vNeighbors = new HashMap<Vertex, LinkedList<Vertex>>();
		for (Vertex v : vertices)
			this.vNeighbors.put(v, new LinkedList<Vertex>(g.getNeighbors(v)));
	}

	@Override
	public int size() {
		return vertices.size();
	}

	@Override
	public LinkedList<Vertex> getNeighbors(Vertex v) {
		return vNeighbors.get(v);
	}

	// /**
	// * @return a random vertex of the graph
	// */
	// public Vertex getRandomVertex() {
	// return vertices.get((int) (Math.random() * vertices.size()));
	// }

	@Override
	public Vertex addVertex() {
		int index = vertices.size();
		Vertex v = new Vertex(index);
		vertices.add(v);
		vNeighbors.put(v, new LinkedList<Vertex>());
		return v;
	}

	/**
	 * Inserts an existing vertex v into the graph. <br>
	 * <b>Note:</b> the vertex indices will be inconsecutive after insertion.
	 * @param v the vertex to be inserted
	 * @return <code>true</code> if the vertex is inserted successfully
	 */
	public boolean insertVertex(Vertex v) {
		if (this.contains(v)) return false;

		vertices.add(v);
		vNeighbors.put(v, new LinkedList<Vertex>());
		this.isConsecutiveIndex = false;
		return true;
	}

	/**
	 * Removes vertex v and changes the indices of the subsequence vertices such that the indices
	 * remains consecutive.
	 * @param v the vertex to be removed
	 * @return <code>true</code> If the vertex is removed successfully
	 * @throws InconsecutiveIndicesException If <code>this.isConsecutiveIndex() == false</code>
	 * @see #removeVertex(Vertex)
	 */
	public boolean removeVertexWithConsecutiveIndex(Vertex v) throws InconsecutiveIndicesException {
		if (!this.contains(v)) return false;
		if (!this.isConsecutiveIndex()) throw new InconsecutiveIndicesException();

		LinkedList<Vertex> neighbors = this.getNeighbors(v);
		for (Vertex u : neighbors)
			getNeighbors(u).remove(v);
		neighbors.clear();
		vertices.remove(v);
		vNeighbors.remove(v);

		for (int i = 0; i < vertices.size(); ++i)
			vertices.get(i).index = i;

		return true;
	}

	/**
	 * Removes vertex v from the graph, without changing indices of other vertices. <br>
	 * <b>Note:</b> indices of vertices will be inconsecutive after removal
	 * @param v
	 * @return true if the vertex is successfully removed
	 * @see #removeVertexWithConsecutiveIndex(Vertex)
	 */
	@Override
	public boolean removeVertex(Vertex v) {
		if (!contains(v)) return false;

		LinkedList<Vertex> neighbors = this.getNeighbors(v);
		for (Vertex u : neighbors)
			getNeighbors(u).remove(v);
		neighbors.clear();
		vertices.remove(v);
		vNeighbors.remove(v);
		this.isConsecutiveIndex = false;
		return true;
	}

	/**
	 * Adds an undirected edge to the graph
	 * @param e an edge between two vertices of the graph
	 * @see #addEdge(int, int)
	 * @see AbstractGraph#addEdge(Object, Object)
	 */
	public void addEdge(Edge e) {
		addEdge(e.v1(), e.v2());
	}

	/**
	 * Adds an undirected edge between two vertices with indices i1 & i2.<br>
	 * Shouldn't be called if the indices of vertices are not consecutive
	 * @param i1 index for the first vertex
	 * @param i2 index for the second vertex
	 * @throws InconsecutiveIndicesException If <code>this.isConsecutiveIndex() == false</code>
	 * @see #addEdge(Edge)
	 * @see AbstractGraph#addEdge(Object, Object)
	 */
	public void addEdge(int i1, int i2) {
		if (!this.isConsecutiveIndex()) throw new InconsecutiveIndicesException();
		addEdge(getVertex(i1), getVertex(i2));
	}

	/**
	 * Removes an undirected edge from the graph. The edge must exist when calling the method.
	 * @param e an edge between two vertices of the graph
	 * @return <code>true</code> if the edge is successfully removed.
	 * @see #removeEdge(int, int)
	 * @see AbstractGraph#removeEdge(Object, Object)
	 */
	public boolean removeEdge(Edge e) {
		return removeEdge(e.v1(), e.v2());
	}

	/**
	 * Removes the undirected edge between two vertices with indices i1 & i2. The edge must exist
	 * when calling the method.
	 * @param i1 index for the first vertex
	 * @param i2 index for the second vertex
	 * @return <code>true</code> if the edge is successfully removed.
	 * @throws InconsecutiveIndicesException If <code>this.isConsecutiveIndex() == false</code>
	 * @see #removeEdge(Edge)
	 * @see AbstractGraph#removeEdge(Object, Object)
	 */
	public boolean removeEdge(int i1, int i2) {
		if (!this.isConsecutiveIndex()) throw new InconsecutiveIndicesException();
		return removeEdge(getVertex(i1), getVertex(i2));
	}

	/**
	 * Checks whether the two vertices of a given edge are adjacent in this graph
	 * @param edge
	 * @return <code>true</code> if the endpoints of <code>edge</code> are adjacent in this graph
	 * @see #isAdjacent(int, int)
	 * @see AbstractGraph#isAdjacent(Object, Object)
	 */
	public boolean isAdjacent(Edge edge) {
		return isAdjacent(edge.v1(), edge.v2());
	}

	/**
	 * Checks whether the two vertices with given indices are adjacent in this graph<br>
	 * @param i1 index for the first vertex
	 * @param i2 index for the second vertex
	 * @return <code>true</code> if the two vertices are adjacent in this graph
	 * @throws InconsecutiveIndicesException If <code>this.isConsecutiveIndex() == false</code>
	 * @see #isAdjacent(Edge)
	 * @see AbstractGraph#isAdjacent(Object, Object)
	 */
	public boolean isAdjacent(int i1, int i2) {
		if (!this.isConsecutiveIndex()) throw new InconsecutiveIndicesException();
		return isAdjacent(getVertex(i1), getVertex(i2));
	}

	/**
	 * Gets the <code>i</code>th vertex of the graph<br>
	 * <b>CAUTION:</b> should only be used for iteration if the indices of vertices are not
	 * consecutive
	 * @param i the index of the vertex
	 * @return the <code>i</code>th vertex of the graph
	 */
	public Vertex getVertex(int i) {
		assert (i < size());
		return vertices.get(i);
	}

	@Override
	public Collection<? extends Vertex> getVertices() {
		return this.vertices;
	}

	@Override
	public String toString() {
		String s = new String();
		// int n = vertices.size();
		for (Vertex v : vertices) {
			s += v + ":[ ";
			for (Vertex u : vNeighbors.get(v))
				s += u + " ";
			s += "]\t";
		}
		return s;
	}

	/**
	 * Returns a subgraph induced by vertices in <code>vset</code> <br>
	 * <b>Note:</b> vertices of a proper subgraph will not have consecutive indices.
	 * @param vset a subset of vertices of this graph
	 * @return a subgraph induced by vertices in <code>vset</code>. <br>
	 */
	public LinkageGraph inducedSubgraph(ArrayList<Vertex> vset) {
		int n = vset.size();

		// returns a copy of myself if the subgraph is not proper
		if (n == size()) return new LinkageGraph(this);

		LinkageGraph g = new LinkageGraph();
		for (Vertex v : vset) {
			g.insertVertex(v);
			// g.vertices.add(v);
			// g.vNeighbors.put(v, new LinkedList<Vertex>());
		}
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < i; ++j) {
				Vertex v1 = vset.get(i), v2 = vset.get(j);
				if (this.isAdjacent(v1, v2)) g.addEdge(v1, v2);
			}
		}
		return g;
	}

	@Override
	public boolean contains(Vertex v) {
		return vNeighbors.containsKey(v);
	}

}
