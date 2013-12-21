package ccs;

import ccs.graph.Edge;

/**
 * Organizing floating panels
 */
public class FloatingPanels {
	private FloatingPanels() {
		curve3Dpanel = new Curve3DContainer();
		curve3Dpanel.setVisible(false);

	}

	private static FloatingPanels me = new FloatingPanels();

	public static FloatingPanels getInstance() {
		return me;
	}

	private Curve3DContainer curve3Dpanel;
	private EdgeLengthContainer edgeLengthPanel;

	public boolean addCurve3D(Curve3DMotion c) {
		return curve3Dpanel.addCurve(c);
	}

	public boolean removeCurve3D(Curve3D c) {
		return curve3Dpanel.removeCurve(c);
	}

	public void clearCurves() {
		curve3Dpanel.clear();
	}

	public void showCurve3DPanel() {
		curve3Dpanel.setVisible(true);
		curve3Dpanel.setAlwaysOnTop(true);
		curve3Dpanel.createDialog();
	}

	public void closeCurve3DPanel() {
		curve3Dpanel.closeDialog();
		curve3Dpanel.setVisible(false);
	}

	public void repaintAll() {
		if (curve3Dpanel.isVisible()) {
			// Debug.warnMsg("painting 3d panel");
			curve3Dpanel.repaint();
		}

		if (edgeLengthPanel.isVisible())
			edgeLengthPanel.repaint();
	}

	public Edge[] getProjectionEdges() {
		return edgeLengthPanel.getProjectionEdges();
	}

	public void showEdgeLengthPanel() {
		edgeLengthPanel.createDialog();
		edgeLengthPanel.setAlwaysOnTop(true);
	}

	public void closeEdgeLengthPanel() {
		edgeLengthPanel.closeDialog();
	}

	public int indexOf(Edge e) {
		return edgeLengthPanel.indexOf(e);
	}

	public void refresh() {
		if (TreeDecompModel.getInstance().isLow()) {
			edgeLengthPanel = new EdgeLengthContainer(TreeDecompModel
					.getInstance().getTd());
			edgeLengthPanel.setVisible(false);
		}
		// edgeLengthPanel.refresh(TreeDecompModel.getInstance().getTd());
	}
}
