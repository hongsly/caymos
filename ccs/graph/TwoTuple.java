package ccs.graph;

public class TwoTuple<T, V> {
	public T o1;
	public V o2;

	public TwoTuple(T t, V v) {
		o1 = t;
		o2 = v;
	}

	public T getFirst() {
		return o1;
	}

	public V getSecond() {
		return o2;
	}

	public void setFirst(T o) {
		o1 = o;
	}

	public void setSecond(V o) {
		o2 = o;
	}

	static boolean same(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (!(obj instanceof TwoTuple<?, ?>))
			return false;
		TwoTuple<T, V> p = (TwoTuple<T, V>) obj;
		// order matters
		return same(p.o1, this.o1) && same(p.o2, this.o2);
	}

	public String toString() {
		return "{" + o1 + ", " + o2 + "}";
	}

}
