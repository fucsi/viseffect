package fotoszop.tools.basic;

import fotoszop.tools.*;
import fotoszop.gui.MyDialog;
import fotoszop.gui.MyInternalFrame;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * 
 * @author student
 */
public class Scale extends ToolInterface {

	MyDialog dialog;
	private JMenuItem menuItem;
	private HashMap<String, Integer> zoomLevels = new HashMap<String, Integer>();

	public Scale() {
		dialog = new MyDialog("Skalowanie",
				"Dopasuj poziom skalowania obrazka", this);
		dialog.addIntegerSlider(10, 200, 100, null);
		dialog.setShowWindows(true);
		menuItem = new JMenuItem("Skalowanie");
		menuItem.setIcon(this.getResourceMap().getIcon("icons.scale"));
		menuItem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			dialog.setVisible(true);
		}
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
					BufferedImage dst = this.scale2(dialog.sliders.get(0)
							.getValue(), image, frame.getTitle());
					if (dst != null) {
						frame.getPicture().setImage(dst);
					}
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

	private BufferedImage scale2(int percent, BufferedImage image, String title) {
		int a = image.getWidth();
		int b = image.getHeight();

		double scale = Math.sqrt(percent) / 10;

		int a2 = (int) Math.round(scale * a);
		int b2 = (int) Math.round(scale * b);

		BufferedImage dst = new BufferedImage(a2, b2, image.getType());

		int[] original = image.getRGB(0, 0, a, b, null, 0, a);

		int[] copy = new int[a2 * b2];

		int diffA = Math.abs(a2 - a);
		int diffB = Math.abs(b2 - b);

		int skipB = b / diffB + 1;
		int skipA = a / diffA + 1;

		if (a2 < a) {
			int k = 0;
			int l = 0;
			int i = 0;
			int j = 0;
			try {
				for (i = 0; i < b2; i++) {
					k = 0;
					if ((l + 1) % (skipB) == 0) {
						l++;
					}
					for (j = 0; j < a2; j++) {
						if ((k + 1) % (skipA) == 0) {
							k++;
						}
						copy[i * a2 + j] = original[l * a + k];
						k++;
					}
					l++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(b2 + " " + a2 + "\n" + b + " " + a);
				System.out.println(l + " " + k + "\n" + i + " " + j);
				throw e;
			}
			dst.setRGB(0, 0, a2, b2, copy, 0, a2);
		} else if (a2 > a) {
			int k = 0;
			int l = 0;
			int i = 0;
			int j = 0;
			try {
				for (i = 0; i < b2; i++) {
					k = 0;
					if ((l + 1) % (skipB - 1) == 0) {
						for (j = 0; j < a2; j++) {
							if ((k + 1) % (skipA - 1) == 0) {
								copy[i * a2 + j] = original[l * a + k];
								j++;
							}
							copy[i * a2 + j] = original[l * a + k];
							k++;
						}
						k = 0;
						i++;
					}
					for (j = 0; j < a2; j++) {
						if ((k + 1) % (skipA - 1) == 0) {
							copy[i * a2 + j] = original[l * a + k];
							j++;
						}
						copy[i * a2 + j] = original[l * a + k];
						k++;
					}
					l++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println(b2 + " " + a2 + "\n" + b + " " + a);
				System.out.println(l + " " + k + "\n" + i + " " + j);
				throw e;
			}
			dst.setRGB(0, 0, a2, b2, copy, 0, a2);
		} else {
			dst.setRGB(0, 0, a, b, original, 0, a);
		}
		return dst;
	}

}
