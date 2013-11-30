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
public class MeanSquare extends ToolInterface {

    private JMenuItem menuItem;
    private MyDialog dialog;
    private double error1,  error2;

    public MeanSquare() {
        menuItem = new JMenuItem("MeanSquare");
        menuItem.addActionListener(this);
        dialog = new MyDialog("Mean Square Computation", "Chose the original, with and without noise", this);
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
    public JMenuItem getJMenuItem() {
        return menuItem;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List runThisTool(List<BufferedImage> images, List arguments){
        meanSquareError(images);
        List result = new ArrayList();
        result.add(error1/error2);
        result.add(error1);
        result.add(error2);
        return result;
    }

    private void meanSquareError(List<BufferedImage> images) {
        long[] e1 = new long[]{0, 0, 0}, e2 = new long[]{0, 0, 0};
        int[] tmp1 = new int[]{0, 0, 0}, tmp2 = new int[]{0, 0, 0}, tmp0 = new int[]{0, 0, 0};
        BufferedImage org = images.get(0);
        BufferedImage noise = images.get(1);
        BufferedImage cleaned = images.get(2);
        int N = images.get(0).getHeight(), M = images.get(0).getWidth();
        int orgRGB, nrgb, crgb;
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                orgRGB = org.getRGB(x, y);
                tmp0[0] = orgRGB & 0xFF;
                tmp0[1] = orgRGB >> 8 & 0xFF;
                tmp0[2] = orgRGB >> 16 & 0xFF;
                nrgb = noise.getRGB(x, y);
                tmp1[0] = tmp0[0] - (nrgb & 0xFF);
                tmp1[1] = tmp0[1] - (nrgb >> 8 & 0xFF);
                tmp1[2] = tmp0[2] - (nrgb >> 16 & 0xFF);
                crgb = cleaned.getRGB(x, y);
                tmp2[0] = tmp0[0] - (crgb & 0xFF);
                tmp2[1] = tmp0[1] - (crgb >> 8 & 0xFF);
                tmp2[2] = tmp0[2] - (crgb >> 16 & 0xFF);
                for (int i = 0; i < 3; ++i) {
                    e1[i] += tmp1[i] * tmp1[i];
                    e2[i] += tmp2[i] * tmp2[i];
                }
            }
        }
        int size = M * N;
        error1 = (double) (e1[0] + e1[1] + e1[2] ) / size;
        error2 = (double) (e2[0] + e2[1] + e2[2] ) / size;
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
                meanSquareError(images);
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
