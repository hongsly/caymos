package ccs.graph;

import java.util.Collection;
import java.util.List;

import java.lang.UnsupportedOperationException;

/**
 * Abstract class for undirected graphs with generic type vertices.
 * 
 * @param <T>
 *            type of vertex
 */
public abstract class AbstractGraph<T> {
	/**
	 * Returns the number of vertices in the graph.
	 * @return the number of vertices in the graph
	 */
	public abstract int size();

	/**
	 * @param v a vertex of the graph
	 * @return the list of vertices adjacent to v
	 */
	public abstract List<T> getNeighbors(T v);

	/**
	 * (Optional) Insert a new vertex.
	 * 
	 * @return the new vertex added.
	 * @throws UnsupportedOperationException
	 *             if unimplemented in subclass
	 */
	public T addVertex() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return true if the vertex is successfully removed
	 */
	public abstract boolean removeVertex(T v);

	/**
	 * Returns whether the graph contains the given vertex
	 * @param v any vertex
	 * @return <code>true</code> if the graph contains the given vertex
	 */
	public abstract boolean contains(T v);

	/**
	 * Add an undirected edge between two vertices. No edge will be add if the
	 * two vertices are already adjacent.
	 * 
	 * @param v1
	 * @param v2
	 */
	public void addEdge(T v1, T v2) {
		if (v1 == v2 || isAdjacent(v1, v2)) return;
		assert (contains(v1));
		assert (contains(v2));
		getNeighbors(v1).add(v2);
		getNeighbors(v2).add(v1);
	}

	/**
	 * Remove the edge between two vertices. The edge must exist when calling
	 * the method.
	 * 
	 * @param v1
	 * @param v2
	 * @return <code>true</code> If the edge is successfully removed.
	 */
	public boolean removeEdge(T v1, T v2) {
		if (!this.isAdjacent(v1, v2)) return false;
		getNeighbors(v1).remove(v2);
		getNeighbors(v2).remove(v1);
		return true;
	}

	/**
	 * Checks whether two vertices are adjacent in the graph
	 * @param v1
	 * @param v2
	 * @return true if v1 and v2 are adjacent
	 */
	public boolean isAdjacent(T v1, T v2) {
		if (v1 == v2) return false;
		assert (contains(v1));
		assert (contains(v2));
		boolean r = (getNeighbors(v1).contains(v2));
		assert (r == getNeighbors(v2).contains(v1));
		return r;
	}

	/**
	 * Get the collection of vertices of the graph.<br>
	 * <b>CAUTION</b>: modifying this collection may change the graph!
	 * @return A Collection containing vertices of the graph.
	 */
	public abstract Collection<? extends T> getVertices();

	/**
	 * @param v
	 * @return the number of adjacent vertices of v
	 */
	public int degree(T v) {
		assert (contains(v));
		return getNeighbors(v).size();
	}

}
