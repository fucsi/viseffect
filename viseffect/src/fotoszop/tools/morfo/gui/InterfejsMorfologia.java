package fotoszop.tools.morfo.gui;

import fotoszop.tools.morfo.*;
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
 * @author kalkan
 */
public class InterfejsMorfologia extends ToolInterface {

    JMenuItem menu;
    ArrayDialog dialog;
    int[] maska = new int[9];
    MyInternalFrame selectedFrame = null;

    public InterfejsMorfologia() {
        menu = new JMenuItem("Morfologia");
        menu.setIcon(this.getResourceMap().getIcon("icons.morpho"));
        menu.addActionListener(this);
        dialog = new ArrayDialog("morfologia", "Wpisz element strukturalny", this, 1);
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
            JOptionPane.showMessageDialog(menu, "Wybierz obrazek");
            return;
        }

        if (selectedFrame.getPicture().getImage().getType() != BufferedImage.TYPE_BYTE_BINARY) {
            JOptionPane.showMessageDialog(menu, "Obrazek nie jest czarno-bialy");
            return;
        }


        Object[] options = {"[A (+) B] / A",
            "A / [A (-) B]",
            "[A (+) B] / [A (-) B]"};
        int operation = JOptionPane.showOptionDialog(null,
                "Wybierz operacje",
                "Morfologia",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (operation == JOptionPane.CLOSED_OPTION) {
            return;
        }
        try {
            dialog.setVisible(true);
        } catch (NumberFormatException ex) {
            return;
        }
        Morfologia morf = new Morfologia(maska);
        long TIME;
        switch (operation) {
            case JOptionPane.YES_OPTION:
                TIME = System.currentTimeMillis();
                morf.wariant1(selectedFrame.getPicture().getImage());
                System.out.println(System.currentTimeMillis()-TIME);
                selectedFrame.repaint();
                break;
            case JOptionPane.NO_OPTION:
                TIME = System.currentTimeMillis();
                morf.wariant2(selectedFrame.getPicture().getImage());
                System.out.println(System.currentTimeMillis()-TIME);
                selectedFrame.repaint();
                break;
            case JOptionPane.CANCEL_OPTION:
                TIME = System.currentTimeMillis();
                morf.wariant3(selectedFrame.getPicture().getImage());
                System.out.println(System.currentTimeMillis()-TIME);
                selectedFrame.repaint();
                break;
            default:
                break;
        }

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
        if (dialog.isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
                JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            //Tu jest miejsce w ktorym sie cokolwiek zmienia: OK i Cancel
            if (btnString1.equals(value)) {
                JTextField[] fields = dialog.txtFields;

                for (int i = 0; i < fields.length; i++) {
                    String text = fields[i].getText();
                    maska[i] = Integer.parseInt(text);
                }
                dialog.setVisible(false);

            }
        }
    }
}


