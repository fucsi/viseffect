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
public class Splot extends ToolInterface {

    private double w,  w1 = 1.0 / 9,  w2 = 0.1,  w3 = 0.0625;
    private double[] filtr1 = {1, 1, 1,
        1, 1, 1,
        1, 1, 1};
    private double[] filtr2 = {1, 1, 1,
        1, 2, 1,
        1, 1, 1};
    private double[] filtr3 = {1, 2, 1,
        2, 4, 2,
        1, 2, 1};
    private double[] filtr = new double[9],  ftmp;
    double red, green, blue;
    int r,g,b;
    JMenuItem menu;
    public Splot() {
        
        menu = new JMenuItem("Splot - przykladowe warianty");
        menu.setIcon(this.getResourceMap().getIcon("icons.splotexamples"));
        menu.addActionListener(this);
    }

    private long splataj(BufferedImage image) {
        int m = image.getHeight();
        int n = image.getWidth();
        int[] rgb = image.getRGB(0, 0, n, m, null, 0, n);
        int[] newrgb = new int[rgb.length];
        long TIME = System.currentTimeMillis();
        
        for (int i = 0; i < n; i++) {
            newrgb[i] = rgb[i];
            newrgb[rgb.length - 1 - i] = rgb[rgb.length - 1 - i];
        }
        for (int i = 1; i < m - 1; i++) {
            newrgb[i * n] = rgb[i * n];
            newrgb[(i + 1) * n - 1] = rgb[(i + 1) * n - 1];
            for (int j = 1; j < n - 1; j++) {
                red = green = blue = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        red += ((rgb[(i + k) * n + j + l] >>> 16) & 0xFF) * filtr[4 + l + 3 * k];
                        green += ((rgb[(i + k) * n + j + l] >>> 8) & 0xFF) * filtr[4 + l + 3 * k];
                        blue += (rgb[(i + k) * n + j + l] & 0xFF) * filtr[4 + l + 3 * k];

                    }
                }
                konwertuj();
                newrgb[n * i + j] = (r << 16 | g << 8 | b);
            }
        }
        TIME = System.currentTimeMillis() - TIME;
        image.setRGB(0, 0, n, m, newrgb, 0, n);
        return TIME;
    }

    private void konwertuj() {
        if (red > 255) {
            red = 255;
        } else if (red < 0) {
            red = 0;
        }
        if (blue > 255) {
            blue = 255;
        } else if (blue < 0) {
            blue = 0;
        }
        if (green > 255) {
            green = 255;
        } else if (green < 0) {
            green = 0;
        }
        r = (int) Math.round(red);
        g = (int) Math.round(green);
        b = (int) Math.round(blue);
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        Object[] possibilities = {"1", "2", "3"};
        String s = (String) JOptionPane.showInputDialog(
                null,
                "Wybierz filtr:\n" +
                "  1)        2)        3)\n" +
                "1 1 1    1 1 1    1 2 1\n" +
                "1 1 1    1 2 1    2 4 2\n" +
                "1 1 1    1 1 1    1 2 1\n" +
                "w = 1/9    w=1/10      w=1/16",
                "Splot",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "1");

        if ((s != null) && (s.length() > 0)) {
            if (s.compareTo("1") == 0) {
                w = w1;
                ftmp = filtr1;
            } else if (s.compareTo("2") == 0) {
                w = w2;
                ftmp = filtr2;
            } else if (s.compareTo("3") == 0) {
                w = w3;
                ftmp = filtr3;
            }
            for (int i = 0; i < filtr.length; i++) {
                filtr[i] = ftmp[i] * w;
            }
            for (MyInternalFrame frame : WindowManager.getInstance().getFrames()) {
                if (frame.isSelected()) {
                    long TIME;
                    BufferedImage image = frame.getPicture().getImage();
                    
                        TIME = splataj(image);
                    System.out.println("splot: "+TIME);

                    frame.repaint();
                    JOptionPane.showMessageDialog(null, ""+TIME);
                    return;
                }
            }
        }

    }

    @Override
    public JMenuItem getJMenuItem() {
        return menu;
    }

    @Override
    public List runThisTool(List<BufferedImage> images, List arguments) {
        return super.runThisTool(images, arguments);
    }
}
