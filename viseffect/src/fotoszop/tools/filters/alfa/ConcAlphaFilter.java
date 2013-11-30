/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.filters.alfa;

import fotoszop.gui.MyDialog;
import fotoszop.gui.MyInternalFrame;
import fotoszop.tools.ToolInterface;
import fotoszop.tools.filters.CreateFrame;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ConcAlphaFilter extends ToolInterface {

	private JMenuItem menuItem;
	private MyDialog dialog;

	public ConcAlphaFilter() {
		menuItem = new JMenuItem("Filtr ConcAlpha");
		menuItem.addActionListener(this);
		dialog = new MyDialog("FIltr ConcAlpha", "Ustaw M, N oraz D", this);
		dialog.addTextField("2", true);
		dialog.addTextField("2", true);
		dialog.addTextField("2", true);
		dialog.setShowWindows(true);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menuItem;
	}

	@Override
	public List runThisTool(List<BufferedImage> images, List arguments) {
		alphaTrimmedFilter(images.get(0), (Integer) arguments.get(0),
				(Integer) arguments.get(1), (Integer) arguments.get(2));
		return images;
	}

	private void alphaTrimmedFilter(BufferedImage image, int m, int n, int d) {
		long TIME = System.currentTimeMillis();
		int w = image.getWidth();
		int h = image.getHeight();
		int[] oldPixels = image.getRGB(0, 0, w, h, null, 0, w);
		int M = 1 + (m << 1);
		int N = 1 + (n << 1);
		int doubleD = d << 1;

		// <editor-fold desc="kod do testowania algorytmu">
		// TESTOWANIE
		/*
		 * oldPixels = new int[72]; for (int i = 0; i < oldPixels.length; i++) {
		 * oldPixels[i] = i; } w = 8; h = 9;
		 * 
		 * for (int iterator1 = 0; iterator1 < h; ++iterator1) { for (int
		 * iterator2 = 0; iterator2 < w; ++iterator2) { String x = "" +
		 * oldPixels[iterator1 w + iterator2]; if (x.length() == 1) { x = "0" +
		 * x; } System.out.print(x + " "); } System.out.println(); }
		 */
		// TESTOWANIE
		// </editor-fold>
		int moreSize = oldPixels.length + (w * m << 1) + (h * n << 1)
				+ (m * n << 2);
		int[] bintmp = new int[3];
		int[] pixels = new int[moreSize];
		{
			CreateFrame cfUp = new CreateFrame(oldPixels, pixels, m, n, h, w,
					CreateFrame.EXPAND_UP);
			CreateFrame cfDown = new CreateFrame(CreateFrame.EXPAND_LEFT);
			int t1;
			int offset = m * w + (n * m << 1);
			// <editor-fold desc=
			// "przepisanie starych pikseli do nowej tablicy w to samo miejsce">
			for (int i = 0; i < h; ++i) {
				t1 = i * w;
				for (int j = 0; j < w; ++j) {
					pixels[offset + n * ((i << 1) + 1) + t1] = oldPixels[t1];
					++t1;
				}
			}
			// </editor-fold>
			// <editor-fold desc="uzupelnienie brzegow">
			// gorny margines
			Thread t = new Thread(cfUp);
			t.start();
			// dolny margines
			cfDown.doBottom();
			try {
				t.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(ConcAlphaFilter.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			// lewy bok
			cfUp.setMode(CreateFrame.EXPAND_RIGHT);
			t = new Thread(cfUp);
			t.start();
			// prawy bok
			cfDown.doLeft();
			try {
				t.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(ConcAlphaFilter.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			// </editor-fold>
			// <editor-fold desc="kod do testowania sieci">
			/*
			 * System.out.println(); for (int iterator1 = 0; iterator1 < h + 2
			 * n; ++iterator1) { for (int iterator2 = 0; iterator2 < w + 2 m;
			 * ++iterator2) { String x = "" + pixels[iterator1 (w + 2 m) +
			 * iterator2]; if (x.length() == 1) { x = "0" + x; }
			 * System.out.print(x + " "); } System.out.println(); }
			 */
			// </editor-fold>
		}
		int neighbourhoodSize = M * N;
		int[] window = new int[neighbourhoodSize];

		int index = 0;
		int ind = -1;
		int bin1 = 0, bin2 = 0, bin3 = 0;
		for (int i = m; i < h + m; ++i) {
			for (int j = n; j < w + n; ++j) {
				index = 0;
				// wypelnienie okna srednich
				for (int dj = -n; dj <= n; ++dj) {
					for (int di = -m; di <= m; ++di) {
						window[index++] = pixels[(i + di) * (w + (n << 1)) + j
								+ dj];
					}
				}
				// polsortowanie i odrzucenie min d/2 i max d/2
				int min = 0;
				int max = 0;
				for (int k = 0; k < d; ++k) {
					// Find position of minimum element
					min = k;
					max = k;
					bin2 = (window[min] >> 16 & 0xFF)
							+ (window[min] >> 8 & 0xFF) + (window[min] & 0xFF);
					bin3 = (window[max] >> 16 & 0xFF)
							+ (window[max] >> 8 & 0xFF) + (window[max] & 0xFF);
					for (int l = k + 1; l < window.length - k; ++l) {
						bin1 = (window[l] >> 16 & 0xFF)
								+ (window[l] >> 8 & 0xFF) + (window[l] & 0xFF);

						if (bin1 < bin2) {
							min = l;
							bin2 = (window[min] >> 16 & 0xFF)
									+ (window[min] >> 8 & 0xFF)
									+ (window[min] & 0xFF);
						} else if (bin1 > bin3) {
							max = l;
							bin3 = (window[max] >> 16 & 0xFF)
									+ (window[max] >> 8 & 0xFF)
									+ (window[max] & 0xFF);
						}
					}
					// Put found minimum element in its place
					index = window[k];// tmp variable
					window[k] = window[min];
					window[min] = index;
					index = window[window.length - 1 - k];
					window[window.length - 1 - k] = window[max];
					window[max] = index;
				}
				// liczenie sredniej
				bintmp[0] = 0;
				bintmp[1] = 0;
				bintmp[2] = 0;
				for (int k = d; k < window.length - d; ++k) {

					bintmp[0] += window[k] >> 16 & 0xFF;
					bintmp[1] += window[k] >> 8 & 0xFF;
					bintmp[2] += window[k] & 0xFF;
				}
				bintmp[0] /= neighbourhoodSize - doubleD;
				bintmp[1] /= neighbourhoodSize - doubleD;
				bintmp[2] /= neighbourhoodSize - doubleD;
				if (bintmp[0] > 0xFF) {
					bintmp[0] = 0xFF;
				}
				if (bintmp[1] > 0xFF) {
					bintmp[1] = 0xFF;
				}
				if (bintmp[2] > 0xFF) {
					bintmp[2] = 0xFF;
				}

				// nadpisanie oldpixels
				++ind;
				oldPixels[ind] = ((oldPixels[ind] >> 24 & 0xFF)
						| bintmp[0] << 16 | bintmp[1] << 8 | bintmp[2]);
			}
		}
		/*
		 * System.out.println(); for (int iterator1 = 0; iterator1 < h;
		 * ++iterator1) { for (int iterator2 = 0; iterator2 < w; ++iterator2) {
		 * String x = "" + oldPixels[iterator1 w + iterator2]; if (x.length() ==
		 * 1) { x = "0" + x; } System.out.print(x + " "); }
		 * System.out.println(); } System.out.println();
		 */

		TIME = System.currentTimeMillis() - TIME;
		System.out.println("alfa normal " + TIME);
		image.setRGB(0, 0, w, h, oldPixels, 0, w);
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

				int m, n, d;
				try {
					m = Integer.parseInt(dialog.textFields.get(0).getText());
					n = Integer.parseInt(dialog.textFields.get(1).getText());
					d = Integer.parseInt(dialog.textFields.get(2).getText());
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(dialog,
							"m, n and d musza byc calkowie");
					return;
				}
				if (m < 0 || n < 0) {
					JOptionPane.showMessageDialog(dialog,
							"m, n musza byc wieksze od zera");
					return;
				}

				if (d < 0) {
					JOptionPane.showMessageDialog(dialog,
							"d musi byc co najmniej zerem");
					return;
				}
				if ((d << 1) > ((m << 1) + 1) * (((n << 1) + 1)) - 1) {
					JOptionPane.showMessageDialog(dialog,
							"d musi byc mniejsze niz rozmiar okna");
					return;
				}

				for (MyInternalFrame frame : frames) {
					BufferedImage image = frame.getPicture().getImage();
					alphaTrimmedFilter(image, m, n, d);
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
