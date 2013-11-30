/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.filters;

import fotoszop.gui.ArrayDialog;
import fotoszop.gui.MyInternalFrame;
import fotoszop.gui.WindowManager;
import fotoszop.tools.ToolInterface;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * 
 * @author student
 */
public class SplotOgolny extends ToolInterface {

	private double[] filtr = new double[9];
	double red, green, blue;
	int r, g, b;
	JMenuItem menu;
	ArrayDialog dialog;
	private MyInternalFrame savedFrame;

	public SplotOgolny() {
		menu = new JMenuItem("Splot ogolny");
		menu.setIcon(this.getResourceMap().getIcon("icons.splotgeneral"));
		menu.addActionListener(this);
		dialog = new ArrayDialog("Splot", "Wpisz maske filtru", this);
	}

	private long splataj(BufferedImage image) {
		long TIME = System.currentTimeMillis();
		int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
				null, 0, image.getWidth());
		int[] newrgb = new int[rgb.length];
		int m = image.getHeight();
		int n = image.getWidth();

		for (int i = 0; i < n; i++) {
			newrgb[i] = rgb[i];
			newrgb[rgb.length - 1 - i] = rgb[rgb.length - 1 - i];
		}
		for (int i = 1; i < m - 1; i++) {
			newrgb[i * n] = rgb[i * n];
			newrgb[(i + 1) * n - 1] = rgb[(i + 1) * n - 1];
			for (int j = 1; j < n - 1; j++) {
				red = green = blue = 0;
				for (int k = -1; k <= 1; k++) {
					for (int l = -1; l <= 1; l++) {
						red += ((rgb[(i + k) * n + j + l] >>> 16) & 0xFF)
								* filtr[4 + l + 3 * k];
						green += ((rgb[(i + k) * n + j + l] >>> 8) & 0xFF)
								* filtr[4 + l + 3 * k];
						blue += (rgb[(i + k) * n + j + l] & 0xFF)
								* filtr[4 + l + 3 * k];

					}
				}
				konwertuj();
				newrgb[n * i + j] = (r << 16 | g << 8 | b);
			}
		}
		TIME = System.currentTimeMillis() - TIME;
		image.setRGB(0, 0, n, m, newrgb, 0, n);
		return TIME;
	}

	private void konwertuj() {
		if (red > 255) {
			red = 255;
		} else if (red < 0) {
			red = 0;
		}
		if (blue > 255) {
			blue = 255;
		} else if (blue < 0) {
			blue = 0;
		}
		if (green > 255) {
			green = 255;
		} else if (green < 0) {
			green = 0;
		}
		r = (int) Math.round(red);
		g = (int) Math.round(green);
		b = (int) Math.round(blue);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		for (MyInternalFrame frame : WindowManager.getInstance().getFrames()) {
			if (frame.isSelected()) {
				savedFrame = frame;
				dialog.setVisible(true);
				return;
			}
		}
		JOptionPane.showMessageDialog(dialog, "Wybierz obrazek", "error",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menu;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		JOptionPane optionPane = dialog.optionPane;
		String btnString1 = "OK";
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
				JTextField[] fields = dialog.txtFields;
				try {
					for (int i = 0; i < fields.length; i++) {
						String text = fields[i].getText();
						filtr[i] = Double.parseDouble(text);
					}
					long TIME = splataj(savedFrame.getPicture().getImage());
					System.out.println("Ogolny: " + TIME);

					savedFrame.repaint();

					dialog.setVisible(false);
					JOptionPane.showMessageDialog(dialog, "" + TIME);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(dialog, "Zle dane w filtrze",
							"error", JOptionPane.ERROR_MESSAGE);

				}
			}
		}
	}

}
