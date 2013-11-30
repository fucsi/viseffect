/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.morfo.gui;

import fotoszop.tools.morfo.*;
import fotoszop.gui.ArrayDialog;
import fotoszop.gui.MyInternalFrame;
import fotoszop.gui.WindowManager;
import fotoszop.tools.ToolInterface;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * 
 * @author kalkan
 */
public class InterfejsHMT extends ToolInterface {

	JMenuItem menu;
	ArrayDialog dialog1, dialog2;
	ArrayDialog dialog;
	int[] maska1 = new int[9];
	MyInternalFrame selectedFrame = null;

	public InterfejsHMT() {
		menu = new JMenuItem("HitMiss");
		menu.setIcon(this.getResourceMap().getIcon("icons.hitmis"));
		menu.addActionListener(this);
		dialog1 = new ArrayDialog("HitMiss", "Wpisz element strukturalny B1",
				this, 1);
		dialog2 = new ArrayDialog("HitMiss", "Wpisz element strukturalny B2",
				this, 1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (MyInternalFrame frame : WindowManager.getInstance().getFrames()) {
			if (frame.isSelected()) {
				selectedFrame = frame;
				break;
			}
		}
		if (selectedFrame == null) {
			JOptionPane.showMessageDialog(menu, "wybierz obrazek");
			return;
		}

		if (selectedFrame.getPicture().getImage().getType() != BufferedImage.TYPE_BYTE_BINARY) {
			JOptionPane
					.showMessageDialog(menu, "Obrazek nie jest czarno-bialy");
			return;
		}
		dialog = dialog1;
		try {
			dialog1.setVisible(true);
		} catch (NumberFormatException ex) {
			return;
		}
		int[] maska2 = Arrays.copyOf(maska1, 9);
		dialog = dialog2;
		try {
			dialog2.setVisible(true);
		} catch (NumberFormatException ex) {
			return;
		}
		Object[] options = { "Erozja przez Dylatacje",
				"Erozja przez intersekcje" };
		int operation = JOptionPane.showOptionDialog(null, "Wybierz algorytm",
				"Morfologia", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (operation == JOptionPane.CLOSED_OPTION) {
			return;
		}
		Morfologia morf = null;
		if (operation == JOptionPane.YES_OPTION) {
			morf = new Morfologia(maska2, maska1);
		} else if (operation == JOptionPane.NO_OPTION) {
			morf = new MorfologiaNowa(maska2, maska1);
		}
		long TIME = System.currentTimeMillis();
		morf.hitmiss(selectedFrame.getPicture().getImage());
		System.out.println(System.currentTimeMillis() - TIME);
		selectedFrame.repaint();

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

				for (int i = 0; i < fields.length; i++) {
					String text = fields[i].getText();
					maska1[i] = Integer.parseInt(text);
				}
				dialog.setVisible(false);

			}
		}
	}
}
