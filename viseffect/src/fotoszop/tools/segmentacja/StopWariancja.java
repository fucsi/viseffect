/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.segmentacja;

import fotoszop.tools.segmentacja.interfaces.WarunekStopu;

/**
 *
 * @author kalkan
 */
public class StopWariancja extends WarunekStopu {

    public int limit = 1;

    public StopWariancja(double prog, int limit) {
        super(prog);
        this.limit = limit;
    }

    @Override
    @SuppressWarnings("static-access")
    public boolean czyKoniec(int[] rgb, Region r) {
        double avg;
        if (r.composites == null || r.composites.isEmpty()) {
            if (r.visited) {
                return r.var <= prog || (r.x1 - r.x0) <= limit || (r.y1 - r.y0) <= limit;
            }

            if ((r.x1 - r.x0) <= limit || (r.y1 - r.y0) <= limit) {
                for (int j = r.y0; j < r.y1; j++) {
                    for (int i = r.x0; i < r.x1; i++) {

                        int pix = rgb[i + j * r.W];
                        r.sum += (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                        ++r.amount;
                    }
                }
                r.mean = (double) r.sum / (r.amount * 3);
                double war = 0;
                int sum;
                double tmp = 0;
                for (int j = r.y0; j < r.y1; j++) {
                    for (int i = r.x0; i < r.x1; i++) {
                        int pix = rgb[i + j * r.W];
                        sum = (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                        tmp = (double) sum / 3.0 - r.mean;
                        war += tmp * tmp;
                    }
                }
                war /= r.amount;
                r.var = war;
                r.visited = true;
                return true;
            }
            for (int j = r.y0; j < r.y1; j++) {
                for (int i = r.x0; i < r.x1; i++) {
                    int pix = rgb[i + j * r.W];
                    r.sum += (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                    ++r.amount;
                }
            }
            r.mean = avg = (double) r.sum / (r.amount * 3);
            double war = 0;
            int sum;
            double tmp = 0;
            for (int j = r.y0; j < r.y1; j++) {
                for (int i = r.x0; i < r.x1; i++) {
                    int pix = rgb[i + j * r.W];
                    sum = (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                    tmp = (double) sum / 3.0 - avg;
                    war += tmp * tmp;
                }
            }
            war /= r.amount;
            r.var = war;
            r.visited = true;
            return war <= prog;
        } else {
            if (r.visited) {
                return r.var <= prog;
            }

            for (Region region : r.composites) {
                r.sum += region.sum;
                r.amount += region.amount;
            }
            r.mean = avg = (double) r.sum / (r.amount * 3);
            double war = 0;
            double tmp = 0;
            int sum;
            for (Region region : r.composites) {
                for (int j = region.y0; j < region.y1; j++) {
                    for (int i = region.x0; i < region.x1; i++) {
                        int pix = rgb[i + j * r.W];
                        sum = (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                        tmp = (double) sum / 3.0 - avg;
                        war += tmp * tmp;
                    }
                }
            }
            war /= r.amount;
            r.var = war;
            r.visited = true;
            return war <= prog;
        }
    }

    @SuppressWarnings("static-access")
    public void oblicz(Region r, int[] rgb) {
        double avg;
        r.sum = r.amount = 0;
        if (r.composites == null || r.composites.isEmpty()) {
            for (int j = r.y0; j < r.y1; j++) {
                for (int i = r.x0; i < r.x1; i++) {

                    int pix = rgb[i + j * r.W];
                    r.sum += (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                    ++r.amount;
                }
            }
            r.mean = avg = (double) r.sum / (r.amount * 3);
            double war = 0;
            int sum;
            double tmp = 0;
            for (int j = r.y0; j < r.y1; j++) {
                for (int i = r.x0; i < r.x1; i++) {
                    int pix = rgb[i + j * r.W];
                    sum = (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                    tmp = (double) sum / 3.0 - avg;
                    war += tmp * tmp;
                }
            }
            war /= r.amount;
            r.var = war;
            r.visited = true;
        } else {
            for (Region region : r.composites) {
                r.sum += region.sum;
                r.amount += region.amount;
            }
            r.mean = avg = (double) r.sum / (r.amount * 3);
            double war = 0;
            double tmp = 0;
            int sum;
            for (Region region : r.composites) {
                for (int j = region.y0; j < region.y1; j++) {
                    for (int i = region.x0; i < region.x1; i++) {
                        int pix = rgb[i + j * r.W];
                        sum = (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                        tmp = (double) sum / 3.0 - avg;
                        war += tmp * tmp;
                    }
                }
            }
            war /= r.amount;
            r.var = war;
            r.visited = true;
        }
    }
}
