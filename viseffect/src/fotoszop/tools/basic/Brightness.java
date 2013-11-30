package fotoszop.tools.basic;

import fotoszop.tools.*;
import fotoszop.gui.MyDialog;
import fotoszop.gui.MyInternalFrame;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
 * @author kalkan
 */
public class Brightness extends ToolInterface {

	private JMenuItem menuItem;
	private MyDialog dialog;

	public Brightness() {
		menuItem = new JMenuItem("Zmiana jasnosci");
		menuItem.setIcon(this.getResourceMap().getIcon("icons.brightness"));
		menuItem.addActionListener(this);
		dialog = new MyDialog("Dopasuj jasnosc",
				"Zmien poziom jasnosci dla wyybranego obrazka", this);
		dialog.addIntegerSlider(-255, 255, 0, null);
		dialog.setShowWindows(true);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menuItem;
	}

	private void brighten(int offset, BufferedImage image) {
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
				null, 0, image.getWidth());
		int alpha, red, green, blue;
		for (int i = 0; i < pixels.length; i++) {
			alpha = pixels[i] & 0xFF000000;
			red = pixels[i] >> 16 & 0xFF;
			green = pixels[i] >> 8 & 0xFF;
			blue = pixels[i] & 0xFF;
			red += offset;
			if (red < 0) {
				red = 0;
			} else if (red > 255) {
				red = 255;
			}
			blue += offset;
			if (blue < 0) {
				blue = 0;
			} else if (blue > 255) {
				blue = 255;
			}
			green += offset;
			if (green < 0) {
				green = 0;
			} else if (green > 255) {
				green = 255;
			}
			pixels[i] = (alpha | red << 16 | green << 8 | blue);
		}
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0,
				image.getWidth());
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
					this.brighten(dialog.sliders.get(0).getValue(), image);
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
		if (e.getSource() instanceof JMenuItem) {
			dialog.setVisible(true);
		}
	}
}
