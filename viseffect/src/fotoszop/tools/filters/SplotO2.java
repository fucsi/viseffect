/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.filters;

import fotoszop.gui.MyInternalFrame;
import fotoszop.gui.WindowManager;
import fotoszop.tools.ToolInterface;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
 * @author kalkan
 */
public class SplotO2 extends ToolInterface {

	private double w, w1 = 1.0 / 9, w2 = 0.1, w3 = 0.0625;
	LookupTable lookup = null;
	private int[] filtr1 = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	private int[] filtr2 = { 1, 1, 1, 1, 2, 1, 1, 1, 1 };
	private int[] filtr3 = { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
	private int[] filtr = new int[9], ftmp;
	double red, green, blue;
	int r, g, b;
	JMenuItem menu;

	public SplotO2() {
		menu = new JMenuItem("Splot - wariant 2");
		menu.addActionListener(this);
	}

	private void sumuj(int rgb[], int i, int j, int m, int n) {
		red = green = blue = 0;
		int index, index2;
		for (int k = -1; k <= 1; k++) {
			for (int l = -1; l <= 1; l++) {
				index = 4 + l + 3 * k;
				index2 = (i + k) * n + j + l;
				red += lookup.get(filtr[index], (rgb[index2] >>> 16) & 0xFF);
				green += lookup.get(filtr[index], (rgb[index2] >>> 8) & 0xFF);
				blue += lookup.get(filtr[index], rgb[index2] & 0xFF);
			}
		}
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

	private void sumujPoziomoP(int rgb[], int i, int j, int m, int n) {
		int indexL = i * n + j - 2;
		int indexP = i * n + j + 1;
		int kn1, kn2;
		int ind1, ind2;
		for (int k = -1; k <= 1; k++) {
			kn1 = indexL + k * n;
			kn2 = indexP + k * n;
			ind1 = 3 * (k + 1);
			ind2 = 3 * (k + 1) + 2;
			red = red - lookup.get(filtr[ind1], (rgb[kn1] >>> 16) & 0xFF)
					+ lookup.get(filtr[ind2], (rgb[kn2] >>> 16) & 0xFF);
			green = green - lookup.get(filtr[ind1], (rgb[kn1] >>> 8) & 0xFF)
					+ lookup.get(filtr[ind2], (rgb[kn2] >>> 8) & 0xFF);
			blue = blue - lookup.get(filtr[ind1], rgb[kn1] & 0xFF)
					+ lookup.get(filtr[ind2], rgb[kn2] & 0xFF);
		}
	}

	private void sumujPoziomoL(int rgb[], int i, int j, int m, int n) {
		int indexL = i * n + j - 1;
		int indexP = i * n + j + 2;
		int kn1;
		int kn2;
		int ind1;
		int ind2;
		for (int k = -1; k <= 1; k++) {
			kn1 = indexL + k * n;
			kn2 = indexP + k * n;
			ind1 = 3 * (k + 1);
			ind2 = 3 * (k + 1) + 2;
			red = red + lookup.get(filtr[ind1], (rgb[kn1] >>> 16) & 0xFF)
					- lookup.get(filtr[ind2], (rgb[kn2] >>> 16) & 0xFF);
			green = green + lookup.get(filtr[ind1], (rgb[kn1] >>> 8) & 0xFF)
					- lookup.get(filtr[ind2], (rgb[kn2] >>> 8) & 0xFF);
			blue = blue + lookup.get(filtr[ind1], rgb[kn1] & 0xFF)
					- lookup.get(filtr[ind2], rgb[kn2] & 0xFF);
		}
	}

	private void sumujPionowoD(int rgb[], int i, int j, int m, int n) {
		int indexG = (i - 2) * n + j;
		int indexD = (i + 1) * n + j;
		int kl1, kl2;
		int l1, l7;
		for (int l = -1; l <= 1; l++) {
			kl1 = indexG + l;
			kl2 = indexD + l;
			l1 = l + 1;
			l7 = l + 7;
			red = red - lookup.get(filtr[l1], (rgb[kl1] >>> 16) & 0xFF)
					+ lookup.get(filtr[l7], (rgb[kl2] >>> 16) & 0xFF);
			green = green - lookup.get(filtr[l1], (rgb[kl1] >>> 8) & 0xFF)
					+ lookup.get(filtr[l7], (rgb[kl2] >>> 8) & 0xFF);
			blue = blue - lookup.get(filtr[l1], rgb[kl1] & 0xFF)
					+ lookup.get(filtr[l7], rgb[kl2] & 0xFF);
		}
	}

	private long splataj3(BufferedImage image) {
		int m = image.getHeight();
		int n = image.getWidth();
		int[] rgb = image.getRGB(0, 0, n, m, null, 0, n);
		int[] newrgb = new int[rgb.length];

		long TIME = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			newrgb[i] = rgb[i];
			newrgb[rgb.length - 1 - i] = rgb[rgb.length - 1 - i];
		}
		for (int i = 1; i < m; i++) {
			newrgb[i * n] = rgb[i * n];
			newrgb[(i + 1) * n - 1] = rgb[(i + 1) * n - 1];
		}
		sumuj(rgb, 1, 1, m, n);
		konwertuj();
		newrgb[n + 1] = (r << 16 | g << 8 | b);

		for (int j = 2; j < n - 1; j++) {
			sumujPoziomoP(rgb, 1, j, m, n);
			konwertuj();
			newrgb[n + j] = (r << 16 | g << 8 | b);
		}
		for (int i = 2; i < m - 1; i++) {
			sumujPionowoD(rgb, i, n - 2, m, n);
			konwertuj();
			newrgb[i * n + n - 2] = (r << 16 | g << 8 | b);
			for (int j = n - 3; j >= 1; j--) {
				sumujPoziomoL(rgb, i, j, m, n);
				konwertuj();
				newrgb[n * i + j] = (r << 16 | g << 8 | b);
			}
			if (!(++i < m - 1)) {
				break;
			}
			sumujPionowoD(rgb, i, 1, m, n);
			konwertuj();
			newrgb[i * n + 1] = (r << 16 | g << 8 | b);
			for (int j = 2; j <= n - 2; j++) {
				sumujPoziomoP(rgb, i, j, m, n);
				konwertuj();
				newrgb[n * i + j] = (r << 16 | g << 8 | b);
			}
		}
		TIME = System.currentTimeMillis() - TIME;
		image.setRGB(0, 0, n, m, newrgb, 0, n);
		return TIME;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object[] possibilities = { "1", "2", "3" };
		String s = (String) JOptionPane.showInputDialog(null,
				"Wybierz filtr:\n" + "  1)        2)        3)\n"
						+ "1 1 1    1 1 1    1 2 1\n"
						+ "1 1 1    1 2 1    2 4 2\n"
						+ "1 1 1    1 1 1    1 2 1\n"
						+ "w = 1/9    w=1/10      w=1/16", "Splot",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "1");

		if ((s != null) && (s.length() > 0)) {
			if (s.compareTo("1") == 0) {
				w = w1;
				ftmp = filtr1;
				lookup = new LookupTable(1);
				lookup.populate(new int[] { 1 }, w);
			} else if (s.compareTo("2") == 0) {
				w = w2;
				ftmp = filtr2;
				lookup = new LookupTable(2);
				lookup.populate(new int[] { 1, 2 }, w);
			} else if (s.compareTo("3") == 0) {
				w = w3;
				ftmp = filtr3;
				lookup = new LookupTable(3);
				lookup.populate(new int[] { 1, 2, 4 }, w);
			}
			for (int i = 0; i < filtr.length; i++) {
				filtr[i] = ftmp[i] / 2;
			}
			for (MyInternalFrame frame : WindowManager.getInstance()
					.getFrames()) {
				if (frame.isSelected()) {
					long TIME;
					BufferedImage image = frame.getPicture().getImage();
					TIME = splataj3(image);

					System.out.println("splot: " + TIME);

					frame.repaint();
					JOptionPane.showMessageDialog(null, "" + TIME);
					return;
				}
			}
		}

	}

	@Override
	public JMenuItem getJMenuItem() {
		return menu;
	}
}
