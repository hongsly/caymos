package ccs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ccs.graph.Point2D;
import ccs.graph.Vertex;

public class FixAxisListener implements ActionListener, MouseListener {
	private FixAxisListener() {
	}

	private static FixAxisListener me = new FixAxisListener();

	public static FixAxisListener getInstance() {
		return me;
	}

	private boolean isPickingAxis = false;

	private Vertex o, x;

	public Vertex getO() {
		return o;
	}

	private static final int clickPrecision = 15;

	public void mouseClicked(MouseEvent e) {
		if (!isPickingAxis)
			return;

		Point2D clickP = new Point2D(e.getX(), e.getY());
		TreeDecompModel tdModel = TreeDecompModel.getInstance();

		if (o == null) {
			o = tdModel.getVertex(clickP, clickPrecision);
		} else {
			x = tdModel.getVertex(clickP, clickPrecision);
			tdModel.setAxis(o, x);
			o = null;
			x = null;
			isPickingAxis = false;
		}
		GPanel.getInstance().repaint();
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		// control.fixAxisButton is clicked
		isPickingAxis = true;
	}

}
