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
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author kalkan
 */
public class SplotKirsz extends ToolInterface {

    int r, g, b;
    JMenuItem menu;

    public SplotKirsz() {
        menu = new JMenuItem("Splot Kirsza na obrazie");
        menu.setIcon(this.getResourceMap().getIcon("icons.splotkirsh"));
        menu.addActionListener(this);
    }

    private long splataj(BufferedImage image) {
        long TIME = System.currentTimeMillis();
        int sr, sg, sb;
        int m = image.getHeight();
        int n = image.getWidth();
        int[] rgb = image.getRGB(0, 0, n, m, null, 0, n);
        int[] newrgb = new int[rgb.length];
        int r0, r1, r2, r3, r4, r5, r6, r7;
        int g0, g1, g2, g3, g4, g5, g6, g7;
        int b0, b1, b2, b3, b4, b5, b6, b7;
        int mr, mg, mb;
        int c;
        int tr, tb, tg;
        int max, suma;
        int tmp1, tmp2;
        for (int i = 0; i < n; i++) {
            newrgb[i] = rgb[i];
            newrgb[rgb.length - 1 - i] = rgb[rgb.length - 1 - i];
        }
        for (int i = 1; i < m - 1; i++) {
            newrgb[i * n] = rgb[i * n];
            newrgb[(i + 1) * n - 1] = rgb[(i + 1) * n - 1];
            for (int j = 1; j < n - 1; j++) {
                max = 1;
                mr = mg = 0;
                mb = 1;
                c = n * i + j;

                sr = sg = sb = 0;
                sr += (r0 = ((rgb[c - n - 1] >>> 16) & 0xFF)) + (r1 = ((rgb[c - n] >>> 16) & 0xFF)) + (r2 = ((rgb[c - n + 1] >>> 16) & 0xFF)) +
                        (r7 = ((rgb[c - 1] >>> 16) & 0xFF)) + (r3 = ((rgb[c + 1] >>> 16) & 0xFF)) +
                        (r6 = ((rgb[c + n - 1] >>> 16) & 0xFF)) + (r5 = ((rgb[c + n] >>> 16) & 0xFF)) + (r4 = ((rgb[c + n + 1] >>> 16) & 0xFF));
                sg += (g0 = ((rgb[c - n - 1] >>> 8) & 0xFF)) + (g1 = ((rgb[c - n] >>> 8) & 0xFF)) + (g2 = ((rgb[c - n + 1] >>> 8) & 0xFF)) +
                        (g7 = ((rgb[c - 1] >>> 8) & 0xFF)) + (g3 = ((rgb[c + 1] >>> 8) & 0xFF)) +
                        (g6 = ((rgb[c + n - 1] >>> 8) & 0xFF)) + (g5 = ((rgb[c + n] >>> 8) & 0xFF)) + (g4 = ((rgb[c + n + 1] >>> 8) & 0xFF));
                sb += (b0 = ((rgb[c - n - 1]) & 0xFF)) + (b1 = ((rgb[c - n]) & 0xFF)) + (b2 = ((rgb[c - n + 1]) & 0xFF)) +
                        (b7 = ((rgb[c - 1]) & 0xFF)) + (b3 = ((rgb[c + 1]) & 0xFF)) +
                        (b6 = ((rgb[c + n - 1]) & 0xFF)) + (b5 = ((rgb[c + n]) & 0xFF)) + (b4 = ((rgb[c + n + 1]) & 0xFF));
                suma = ((tr = r1 + r2)) + (tg = (g1 + g2)) + (tb = (b1 + b2));
                if ((tmp1 = (r0 + g0 + b0)) > (tmp2 = (r3 + b3 + g3))) {
                    suma += tmp1;
                    if (suma > max) {
                        max = suma;
                        mr = r0 + tr;
                        mg = g0 + tg;
                        mb = b0 + tb;
                    }
                } else {
                    suma += tmp2;
                    if (suma > max) {
                        max = suma;
                        mr = r3 + tr;
                        mg = g3 + tg;
                        mb = b3 + tb;
                    }
                }
                suma = ((tr = r3 + r4)) + (tg = (g3 + g4)) + (tb = (b3 + b4));
                if ((tmp1 = (r2 + g2 + b2)) > (tmp2 = (r5 + g5 + b5))) {
                    suma += tmp1;
                    if (suma > max) {
                        max = suma;
                        mr = r2 + tr;
                        mg = g2 + tg;
                        mb = b2 + tb;
                    }
                } else {
                    suma += tmp2;
                    if (suma > max) {
                        max = suma;
                        mr = r5 + tr;
                        mg = g5 + tg;
                        mb = b5 + tb;
                    }
                }
                suma = ((tr = r5 + r6)) + (tg = (g6 + g5)) + (tb = (b5 + b6));
                if ((tmp1 = (r4 + g4 + b4)) > (tmp2 = (r7 + g7 + b7))) {
                    suma += tmp1;
                    if (suma > max) {
                        max = suma;
                        mr = r4 + tr;
                        mg = g4 + tg;
                        mb = b4 + tb;
                    }
                } else {
                    suma += tmp2;
                    if (suma > max) {
                        max = suma;
                        mr = r7 + tr;
                        mg = g7 + tg;
                        mb = b7 + tb;
                    }
                }
                suma = ((tr = r7 + r0)) + (tg = (g7 + g0)) + (tb = (b7 + b0));
                if ((tmp1 = (r1 + g1 + b1)) > (tmp2 = (r6 + g6 + b6))) {
                    suma += tmp1;
                    if (suma > max) {
                        max = suma;
                        mr = r1 + tr;
                        mg = g1 + tg;
                        mb = b1 + tb;
                    }
                } else {
                    suma += tmp2;
                    if (suma > max) {
                        max = suma;
                        mr = r6 + tr;
                        mg = g6 + tg;
                        mb = b6 + tb;
                    }
                }
                r = Math.abs(8 * mr - 3 * sr);
                g = Math.abs(8 * mg - 3 * sg);
                b = Math.abs(8 * mb - 3 * sb);
                ograniczRGB();
                newrgb[n * i + j] = (r << 16 | g << 8 | b);
            }
        }
        TIME = System.currentTimeMillis() - TIME;
        image.setRGB(0, 0, n, m, newrgb, 0, n);
        return TIME;
    }

