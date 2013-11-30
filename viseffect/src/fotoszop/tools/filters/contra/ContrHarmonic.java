/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.filters.contra;

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
public class ContrHarmonic extends ToolInterface {

    private JMenuItem menuItem;
    private MyDialog dialog;

    public ContrHarmonic() {
        menuItem = new JMenuItem("Filtr Kontrharmoniczny");
        menuItem.addActionListener(this);
        dialog = new MyDialog("Filtr Kontrharmoniczny", "Ustaw M, N oraz Q", this);
        dialog.addTextField("2", true);
        dialog.addTextField("2", true);
        dialog.addTextField("1", true);
        dialog.setShowWindows(true);
    }

    @Override
    public JMenuItem getJMenuItem() {
        return menuItem;
    }

    @Override
    public List runThisTool(List<BufferedImage> images, List arguments) {
        contrHarmonic(images.get(0), (Integer) arguments.get(0), (Integer) arguments.get(1), (Double) arguments.get(2));
        return images;
    }

    private void contrHarmonic(BufferedImage image, int m, int n, double q) {
        long TIME = System.currentTimeMillis();
        int w = image.getWidth();
        int h = image.getHeight();
        int[] oldPixels = image.getRGB(0, 0, w, h, null, 0, w);
        int moreSize = oldPixels.length + (w * m << 1) + (h * n << 1) + (m * n << 2);
        int[] pixels = new int[moreSize];
        {
            int tmp;
            int offset = m * w + (n * m << 1);
            int t1, t2, t3;
            //przepisanie starych pikseli do nowej tablicy w to samo miejsce
            for (int i = 0; i < h; ++i) {
                t1 = i * w;
                for (int j = 0; j < w; ++j) {
                    //tmp = i * w + j;
                    //pixels[offset + n * ((i << 1) + 1) + tmp] = oldPixels[tmp];
                    pixels[offset + n * ((i << 1) + 1) + t1] = oldPixels[t1];
                    ++t1;
                }
            }
            //uzupelnienie brzegow
            //  gorny margines

            for (int i = 0; i < m; ++i) {
                tmp = n * ((i << 1) + 1);//dl lewego marginesu
                t1 = i * w;
                t2 = (m - i) * w;
                t3 = tmp + t1;
                for (int j = 0; j < w; ++j) {
                    pixels[t3] = oldPixels[t2];
                    ++t2;
                    ++t3;
                }
            }
            //dolny margines
            offset = (w + (n << 1)) * (h + m);
            for (int i = 0; i < m; ++i) {
                tmp = n * ((i << 1) + 1);//dl lewego marginesu
                t1 = offset + tmp + i * w;
                t2 = oldPixels.length - w * (i + 1);
                for (int j = 0; j < w; ++j) {
                    pixels[t1] = oldPixels[t2];
                    ++t1;
                    ++t2;
                }
            }

            //lewy i prawy bok
            offset = (w + (n << 1)) * m;
            for (int i = 0; i < h + (m << 1); ++i) {
                tmp = i * (w + (n << 1));
                t1 = (i + 1) * (w + (n << 1)) - 1;
                for (int j = 0; j < n; ++j) {
                    pixels[tmp + j] = pixels[tmp + j + (n - j << 1)];
                    pixels[t1 - j] = pixels[t1 - (n << 1) + j];

                }
            }
        }

        int ind = -1;
        double[] up = new double[]{0.0, 0.0, 0.0}, down = new double[]{0.0, 0.0, 0.0};
        int tmp;
        double tmpb, tmpg, tmpr;
        int red, green, blue;
        for (int i = m; i < h + m; ++i) {
            for (int j = n; j < w + n; ++j) {
                for (int k = 0; k < down.length; k++) {
                    down[k] = 0;
                    up[k] = 0;
                }
                tmp = 0;
                up[0] = up[1] = up[2] = down[0] = down[1] = down[2] = 0;
                for (int dj = -n; dj <= n; ++dj) {
                    for (int di = -m; di <= m; ++di) {
                        tmp = pixels[(i + di) * (w + (n << 1)) + j + dj];

                        red = tmp >> 16 & 0xFF;
                        green = tmp >> 8 & 0xFF;
                        blue = tmp & 0xFF;


                        tmpr = Math.pow(red, q);
                        tmpg = Math.pow(green, q);
                        tmpb = Math.pow(blue, q);
                        if (q >= 0) {
                            up[0] += tmpb * blue;
                            up[1] += tmpr * red;
                            up[2] += tmpg * green;
                        } else {
                            up[0] += Math.pow(blue, q + 1);
                            up[1] += Math.pow(red, q + 1);
                            up[2] += Math.pow(green, q + 1);
                        }
                        down[0] += tmpb;
                        down[1] += tmpr;
                        down[2] += tmpg;

                    }
                }
                //nadpisanie oldpixels


                try {
                    red = (int) Math.round(up[1] / down[1]);

                } catch (Exception e) {
                    red = 0xFF;
                }
                try {
                    green = (int) Math.round(up[2] / down[2]);
                } catch (Exception e) {
                    green = 0xFF;
                }

                try {
                    blue = (int) Math.round(up[0] / down[0]);

                } catch (Exception e) {
                    blue = 0xff;
                }
                if (blue > 0xFF) {
                    blue = 0xFF;
                }

                if (red > 0xFF) {
                    red = 0xFF;
                }
                if (green > 0xFF) {
                    green = 0xFF;
                }
                ++ind;
                oldPixels[ind] = ((oldPixels[ind] & 0xFF000000) | red << 16 | green << 8 | blue);

            }
        }

        TIME = System.currentTimeMillis() - TIME;
        System.out.println("contra normal " + TIME);
        image.setRGB(0, 0, w, h, oldPixels, 0, w);
    }

    @Override
    public void propertyChange(
            PropertyChangeEvent e) {
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

                int m, n;
                double q;
                try {
                    m = Integer.parseInt(dialog.textFields.get(0).getText());
                    n = Integer.parseInt(dialog.textFields.get(1).getText());
                    q = Double.parseDouble(dialog.textFields.get(2).getText());
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(dialog, "m, n oraz d muza byc calkowite");
                    return;
                }
                if (m < 0 || n < 0) {
                    JOptionPane.showMessageDialog(dialog, "m, n musza byc co najmniej zerem");
                    return;
                }


                for (MyInternalFrame frame : frames) {
                    BufferedImage image = frame.getPicture().getImage();
                    contrHarmonic(image, m, n, q);
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
