package ccs.graph;

public class Pair<T> {
	// not ordered
	private T o1;
	private T o2;

	public Pair(T o1, T o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	public T getFirst() {
		return o1;
	}

	public T getSecond() {
		return o2;
	}

	public void setFirst(T o) {
		o1 = o;
	}

	public void setSecond(T o) {
		o2 = o;
	}

	static boolean same(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair<?>))
			return false;
		Pair<T> p = (Pair<T>) obj;
		// order does not matter
		return same(p.o1, this.o1) && same(p.o2, this.o2)
				|| same(p.o1, this.o2) && same(p.o2, this.o1);
	}

	public String toString() {
		return "Pair{" + o1 + ", " + o2 + "}";
	}

	public void swap() {
		T temp = o1;
		o1 = o2;
		o2 = temp;
	}
}

