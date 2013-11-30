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
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
 * @author kalkan
 */
public class SplotO1 extends ToolInterface {

	private double w, w1 = 1.0 / 9, w2 = 0.1, w3 = 0.0625;
	private double[] filtr1 = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	private double[] filtr2 = { 1, 1, 1, 1, 2, 1, 1, 1, 1 };
	private double[] filtr3 = { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
	private double[] filtr = new double[9], ftmp;
	double red, green, blue;
	int r, g, b;
	JMenuItem menu;

	public SplotO1() {
		menu = new JMenuItem("Splot - wariant 1");
		menu.addActionListener(this);
	}

	private void sumuj(int rgb[], int i, int j, int m, int n) {
		red = green = blue = 0;
		int index, index2;
		for (int k = -1; k <= 1; k++) {
			for (int l = -1; l <= 1; l++) {
				index = 4 + l + 3 * k;
				index2 = (i + k) * n + j + l;
				red += ((rgb[index2] >>> 16) & 0xFF) * filtr[index];
				green += ((rgb[index2] >>> 8) & 0xFF) * filtr[index];
				blue += (rgb[index2] & 0xFF) * filtr[index];
			}
		}
	}

	private void konwertuj() {
		/*
		 * if (red > 255) { red = 255; } else if (red < 0) { red = 0; } if (blue
		 * > 255) { blue = 255; } else if (blue < 0) { blue = 0; } if (green >
		 * 255) { green = 255; } else if (green < 0) { green = 0; }
		 */
		r = (int) Math.round(red);
		g = (int) Math.round(green);
		b = (int) Math.round(blue);
	}

	private void sumujPoziomoP(int rgb[], int i, int j, int m, int n) {
		int indexL = i * n + j - 2;
		int indexP = i * n + j + 1;
		int kn1, kn2;
		int ind2;
		int ind1;
		for (int k = -1; k <= 1; k++) {
			kn1 = indexL + k * n;
			kn2 = indexP + k * n;
			ind1 = 3 * (k + 1);
			ind2 = 3 * (k + 1) + 2;
			red = red - ((rgb[kn1] >>> 16) & 0xFF) * filtr[ind1]
					+ ((rgb[kn2] >>> 16) & 0xFF) * filtr[ind2];
			green = green - ((rgb[kn1] >>> 8) & 0xFF) * filtr[ind1]
					+ ((rgb[kn2] >>> 8) & 0xFF) * filtr[ind2];
			blue = blue - (rgb[kn1] & 0xFF) * filtr[ind1] + (rgb[kn2] & 0xFF)
					* filtr[ind2];
		}
	}

	private void sumujPoziomoL(int rgb[], int i, int j, int m, int n) {
		int indexL = i * n + j - 1;
		int indexP = i * n + j + 2;
		int kn2;
		int ind1;
		int ind2;
		int kn1;
		for (int k = -1; k <= 1; k++) {
			kn1 = indexL + k * n;
			kn2 = indexP + k * n;
			ind1 = 3 * (k + 1);
			ind2 = 3 * (k + 1) + 2;
			red = red + ((rgb[kn1] >>> 16) & 0xFF) * filtr[ind1]
					- ((rgb[kn2] >>> 16) & 0xFF) * filtr[ind2];
			green = green + ((rgb[kn1] >>> 8) & 0xFF) * filtr[ind1]
					- ((rgb[kn2] >>> 8) & 0xFF) * filtr[ind2];
			blue = blue + (rgb[kn1] & 0xFF) * filtr[ind1] - (rgb[kn2] & 0xFF)
					* filtr[ind2];
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
			red = red - ((rgb[kl1] >>> 16) & 0xFF) * filtr[l1]
					+ ((rgb[kl2] >>> 16) & 0xFF) * filtr[l7];
			green = green - ((rgb[kl1] >>> 8) & 0xFF) * filtr[l1]
					+ ((rgb[kl2] >>> 8) & 0xFF) * filtr[l7];
			blue = blue - (rgb[kl1] & 0xFF) * filtr[l1] + (rgb[kl2] & 0xFF)
					* filtr[l7];
		}
	}

	private long splataj2(BufferedImage image) {
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
		/*
		 * Integer[] window = new Integer[6]; Comparator<Integer> comp = new
		 * Comparator<Integer>() {
		 * 
		 * public int compare(Integer o1, Integer o2) { if(o1.equals(o2)) {
		 * return 0; } if( ((o1>>>16)&0xFF)+((o1>>>8)&0xFF)+(o1 & 0xFF) <
		 * ((o2>>>16)&0xFF)+((o2>>>8)&0xFF)+(o2 & 0xFF)){ return -1; } return 1;
		 * } };
		 */

		/*
		 * for (int i = 1; i < n - 1; i++) { for (int ind = 0; ind < 3; ind++) {
		 * window[ind] = rgb[i - 1 + ind]; } for (int indw = 3; indw < 6;
		 * indw++) { window[indw] = rgb[n + i - 4 + indw]; }
		 * Arrays.sort(window,comp); r=g=b=0; for (int j = 1; j < window.length
		 * - 1; j++) { r += (window[j]>>>16) & 0xFF; g += (window[j]>>>8) &
		 * 0xFF; b += (window[j]) & 0xFF; }
		 * 
		 * newrgb[i] = r<<14 | g << 6 | b>>>2;
		 * 
		 * for (int ind = 0; ind < 3; ind++) { window[ind] = rgb[rgb.length - i
		 * - ind]; } for (int indw = 3; indw < 6; indw++) { window[indw] =
		 * rgb[rgb.length - i - indw + 3 - n]; } Arrays.sort(window,comp);
		 * r=g=b=0; for (int j = 1; j < window.length - 1; j++) { r +=
		 * (window[j]>>>16) & 0xFF; g += (window[j]>>>8) & 0xFF; b +=
		 * (window[j]) & 0xFF; } newrgb[rgb.length - 1 - i] = r<<14 | g << 6 |
		 * b>>>2; }
		 */
		/*
		 * for (int i = 1; i < m-1; i++) { for (int ind = 0; ind < 3; ind++) {
		 * window[ind] = rgb[n (i - 1 + ind)]; } for (int ind = 3; ind < 6;
		 * ind++) { window[ind] = rgb[n (i - 4 + ind) + 1]; }
		 * Arrays.sort(window,comp); r=g=b=0; for (int j = 1; j < window.length
		 * - 1; j++) { r += (window[j]>>>16) & 0xFF; g += (window[j]>>>8) &
		 * 0xFF; b += (window[j]) & 0xFF; } newrgb[i n] =r<<14 | g << 6 | b>>>2;
		 * 
		 * for (int ind = 0; ind < 3; ind++) { window[ind] = rgb[(i + ind) n -
		 * 1]; } for (int ind = 3; ind < 6; ind++) { window[ind] = rgb[(i + ind
		 * - 3) n - 2]; } Arrays.sort(window,comp); r=g=b=0; for (int j = 1; j <
		 * window.length - 1; j++) { r += (window[j]>>>16) & 0xFF; g +=
		 * (window[j]>>>8) & 0xFF; b += (window[j]) & 0xFF; } newrgb[(i + 1) n -
		 * 1] =r<<14 | g << 6 | b>>>2; } newrgb[0] = rgb[0]; newrgb[n-1] =
		 * rgb[n-1]; newrgb[rgb.length-1] = rgb[rgb.length-1];
		 * newrgb[rgb.length-n] = rgb[rgb.length-n];
		 */

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
			} else if (s.compareTo("2") == 0) {
				w = w2;
				ftmp = filtr2;
			} else if (s.compareTo("3") == 0) {
				w = w3;
				ftmp = filtr3;
			}
			for (int i = 0; i < filtr.length; i++) {
				filtr[i] = ftmp[i] * w;
			}
			for (MyInternalFrame frame : WindowManager.getInstance()
					.getFrames()) {
				if (frame.isSelected()) {
					long TIME;
					BufferedImage image = frame.getPicture().getImage();
					TIME = splataj2(image);

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
