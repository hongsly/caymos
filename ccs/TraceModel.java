package ccs;

import java.util.ArrayList;

import ccs.graph.Vertex;

public class TraceModel {
	private TraceModel() {
		tracingVertices = new ArrayList<Vertex>();
		traces = new ArrayList<ArrayList<Trace>>();
	}

	private static TraceModel me = new TraceModel();

	public static TraceModel getInstance() {
		return me;
	}

	private ArrayList<Vertex> tracingVertices;

	private ArrayList<ArrayList<Trace>> traces;

	public ArrayList<Vertex> getTracingVertices() {
		return tracingVertices;
	}

	public ArrayList<ArrayList<Trace>> getTraces() {
		return traces;
	}

	public void clear() {
		tracingVertices.clear();
		traces.clear();
	}
}
