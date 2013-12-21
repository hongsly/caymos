package ccs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ccs.graph.Graph;

public class PathPickListener implements ActionListener {

	private ControlPanel control = ControlPanel.getInstance();

	private TreeDecompModel tdModel = TreeDecompModel.getInstance();
	private CCSModel ccsModel = CCSModel.getInstance();
	private MotionModel motionModel = MotionModel.getInstance();

	public void actionPerformed(ActionEvent e) {
		AbstractButton b = (AbstractButton) e.getSource();
		int picking = control.getPickingEnd(b);
		assert (picking == 1 || picking == 2);

		// assert (status.isPicking());
		if (control.getPathType() == 2) {
			Graph real = tdModel.getGraphClone();
			if (picking == 1)
				motionModel.setStartRealization(real);
			else
				motionModel.setEndRealization(real);
			// main.graphPanel.repaint();
		}
		// } else if (status == PathStatus.cayleyPicking) {
		double val = ccsModel.getCurrent();
		if (picking == 1)
			motionModel.setStartCayleyConfig(val);
		else
			motionModel.setEndCayleyConfig(val);

		control.checkGenPathBtn();
		
		GPanel.getInstance().repaint();
		CCSPanel.getInstance().repaint();
		
		// main.ccsInfo.ccsPanel.repaint();
	}

}
