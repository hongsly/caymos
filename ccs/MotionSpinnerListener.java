package ccs;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ccs.graph.ConnectedComponent;
import ccs.graph.ContinuousMotion;
import ccs.graph.Graph;
import ccs.graph.SamplePoint;

public class MotionSpinnerListener implements ChangeListener {

	/**
	 * 0: pathSpinner; 1:p1; 2:p2
	 */
	private int which = 0;

	/**
	 * @param n
	 *            Indicates which spinner. 0: pathSpinner; 1:p1; 2:p2
	 */
	public MotionSpinnerListener(int n) {
		which = n;
	}

	private TreeDecompModel tdModel = TreeDecompModel.getInstance();
	private MotionModel motionModel = MotionModel.getInstance();

	public void stateChanged(ChangeEvent e) {
		DialSpinner<SamplePoint<Graph>> spinner = ((DialSpinner<SamplePoint<Graph>>) (e
				.getSource()));

		if (!spinner.isVisible())
			return;

		ContinuousMotion p;
		if (which == 0)
			p = motionModel.getMotion();
		else if (which == 1)
			p = motionModel.getM1();
		else
			p = motionModel.getM2();
		// double percentage = spinner.getPercentage();

		// ContinuousMotionSamples<Graph> pSamples = motionModel
		// .getMotionSamples();

		// int index = (int) percentage * pSamples.size();
		// SamplePoint<Graph> point = pSamples.get(index);
		SamplePoint<Graph> point = spinner.getValue();
		Graph g = point.getValue();

		if (g != null) {
			switch (which) {
			case 0:
				tdModel.setPoints(g);
				break;
			/*
			 * case 1: motionModel.getStartRealization().setPoints(g); break;
			 * case 2: motionModel.getEndRealization().setPoints(g); break;
			 */
			}

		}

		GPanel.getInstance().repaint();
		CCSModel.getInstance().refreshCurrent();
		CCSPanel.getInstance().repaint();
		FloatingPanels.getInstance().repaintAll();
	}

}
