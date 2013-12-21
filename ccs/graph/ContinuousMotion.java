package ccs.graph;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ccs.Debug;

/**
 * Interface for MotionPath and ConnectedComponent
 */
public abstract class ContinuousMotion implements List<Node> {
	protected TreeDecomp t;

	protected ArrayList<Node> nodes;

	public ContinuousMotion(TreeDecomp t) {
		this.t = t;
		nodes = new ArrayList<Node>();
	}

	public abstract Node startNode();

	public abstract Node endNode();

	public AbstractList<SolutionType> getSolutionTypes() {
		ArrayList<SolutionType> l = new ArrayList<SolutionType>();
		for (Node n : this) {
			l.add(n.getSolutionType());
		}
		return l;
	}

	public String toString() {
		return nodes.toString();
	}

	/*
	 * public AbstractList<Node> getNodes() { // return nodes;
	 */

	public int size() {
		return nodes.size();
	}

	public Node set(int index, Node n) {
		return nodes.set(index, n);
	}

	public boolean add(Node n) {
		// Debug.msg("adding node " + n);
		return nodes.add(n);
	}

	public Node get(int index) {
		return nodes.get(index);
	}

	public void clear() {
		nodes.clear();
	}

	public boolean contains(Object arg0) {
		return nodes.contains(arg0);
	}

	public boolean containsAll(Collection<?> arg0) {
		return nodes.containsAll(arg0);
	}

	public int indexOf(Object arg0) {
		return nodes.indexOf(arg0);
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	/**
	 * Not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public int lastIndexOf(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public ListIterator<Node> listIterator() {
		return nodes.listIterator();
	}

	public ListIterator<Node> listIterator(int arg0) {
		return nodes.listIterator(arg0);
	}

	/**
	 * @param fromIndex
	 *            included
	 * @param toIndex
	 *            not included
	 * @see java.util.List#subList(int, int)
	 */
	public List<Node> subList(int fromIndex, int toIndex) {
		return nodes.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return nodes.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		return nodes.toArray(arg0);
	}

	public void add(int arg0, Node arg1) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends Node> arg0) {
		for (Node n : arg0)
			if (!add(n))
				return false;
		return true;
	}

	public boolean addAll(int arg0, Collection<? extends Node> arg1) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public Node remove(int arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Using a NodeSampler to sample every node.
	 * 
	 * @param <E>
	 * 
	 * @param s
	 *            a sampler defining sampling method for each node
	 * @return a sequence of sample points obtained from each node
	 */
	public <E> ContinuousMotionSamples<E> sample(NodeSampler<E> s) {
		ContinuousMotionSamples<E> samples = new ContinuousMotionSamples<E>(
				this);
		for (Node n : this) {
			samples.addAll(s.sample(n));
		}
		return samples;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ContinuousMotion))
			return false;
		ContinuousMotion that = (ContinuousMotion) obj;
		if (size() != that.size())
			return false;
		ListIterator<Node> iterThis = this.listIterator(), iterThat = that
				.listIterator();
		while (iterThis.hasNext()) {
			if (!iterThis.next().equals(iterThat.next()))
				return false;
		}
		return true;
	}

}

/**
 * Contains one consecutive interval on a continuous motion.
 * 
 */
class Node {
	/**
	 * Start Cayley configuration
	 */
	private double start;

	/**
	 * End Cayley configuration
	 */
	private double end;

	private Interval interval;

	private SolutionType type;

	public Node(double start, double end, Interval interval, SolutionType type) {
		this.start = start;
		this.end = end;
		this.interval = interval;
		this.type = type.clone();
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}

	public Interval getInterval() {
		return interval;
	}

	public SolutionType getSolutionType() {
		return type;
	}

	public double getLength() {
		return Math.abs(getStart() - getEnd());
	}

	public boolean contains(double lf, SolutionType type) {
		if (!type.equals(getSolutionType()))
			return false;
		return Util.between(lf, getStart(), getEnd());
	}

	protected double sampleCayleyAt(double percentage) {
		assert (percentage <= 1 && percentage >= 0);
		double l = getLength() * percentage;
		return (getStart() < getEnd() ? getStart() + l : getStart() - l);
	}

	@Override
	public String toString() {
		return getStart() + "~" + getEnd() + "\t"
				+ getSolutionType().toString();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (!(obj instanceof Node))
			return false;
		Node that = (Node) obj;
		return start == that.start && end == that.end
				&& interval.equals(that.interval) && type.equals(that.type);
	}

}
