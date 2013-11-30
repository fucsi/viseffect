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
public class PeakSignal2Noise extends ToolInterface {

    private JMenuItem menuItem;
    private MyDialog dialog;
    private final double perfectNoiseless = 32.04;
private double error1,error2;
    public PeakSignal2Noise() {
        menuItem = new JMenuItem("PeakSignal2Noise");
        menuItem.addActionListener(this);
        dialog = new MyDialog("PeakSignal2Noise Computation", "Chose the original, with and without noise", this);
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
        peakSignal2Noise(images);
        List result = new ArrayList();
        result.add((perfectNoiseless - error1)/(perfectNoiseless-error2));
        result.add(error1);
        result.add(error2);
        return result;
    }

    private void peakSignal2Noise(List<BufferedImage> images) {
        long tmp,max=0;
        long down1 = 0, down2 = 0;
        long[] tmp1 = new long[]{0, 0,  0}, tmp2 = new long[]{0, 0, 0};
        long[] tmp0 = new long[]{0, 0,  0};
        int orgRGB, nrgb, crgb;
        BufferedImage org = images.get(0);
        BufferedImage noise = images.get(1);
        BufferedImage cleaned = images.get(2);
        int N = images.get(0).getHeight(), M = images.get(0).getWidth();
        for (int x = 0; x < M; x++) {
            for (int y = 0; y < N; y++) {
                orgRGB = org.getRGB(x, y);
                tmp0[0] = orgRGB & 0xFF;
                tmp0[1] = orgRGB >> 8 & 0xFF;
                tmp0[2] = orgRGB >> 16 & 0xFF;
                
                tmp = tmp0[0]+tmp0[1]+tmp0[2];
                if (tmp > max) {
                    max = tmp;
                }
                nrgb = noise.getRGB(x, y);
                tmp1[0] = tmp0[0] - (nrgb & 0xFF);
                tmp1[1] = tmp0[1] - (nrgb >> 8 & 0xFF);
                tmp1[2] = tmp0[2] - (nrgb >> 16 & 0xFF);
                down1 += tmp1[0]*tmp1[0] + tmp1[1]*tmp1[1]+ tmp1[2]*tmp1[2];
                crgb = cleaned.getRGB(x, y);
                tmp2[0] = tmp0[0] - (crgb & 0xFF);
                tmp2[1] = tmp0[1] - (crgb >> 8 & 0xFF);
                tmp2[2] = tmp0[2] - (crgb >> 16 & 0xFF);
                
                down2 += tmp2[0]*tmp2[0] + tmp2[1]*tmp2[1]+ tmp2[2]*tmp2[2];
            }
        }
        max = Math.round(max / 3.0);
        max = max * max * M * N;
        error1 = 10.0 * Math.log10((double) max/down1);
        error2 = 10.0 * Math.log10((double) max/down2);
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
               
                peakSignal2Noise(images);
                dialog.textFields.get(0).setText(
                        "Stosunek różnicy od czystego sygnału " +
                        "(" + perfectNoiseless + "-zaszumiony)/(" + perfectNoiseless + "odszumiony): "+
                        ((perfectNoiseless - error1) / (perfectNoiseless - error2)));
                dialog.textFields.get(1).setText("Zaszumiony: " + error1);
                dialog.textFields.get(2).setText("Odszumiony: " + error2);
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

