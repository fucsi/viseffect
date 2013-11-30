/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.segmentacja.interfaces;

import fotoszop.gui.MyDialog;
import fotoszop.tools.segmentacja.*;
import fotoszop.gui.MyInternalFrame;
import fotoszop.gui.WindowManager;
import fotoszop.tools.ToolInterface;
import fotoszop.tools.segmentacja.Segmentacja;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author kalkan
 */
public class InterfejsSegmentacja extends ToolInterface {

    JMenuItem menu;
    MyDialog dialog;

    public InterfejsSegmentacja() {
        menu = new JMenuItem("Segmentacja");
        menu.setIcon(this.getResourceMap().getIcon("icons.segmentation"));
        menu.addActionListener(this);
        dialog = new MyDialog("", "", this);
        dialog.addTextField("prog warunku", true);
        dialog.addTextField("ramka warunku", true);
        dialog.addTextField("prog scalania", true);
        dialog.addTextField("typ warunku", true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.setVisible(true);

    }

    @Override
    public JMenuItem getJMenuItem() {
        return menu;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        JOptionPane optionPane = dialog.optionPane;
        String btnString1 = dialog.btnString1, btnString2 = dialog.btnString2;
        if (dialog.isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
                JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            //Tu jest miejsce w ktorym sie cokolwiek zmienia: OK i Cancel
            if (btnString1.equals(value)) {
                ArrayList<JTextField> textFields = (ArrayList<JTextField>) dialog.textFields;
                double prog = Double.parseDouble(textFields.get(0).getText());
                double progScal = Double.parseDouble(textFields.get(2).getText());
                int ramka = Integer.parseInt(textFields.get(1).getText());

                int typ = Integer.parseInt(textFields.get(3).getText());
                WarunekStopu stopwar = null;
                switch (typ) {
                    case 1:
                        stopwar = new StopAvg(prog, ramka);
                        break;
                    case 2:
                        stopwar = new StopWariancja(prog, ramka);
                        break;
                    case 3:
                        stopwar = new StopChange(prog, ramka);
                        break;
                    default:
                        stopwar = new StopAvg(prog, ramka);
                        break;
                }
                Segmentacja seg = new Segmentacja(stopwar, new DomyslnyScalacz(progScal));
                MyInternalFrame savedframe = null;
                for (MyInternalFrame frame : WindowManager.getInstance().getFrames()) {
                    if (frame.isSelected()) {
                        savedframe = frame;
                        break;
                    }
                }
                if (savedframe == null) {
                    return;
                }
                seg.segmentuj(savedframe.getPicture().getImage());
                dialog.setVisible(false);
                dialog.reset();
                savedframe.repaint();
            } else if (btnString2.equals(value)) {
                dialog.setVisible(false);
                dialog.reset();
            }

        }





    }
}
