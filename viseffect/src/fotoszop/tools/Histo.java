package fotoszop.tools;

import fotoszop.gui.Histogram;
import fotoszop.gui.MyInternalFrame;
import fotoszop.gui.WindowManager;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author kalkan
 */
public class Histo extends ToolInterface {

    JMenuItem menu;
    Histogram histo;
    MyInternalFrame selected;

    public Histo() {
        menu = new JMenuItem("Histogram obrazka");
        menu.setIcon(this.getResourceMap().getIcon("icons.histo"));
        menu.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedImage image = null;
        for (MyInternalFrame frame : WindowManager.getInstance().getFrames()) {
            if (frame.isSelected()) {
                image = frame.getPicture().getImage();
                selected = frame;
                histo = new Histogram(image);
                histo.addPropertyChangeListener(this);
                histo.setVisible(true);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Prosze najpierw wybrac obrazek");
    }

    @Override
    public JMenuItem getJMenuItem() {
        return menu;
    }

    private void histogramGray(int[] rgb, int[] brightness, int g0) {
        int index;
        int N = rgb.length;
        double max = 254 - g0;
        double tmp;

        int maxNonZero = -1;

        int[] condensed = new int[brightness.length];
        condensed[0] = brightness[0];
        for (int i = 1; i < brightness.length; i++) {
            condensed[i] = 0;
            condensed[i] = condensed[i - 1] + brightness[i];
        }
        for (int i = brightness.length - 1; i >= 0; i--) {
            if (brightness[i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    if (brightness[j] != 0) {
                        maxNonZero = j;
                        break;
                    }
                }
                break;
            }
        }
        if (maxNonZero == -1) {
            return;
        }
        double maxln = Math.log(1.0 - (double) condensed[maxNonZero] / N);

        for (int i = 0; i < rgb.length; i++) {
            index = rgb[i] & 0xFF;

            if (index > maxNonZero) {
                rgb[i] = 0xFFFFFF;
                continue;
            }

            tmp = Math.log(1.0 - (double) condensed[index] / N);

            tmp = tmp * max / maxln;

            index = (int) Math.round(g0 + tmp);

            rgb[i] = (index << 16) + (index << 8) + index;
        }

    }

    private void histogramColor(int[] rgb, int[] red, int[] green, int[] blue, int g0) {
        int indexr, indexg, indexb;
        int N = rgb.length;
        int[] sumr = new int[red.length], sumg = new int[green.length], sumb = new int[blue.length];
        double tmpr, tmpg, tmpb;
        double max = 254 - g0;

        sumr[0] = red[0];
        sumg[0] = green[0];
        sumb[0] = blue[0];
        for (int i = 1; i < sumb.length; i++) {
            sumb[i] = sumb[i - 1] + blue[i];
            sumr[i] = sumr[i - 1] + red[i];
            sumg[i] = sumg[i - 1] + green[i];
        }
        int redMaxNonZero = -1;
        int greenMaxNonZero = -1;
        int blueMaxNonZero = -1;
        for (int i = red.length - 1; i >= 0; i--) {
            if (red[i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    if (red[j] != 0) {
                        redMaxNonZero = j;
                        break;
                    }
                }
                break;
            }
        }
        for (int i = blue.length - 1; i >= 0; i--) {
            if (blue[i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    if (blue[j] != 0) {
                        blueMaxNonZero = j;
                        break;
                    }
                }
                break;
            }
        }
        for (int i = green.length - 1; i >= 0; i--) {
            if (green[i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    if (green[j] != 0) {
                        greenMaxNonZero = j;
                        break;
                    }
                }
                break;
            }
        }
        if (redMaxNonZero == -1 || blueMaxNonZero == -1 || greenMaxNonZero == -1) {
            return;
        }
        double maxlnr = Math.log(1.0 - (double) sumr[redMaxNonZero] / N);
        double maxlng = Math.log(1.0 - (double) sumg[greenMaxNonZero] / N);
        double maxlnb = Math.log(1.0 - (double) sumg[blueMaxNonZero] / N);


        for (int i = 0; i < rgb.length; i++) {
            indexr = (rgb[i] >>> 16) & 0xFF;
            if (indexr <= redMaxNonZero) {
                tmpr = Math.log(1.0 - (double) sumr[indexr] / N);
                tmpr = tmpr * max / maxlnr;
                indexr = (int) Math.round(g0 + tmpr);
            } else {
                indexr = 0xFF;
            }
            indexg = (rgb[i] >>> 8) & 0xFF;
            if (indexg <= greenMaxNonZero) {
                tmpg = Math.log(1.0 - (double) sumg[indexg] / N);
                tmpg = tmpg * max / maxlng;
                indexg = (int) Math.round(g0 + tmpg);
            } else {
                indexg = 0xFF;
            }
            indexb = rgb[i] & 0xFF;
            if (indexb <= blueMaxNonZero) {
                tmpb = Math.log(1.0 - (double) sumb[indexb] / N);
                tmpb = tmpb * max / maxlnb;
                indexb = (int) Math.round(g0 + tmpb);
            } else {
                indexb = 0xFF;
            }
            rgb[i] = (indexr << 16) + (indexg << 8) + indexb;

        }

    }

    private void histogramColorOpty(int[] rgb, int[] brightness, int g0) {
        int indexb,indexg,indexr,index;
        double newval;
        int N = rgb.length;
        double max = 254 - g0;
        double tmp;

        int maxNonZero = -1;

        int[] sum = new int[brightness.length];
        sum[0] = brightness[0];
        for (int i = 1; i < brightness.length; i++) {
            sum[i] = 0;
            sum[i] = sum[i - 1] + brightness[i];
        }
        for (int i = brightness.length - 1; i >= 0; i--) {
            if (brightness[i] != 0) {
                for (int j = i - 1; j >= 0; j--) {
                    if (brightness[j] != 0) {
                        maxNonZero = j;
                        break;
                    }
                }
                break;
            }
        }
        if (maxNonZero == -1) {
            return;
        }
        double maxln = Math.log(1.0 - (double) sum[maxNonZero] / N);

        for (int i = 0; i < rgb.length; i++) {
            indexb = rgb[i] & 0xFF;
            indexg = (rgb[i]>>>8) & 0xFF;
            indexr = (rgb[i]>>>16) & 0xFF;
            index = (int) Math.round((double)(indexb + indexg + indexr)/3.0);
            if (index > maxNonZero) {
                rgb[i] = 0xFFFFFF;
                continue;
            }

            tmp = Math.log(1.0 - (double) sum[indexb] / N);

            tmp = tmp * max / maxln;

            newval = Math.round(g0 + tmp);

            newval = newval / index;

            indexb *= newval;
            indexg *= newval;
            indexr *= newval;

            rgb[i] = (indexr << 16) | (indexg << 8) | indexb;
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().compareTo(histo.property1) == 0) {
            if (histo.isGray) {
                this.histogramGray(histo.rgb, histo.brightness, histo.g0);
            } else {
                switch ((Integer) evt.getNewValue()) {
                    case 1:
                        this.histogramColorOpty(histo.rgb, histo.brightness, histo.g0);
                        break;
                    default:
                        this.histogramColor(histo.rgb, histo.red, histo.green, histo.blue, histo.g0);
                }
            }
            histo.image.setRGB(0, 0, histo.image.getWidth(), histo.image.getHeight(), histo.rgb, 0, histo.image.getWidth());
            histo.rgb = histo.image.getRGB(0, 0, histo.image.getWidth(), histo.image.getHeight(), null, 0, histo.image.getWidth());
            histo.updateHistogram();
            selected.repaint();
        }
    }

    @Override
    public List runThisTool(List<BufferedImage> images, List arguments) {
        return super.runThisTool(images, arguments);
    }
}

/*
 * private void histogramGray(int[] rgb, int[] brightness, int g0, double a) {
        int index;
        int N = rgb.length;
        int sum = 0;
        double tmp;


        for (int i = 0; i < rgb.length; i++) {
            index = rgb[i] & 0xFF;
            sum = 0;
            for (int j = 0; j <= index; j++) {
                sum += brightness[j];
            }
            tmp = Math.log(1.0 - (double) sum / N);

            index = (int) Math.round(g0 - tmp / a);

            if (index < 0) {
                index = 0;
            } else if (index > 255) {
                index = 255;
            }

            rgb[i] = (index << 16) + (index << 8) + index;
        }

    }

    private void histogramColor(int[] rgb, int[] red, int[] green, int[] blue, int g0, double a) {
        int indexr, indexg, indexb;
        int N = rgb.length;
        int sumr = 0, sumg = 0, sumb = 0;
        double tmpr, tmpg, tmpb;



        for (int i = 0; i < rgb.length; i++) {
            indexr = (rgb[i] >>> 16) & 0xFF;
            indexg = (rgb[i] >>> 8) & 0xFF;
            indexb = rgb[i] & 0xFF;
            sumr = sumg = sumb = 0;
            for (int j = 0; j <= indexr; j++) {
                sumr += red[j];
            }
            for (int j = 0; j <= indexb; j++) {
                sumb += blue[j];
            }
            for (int j = 0; j <= indexg; j++) {
                sumg += green[j];
            }
            tmpr = Math.log(1.0 - (double) sumr / N);
            tmpg = Math.log(1.0 - (double) sumg / N);
            tmpb = Math.log(1.0 - (double) sumb / N);

            indexr = (int) Math.round(g0 - tmpr / a);
            indexg = (int) Math.round(g0 - tmpg / a);
            indexb = (int) Math.round(g0 - tmpb / a);

            if (indexr < 0) {
                indexr = 0;
            } else if (indexr > 255) {
                indexr = 255;
            }
            if (indexg < 0) {
                indexg = 0;
            } else if (indexg > 255) {
                indexg = 255;
            }
            if (indexb < 0) {
                indexb = 0;
            } else if (indexb > 255) {
                indexb = 255;
            }
            rgb[i] = (indexr << 16) + (indexg << 8) + indexb;
        }

    }
*/