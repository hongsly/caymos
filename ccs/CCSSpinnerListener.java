package ccs;

import java.util.ArrayList;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ccs.graph.Graph;
import ccs.graph.SolutionType;

public class CCSSpinnerListener implements ChangeListener {

	TreeDecompModel tdModel = TreeDecompModel.getInstance();
	CCSModel ccsModel = CCSModel.getInstance();

	public void stateChanged(ChangeEvent e) {
		JSpinner s = ((JSpinner) (e.getSource()));
		if (!s.isVisible())
			return;
		Double val = (Double) s.getValue();

		// only realize those displayed on ccsPanel
		if (ccsModel.contains(val) == null)
			return;

		ccsModel.setCurrent(val);

		SolutionType forward = tdModel.getForwardSolutionType();
		Graph g = tdModel.tryRealize(val, forward);
		if (g != null)
			tdModel.setPoints(g);
		else if (!ccsModel.isOriented()) {
			ArrayList<Graph> gs = tdModel.tryRealize(val);
			if (!gs.isEmpty())
				tdModel.setPoints(gs.get(0));
		}
		// main.graphPanel.repaint();
		// TODO: ccs Panel refresh
		
		GPanel.getInstance().repaint();
		FloatingPanels.getInstance().repaintAll();
		CCSPanel.getInstance().repaint();
	}

}
