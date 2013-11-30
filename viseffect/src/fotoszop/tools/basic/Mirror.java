/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotoszop.tools.basic;

import fotoszop.gui.MyDialog;
import fotoszop.gui.MyInternalFrame;
import fotoszop.tools.ToolInterface;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author student
 */
public class Mirror extends ToolInterface {

	private JMenuItem menuItem;
	private MyDialog dialog;

	public Mirror() {
		menuItem = new JMenuItem("Przerzuc obraz");
		menuItem.setIcon(this.getResourceMap().getIcon("icons.mirror"));
		menuItem.addActionListener(this);
		dialog = new MyDialog("Przerzuc obrazek", "Przerzuc obraz:", this);
		dialog.addCheckbox("Poziomo");
		dialog.addCheckbox("Pionowo");
		dialog.setShowWindows(true);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menuItem;
	}

	private void mirrorChamferlly(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] swappixels = image.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < pixels.length; ++i)
			swappixels[pixels.length - i - 1] = pixels[i];

		image.setRGB(0, 0, width, height, swappixels, 0, width);
	}

	private void mirrorHorizontally(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] swappixels = image.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				swappixels[(j + 1) * width - i - 1] = pixels[i + j * width];

		image.setRGB(0, 0, width, height, swappixels, 0, width);

	}

	private void mirrorVertically(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		int[] swappixels = image.getRGB(0, 0, width, height, null, 0, width);

		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				swappixels[j * width + i] = pixels[width * (height - j - 1) + i];

		image.setRGB(0, 0, width, height, swappixels, 0, width);

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
					if(dialog.checkboxes.get(0).isSelected() && dialog.checkboxes.get(1).isSelected()){
                        this.mirrorChamferlly(image);
                    }else if (dialog.checkboxes.get(0).isSelected()) {
						this.mirrorHorizontally(image);
					}else {
						this.mirrorVertically(image);
					}
					// this.mirrorChamferlly(image);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		//if (e.getSource() instanceof JMenuItem) {
			dialog.setVisible(true);
		//}
	}

}