    private long splatajSzary(BufferedImage image) {
        long TIME = System.currentTimeMillis();
        int sb;
        int m = image.getHeight();
        int n = image.getWidth();
        int[] rgb = image.getRGB(0, 0, n, m, null, 0, n);
        int[] newrgb = new int[rgb.length];

        int b0, b1, b2, b3, b4, b5, b6, b7;
        int mb;
        int c;

        int suma;
        for (int i = 0; i < n; i++) {
            newrgb[i] = rgb[i];
            newrgb[rgb.length - 1 - i] = rgb[rgb.length - 1 - i];
        }
        for (int i = 1; i < m - 1; i++) {
            newrgb[i * n] = rgb[i * n];
            newrgb[(i + 1) * n - 1] = rgb[(i + 1) * n - 1];
            for (int j = 1; j < n - 1; j++) {

                mb = 1;
                c = n * i + j;

                sb = 0;
                sb += (b0 = ((rgb[c - n - 1]) & 0xFF)) + (b1 = ((rgb[c - n]) & 0xFF)) + (b2 = ((rgb[c - n + 1]) & 0xFF)) +
                        (b7 = ((rgb[c - 1]) & 0xFF)) + (b3 = ((rgb[c + 1]) & 0xFF)) +
                        (b6 = ((rgb[c + n - 1]) & 0xFF)) + (b5 = ((rgb[c + n]) & 0xFF)) + (b4 = ((rgb[c + n + 1]) & 0xFF));
                suma = b1 + b2;
                if (b0 > b3) {
                    suma += b0;
                } else {
                    suma += b3;
                }
                if (suma > mb) {
                    mb = suma;
                }
                suma = b3 + b4;
                if (b2 > b5) {
                    suma += b2;
                } else {
                    suma += b5;
                }
                if (suma > mb) {
                    mb = suma;
                }
                suma = b5 + b6;
                if (b4 > b7) {
                    suma += b4;

                } else {
                    suma += b7;
                }
                if (suma > mb) {
                    mb = suma;
                }
                suma = b7 + b0;
                if (b1 > b6) {
                    suma += b1;

                } else {
                    suma += b6;
                }
                if (suma > mb) {
                    mb = suma;
                }
                b = Math.abs(8 * mb - 3 * sb);
                if (b > 255) {
                    b = 255;
                } else if (b < 0) {
                    b = 0;
                }
                newrgb[n * i + j] = (b << 16 | b << 8 | b);
            }
        }
        TIME = System.currentTimeMillis() - TIME;
        image.setRGB(0, 0, n, m, newrgb, 0, n);
        return TIME;
    }

    private void ograniczRGB() {
        if (r > 255) {
            r = 255;
        } else if (r < 0) {
            r = 0;
        }
        if (b > 255) {
            b = 255;
        } else if (b < 0) {
            b = 0;
        }
        if (g > 255) {
            g = 255;
        } else if (g < 0) {
            g = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (MyInternalFrame frame : WindowManager.getInstance().getFrames()) {
            if (frame.isSelected()) {
                long TIME;
                BufferedImage image = frame.getPicture().getImage();
                if (image.getType() == BufferedImage.TYPE_BYTE_GRAY || image.getType() == BufferedImage.TYPE_USHORT_GRAY) {
                    TIME = splatajSzary(image);
                } else {
                    TIME = splataj(image);
                }
                System.out.println("kirsz: " + TIME);

                frame.repaint();
                JOptionPane.showMessageDialog(null, "" + TIME);
                return;
            }
        }
    }

    @Override
    public JMenuItem getJMenuItem() {
        return menu;
    }

    @Override
    public List runThisTool(
            List<BufferedImage> images, List arguments) {
        return super.runThisTool(images, arguments);
    }
}


