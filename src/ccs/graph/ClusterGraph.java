package ccs.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ClusterGraph extends AbstractGraph<ClusterGraphNode> {
	HashSet<ClusterGraphNode> nodes;
	HashMap<Vertex, ClusterGraphNode> vertices;
	HashMap<Cluster, ClusterGraphNode> clusters;

	HashMap<ClusterGraphNode, LinkedList<ClusterGraphNode>> vNeighbors;

	public ClusterGraph() {
		this.nodes = new HashSet<ClusterGraphNode>();
		this.vNeighbors = new HashMap<ClusterGraphNode, LinkedList<ClusterGraphNode>>();
		vertices = new HashMap<Vertex, ClusterGraphNode>();
		clusters = new HashMap<Cluster, ClusterGraphNode>();
	}

	@Override
	public Object clone() {
		ClusterGraph g = new ClusterGraph();
		g.nodes = new HashSet<ClusterGraphNode>(nodes);
		g.vertices = new HashMap<Vertex, ClusterGraphNode>(vertices);
		g.clusters = new HashMap<Cluster, ClusterGraphNode>(clusters);
		g.vNeighbors = new HashMap<ClusterGraphNode, LinkedList<ClusterGraphNode>>();
		for (ClusterGraphNode node : vNeighbors.keySet())
			g.vNeighbors.put(node,
					new LinkedList<ClusterGraphNode>(vNeighbors.get(node)));
		return g;
	}

	@Override
	public LinkedList<ClusterGraphNode> getNeighbors(ClusterGraphNode node) {
		return vNeighbors.get(node);
	}

	public LinkedList<ClusterGraphNode> getNeighbors(Vertex v) {
		assert (vertices.containsKey(v));
		return getNeighbors(vertices.get(v));
	}

	public LinkedList<ClusterGraphNode> getNeighbors(Cluster c) {
		assert (clusters.containsKey(c));
		return getNeighbors(clusters.get(c));
	}

	@Override
	public int size() {
		return nodes.size();
	}

	public ClusterGraphNode addVertex(ClusterGraphNode node) {
		nodes.add(node);
		if (node.isVertex)
			vertices.put(node.v, node);
		else
			clusters.put(node.c, node);
		vNeighbors.put(node, new LinkedList<ClusterGraphNode>());
		return node;
	}

	public ClusterGraphNode addVertex(Vertex v) {
		return addVertex(new ClusterGraphNode(v));
	}

	public ClusterGraphNode addVertex(Cluster c) {
		return addVertex(new ClusterGraphNode(c));
	}

	/**
	 * Add edges from cluster c to each vertex in vertices
	 */
	public void addEdges(Cluster c, Vertex... vertices) {
		for (Vertex v : vertices)
			addEdge(new ClusterGraphNode(c), new ClusterGraphNode(v));
	}

	/**
	 * Add edges from cluster c to each vertex in c
	 */
	public void addEdges(Cluster c) {
		for (Vertex v : c.getVertices())
			addEdge(new ClusterGraphNode(c), new ClusterGraphNode(v));
	}

	@Override
	public boolean removeVertex(ClusterGraphNode node) {
		if (!this.contains(node))
			return false;
		
		LinkedList<ClusterGraphNode> neighbors = this.getNeighbors(node);
		for (ClusterGraphNode u : neighbors) {
			getNeighbors(u).remove(node);
		}
		neighbors.clear();
		nodes.remove(node);
		vNeighbors.remove(node);
		if (node.isVertex)
			vertices.remove(node.v);
		else
			clusters.remove(node.c);

		return true;
	}

	public boolean removeVertex(Vertex v) {
		return this.removeVertex(vertices.get(v));
	}

	public boolean removeVertex(Cluster c) {
		return this.removeVertex(clusters.get(c));
	}

	/**
	 * Remove the construction step (v > (v1, v2)), by removing c1
	 * and c2 except for the base pair of vertices (v1,v2)
	 * 
	 * @param c1
	 * @param c2
	 * @param v1
	 * @param v2
	 * @param v
	 */
	public void removeStep(Cluster c1, Cluster c2, Vertex v1, Vertex v2,
			Vertex v) {
		for (Vertex u : c1.vertices)
			if (u != v1)
				removeVertex(u);
		for (Vertex u : c2.vertices)
			if (u != v2 && u != v)
				removeVertex(u);
		removeVertex(c1);
		removeVertex(c2);
	}

	/**
	 * !!!! only apply to instances where only shared vertices are retained.
	 * TODO modify this by moving the sharedVertices field of class Cluster
	 * 
	 * @param v
	 * @return
	 */
	boolean isLastLevel(Vertex v) {
		int d = degree(v);
		if (d != 2)
			return false;
		Cluster c1 = getClusters(v).getFirst(), c2 = getClusters(v).getLast();
		if (degree(c1) > 2 || degree(c2) > 2)
			return false;
		return true;
	}

	public ClusterGraphNode mergeClusters(ClusterGraphNode... nodes) {
		assert (nodes.length == 3);
		Cluster[] cs = { nodes[0].c, nodes[1].c, nodes[2].c };
		Cluster c = Cluster.merge(cs);
		ClusterGraphNode newNode = this.addVertex(c);
		for (Vertex v : c.getVertices()) {
			this.addEdge(newNode, vertices.get(v));
		}
		for (ClusterGraphNode node : nodes)
			this.removeVertex(node);
		return newNode;
	}

	@Override
	public Collection<? extends ClusterGraphNode> getVertices() {
		return nodes;
	}

	@Override
	public boolean contains(ClusterGraphNode v) {
		return nodes.contains(v);
	}

	@Override
	public String toString() {
		String s = new String();
		// int n = vertices.size();
		for (ClusterGraphNode v : nodes) {
			s += v + ": ";
			for (ClusterGraphNode u : vNeighbors.get(v))
				s += u + " ";
			s += "\n";
		}
		return s;
	}

	public int degree(Vertex v) {
		return degree(vertices.get(v));
	}

	public int degree(Cluster c) {
		return degree(clusters.get(c));
	}

	/**
	 * @return the set of clusters containing v
	 */
	public LinkedList<Cluster> getClusters(Vertex v) {
		// return vClusters.get(v);
		LinkedList<Cluster> l = new LinkedList<Cluster>();
		for (ClusterGraphNode node : getNeighbors(v)) {
			l.add(node.c);
		}
		return l;
	}

	/**
	 * @param v1
	 *            can be an inner vertex of a cluster
	 * @param v2
	 *            must be a shared vertex
	 */
	public Cluster getClusterBetween(Vertex v1, Vertex v2) {
		LinkedList<Cluster> l = getClusters(v1);
		for (Cluster c : l) {
			if (c.containsSharedVertex(v2))
				return c;
		}
		// for (Cluster c1 : getClusters(v1))
		// for (Cluster c2 : getClusters(v2))
		// if (c1 == c2)
		// return c1;
		return null;
	}
}

class ClusterGraphNode {
	boolean isVertex = true;
	Vertex v;
	Cluster c;

	ClusterGraphNode(Vertex v) {
		this.v = v;
	}

	ClusterGraphNode(Cluster c) {
		this.c = c;
		isVertex = false;
	}

	@Override
	public String toString() {
		return (isVertex ? v.toString() : c.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ClusterGraphNode))
			return false;
		ClusterGraphNode that = (ClusterGraphNode) o;
		return (that.isVertex == this.isVertex && (isVertex ? that.v
				.equals(this.v) : that.c.equals(this.c)));
	}

	@Override
	public int hashCode() {
		if (isVertex) {
			return v.hashCode();
		} else
			return c.hashCode();
	}
}