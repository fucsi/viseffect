/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.basic;

import fotoszop.tools.*;
import fotoszop.gui.MyDialog;
import fotoszop.gui.MyInternalFrame;
import fotoszop.gui.Picture;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
 * @author kalkan
 */
public class Zoom extends ToolInterface {

	MyDialog dialog;
	private JMenuItem menuItem;
	private HashMap<String, Integer> zoomLevels = new HashMap<String, Integer>();

	public Zoom() {
		dialog = new MyDialog("Zoom", "Dopasuj poziom powiekszenia", this);
		
		dialog.addIntegerSlider(10, 1010, 100, null);
		dialog.setShowWindows(true);
		menuItem = new JMenuItem("Zoom");
		menuItem.setIcon(this.getResourceMap().getIcon("icons.zoom"));
		menuItem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//if (e.getSource() instanceof JMenuItem) {
			dialog.setVisible(true);
		//}
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
					this.zoom(dialog.sliders.get(0).getValue(), frame
							.getPicture(), frame.getTitle());
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

	private void zoom(int percent, Picture image, String title) {

		image.setZoomX(image.getZoomX() * percent / 100);
		image.setZoomY(image.getZoomY() * percent / 100);

	}
}
