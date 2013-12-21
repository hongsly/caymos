package ccs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import ccs.graph.CCS;
import ccs.graph.CCSInterface;
import ccs.graph.Interval;
import ccs.graph.SolutionType;

/**
 * Displays intervals in CCS.
 */
public class CCSPanel extends JPanel {
	private CCSPanel() {
	}

	private static CCSPanel me = new CCSPanel();

	public static CCSPanel getInstance() {
		return me;
	}

	/**
	 * the range to draw
	 */
	private double min, max;

	// static final int width, height = 150;

	/*
	 * public void setStatus(Status s) { Status oldStatus = status; status = s;
	 * 
	 * case generated: refreshCCS(); refreshCurrent(); break;
	 * 
	 * case picking: case tracing: case tracingNearest: repaint(); break; } }
	 */

	public double panelToCayley(double x) {
		double l = x / getWidth() * (max - min) + min;
		return l;
	}

	public double cayleyToPanel(double l) {
		double x = (l - min) / (max - min) * getWidth();
		return x;
	}

	private final int intervalHeight = 20;

	// emph: the main interval
	private void drawCCS(CCSInterface s, int y, boolean emph, Graphics2D g2d) {

		ArrayList<Interval> l = s.getIntervals();
		for (Interval interval : l) {
			int lower = (int) cayleyToPanel(interval.lower);
			int upper = (int) cayleyToPanel(interval.upper);

			if (emph)
				g2d.drawRect(lower, y - intervalHeight, upper - lower,
						intervalHeight);
			else
				g2d.drawLine(lower, y, upper, y);

			String s1 = String.format("%.2f", interval.lower);
			String s2 = String.format("%.2f", interval.upper);
			g2d.drawString(s1, lower, y);
			g2d.drawString(s2, upper, y);
		}

	}

	private void drawValue(double value, int y, Graphics2D g2d) {
		final int pointSize = 10;
		if (value >= 0 && CCSModel.getInstance().contains(value) != null) {
			int x = (int) cayleyToPanel(value);
			g2d.fillOval(x - pointSize / 2, y - pointSize / 2, pointSize,
					pointSize);
			String str = String.format("%.2f", value);
			g2d.drawString(str, x, y + intervalHeight / 2);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		CCSModel ccsModel = CCSModel.getInstance();
		TreeDecompModel tdModel = TreeDecompModel.getInstance();
		MotionModel motionModel = MotionModel.getInstance();

		if (!ccsModel.isGenerated())
			return;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(MyStrokes.solid_thin);

		CCSInterface ccs = ccsModel.getCCS().getFirst();
		double ccsmin = ccs.getMin(), ccsmax = ccs.getMax();
		double ccsrange = ccsmax - ccsmin;
		double border = ccsrange / 10;
		min = ccsmin - border;
		max = ccsmax + border;

		// draw intervals
		g2d.setColor(Color.black);
		int axisy = 30;
		g2d.drawLine(0, axisy, getWidth(), axisy);

		drawCCS(ccs, axisy, true, g2d);

		// draw oriented spaces

		Collection<SolutionType> list;
		if (ccsModel.isOriented()) {
			list = new ArrayList<SolutionType>();
			list.add(ccsModel.getSolutionType());
		} else {
			list = ccsModel.getTypesToDraw();
		}

		final int PADDING = 15;
		int y = axisy + PADDING;
		for (SolutionType t : list) {
			Color c = t.getColor();
			g2d.setColor(c.darker());
			drawCCS(((CCS) ccs).getOrientedCCS(t), y, false, g2d);

			// draw current value in occs
			if (tdModel.getForwardSolutionType().equals(t))
				drawValue(ccsModel.getCurrent(), y, g2d);

			y += PADDING;
		}

		// draw current value
		g2d.setColor(Color.black);
		drawValue(ccsModel.getCurrent(), axisy, g2d);

		// ============ draw path start/end cayley configuration ==============
		// if (status == Status.picking || status == Status.tracing) {
		Double config = MotionModel.getInstance().getStartCayleyConfig();
		if (config != null) {
			g2d.setColor(Color.red);
			drawValue(config, axisy, g2d);
		}
		config = MotionModel.getInstance().getEndCayleyConfig();
		if (config != null) {
			g2d.setColor(Color.blue);
			drawValue(config, axisy, g2d);
		}
		// }

		// ============ draw marked points ==============
		for (ConfigPanel c : ccsModel.getMarkedPoints()) {
			g2d.setColor(Color.DARK_GRAY);
			drawValue(c.lf, axisy, g2d);
		}

	}

	/*
	 * public void refreshCCS() { init(main.td.ccs); // if
	 * (main.ccsInfo.getCCSOption() == CCSOption.oriented) { // SolutionType
	 * type = main.td.getForwardSolutionType(); // addType(type); // }
	 * repaint(); }
	 */
}
