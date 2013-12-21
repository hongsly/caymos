package ccs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * When click a draw option button, automatically unselect other draw option
 * buttons, and clear drawing buffer of GPanel
 * 
 */
public class DrawingListener implements ActionListener {

	private JToggleButton drawOptionBtns[];

	public DrawingListener(JToggleButton drawOptionBtns[]) {
		this.drawOptionBtns = drawOptionBtns;
	}

	public void actionPerformed(ActionEvent e) {
		for (JToggleButton b : drawOptionBtns) {
			if (b != e.getSource())
				b.setSelected(false);
		}

		GPanel.getInstance().clearDrawingBuffer();
	}
}