/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.errorcomputation;

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
 * @author kalkan
 */
public class MaxDiff extends ToolInterface {

    private JMenuItem menuItem;
    private MyDialog dialog;
    private double error1,  error2;

    public MaxDiff() {
        menuItem = new JMenuItem("MaxDiff");
        menuItem.setIcon(this.getResourceMap().getIcon("icons.maxdiff"));
        menuItem.addActionListener(this);
        dialog = new MyDialog("MaxDiff Computation", "Chose the original, with and without noise", this);
        dialog.btnString1 = "Calculate";
        dialog.btnString2 = "Close";
        ((JOptionPane) dialog.getContentPane()).setOptions(new String[]{dialog.btnString1, dialog.btnString2});

        dialog.addTextField("Please select the windows", true);
        dialog.addTextField("With noise", true);
        dialog.addTextField("Without noise", true);
        dialog.setShowWindows(true);
        dialog.setHowManyWindowSelectors(3);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List runThisTool(List<BufferedImage> images, List arguments){
        maxDiff(images);
        List result = new ArrayList();
        result.add(error1/error2);
        result.add(error1);
        result.add(error2);
        return result;
    }

    @Override
    public JMenuItem getJMenuItem() {
        return menuItem;
    }

    private void maxDiff(List<BufferedImage> images) {
        int max1 = 0, max2 = 0, max3 = 0;
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE, min3 = Integer.MAX_VALUE;
        int orgRGB = 0;
        int noRGB = 0;
        int clRGB = 0;

        int b1 = 0;

        BufferedImage org = images.get(0);
        BufferedImage noise = images.get(1);
        BufferedImage cleaned = images.get(2);
        int N = images.get(0).getHeight(), M = images.get(0).getWidth();
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                orgRGB = org.getRGB(x, y);
                b1 = (orgRGB >> 16 & 0xFF) + (orgRGB >> 8 & 0xFF) + (orgRGB & 0xFF);
                if (b1 > max1) {
                    max1 = b1;
                } else if (b1 < min1) {
                    min1 = b1;
                }
                noRGB = noise.getRGB(x, y);
                b1 = (noRGB >> 16 & 0xFF) + (noRGB >> 8 & 0xFF) + (noRGB & 0xFF);

                if (b1 > max2) {
                    max2 = b1;
                } else if (b1 < min2) {
                    min2 = b1;
                }
                clRGB = cleaned.getRGB(x, y);
                b1 = (clRGB >> 16 & 0xFF) + (clRGB >> 8 & 0xFF) + (clRGB & 0xFF);
                if (b1 > max2) {
                    max3 = b1;
                } else if (b1 < min3) {
                    min3 = b1;
                }
            }
        }
        max2 = Math.max(Math.abs(max1 - min2), Math.abs(min1 - max2));
        max3 = Math.max(Math.abs(max1 - min3), Math.abs(min1 - max3));
        error1 = (double) max2;
        error2 = (double) max3;
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
                List<MyInternalFrame> frames = dialog.getSelectedWindows();
                List<BufferedImage> images = new ArrayList<BufferedImage>();
                boolean flag = true;
                for (int i = 0; i < frames.size(); i++) {
                    MyInternalFrame myInternalFrame = frames.get(i);
                    images.add(myInternalFrame.getPicture().getImage());
                    for (int j = i + 1; j < frames.size(); j++) {
                        MyInternalFrame myInternalFrame1 = frames.get(j);
                        if (myInternalFrame.equals(myInternalFrame1)) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        break;
                    }
                }
                if (!flag) {
                    dialog.textFields.get(0).setText("Please pick different images");
                    dialog.textFields.get(1).setText("");
                    dialog.textFields.get(2).setText("");
                    return;
                }

                if (images.get(0).getWidth() == images.get(1).getWidth() &&
                        images.get(0).getHeight() == images.get(1).getHeight()) {
                    if (images.get(0).getWidth() == images.get(2).getWidth() &&
                            images.get(0).getHeight() == images.get(2).getHeight()) {
                        flag = false;//size identical for all three images
                    }
                }
                if (flag) {
                    dialog.textFields.get(0).setText("Images need to have the same size");
                    dialog.textFields.get(1).setText("");
                    dialog.textFields.get(2).setText("");
                    return;
                }

                maxDiff(images);
                dialog.textFields.get(0).setText("Stosunek zaszumiony/odszumiony: " + error1 / error2);
                dialog.textFields.get(1).setText("Zaszumiony: " + error1);
                dialog.textFields.get(2).setText("Odzumiony: " + error2);
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

