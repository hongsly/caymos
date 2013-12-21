package ccs.graph;

public class SamplePoint<E> {
	private E value;

	public SamplePoint(E value) {
		this.value = value;
	}

	public void setValue(E value) {
		this.value = value;
	}

	public E getValue() {
		return this.value;
	}
}
