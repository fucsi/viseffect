/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotoszop.tools.basic;

import fotoszop.tools.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import fotoszop.gui.MyDialog;
import fotoszop.gui.MyInternalFrame;

/**
 * 
 * @author student
 */
public class Negative extends ToolInterface {

	private JMenuItem menuItem;
	private MyDialog dialog;

	public Negative() {
		menuItem = new JMenuItem("Negatyw obrazu");
		menuItem.setIcon(this.getResourceMap().getIcon("icons.negative"));
		menuItem.addActionListener(this);
		dialog = new MyDialog("Wykonaj negatyw na wybranym obrazie",
				"Potem nacisnij OK aby kontynowac", this);
		dialog.setShowWindows(true);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menuItem;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		JOptionPane optionPane = dialog.optionPane;
		String btnString1 = dialog.btnString1, btnString2 = dialog.btnString2;
		if (dialog.isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(prop))) {
			Object value = optionPane.getValue();
			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				return;
			}
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			// Tu jest miejsce w ktorym sie cokolwiek zmienia: OK i Cancel
			if (btnString1.equals(value)) {
				List<MyInternalFrame> frames = dialog.getSelectedWindows();
				List<BufferedImage> images = new ArrayList<BufferedImage>();
				for (MyInternalFrame frame : frames) {
					BufferedImage image = frame.getPicture().getImage();
					this.negative(image);
					frame.repaint();
				}
				dialog.setVisible(false);
				dialog.reset();
			} else if (btnString2.equals(value)) {
				dialog.setVisible(false);
				dialog.reset();
			}
		}
	}

	private void negative(BufferedImage image) {
		int[] pixels = image.getRaster().getPixels(0, 0, image.getWidth(),
				image.getHeight(), (int[]) null);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = ~pixels[i];
		}
		image.getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(),
				pixels);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//if (e.getSource() instanceof JMenuItem) {
			dialog.setVisible(true);
		//}
	}

}
