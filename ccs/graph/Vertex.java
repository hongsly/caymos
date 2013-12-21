package ccs.graph;

import java.io.Serializable;

public class Vertex implements Serializable{

	// now vertex does not have position
	// index should only be used as name?
	// position retrieved by graph, using vertex or v.index as key?

	public int index;

	public Vertex(int index) {
		this.index = index;
	}

	public String toString() {
		return "v" + (index);
	}
}
