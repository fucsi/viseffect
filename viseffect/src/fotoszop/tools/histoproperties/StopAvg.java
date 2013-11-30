/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.histoproperties;

import fotoszop.tools.segmentacja.Region;
import fotoszop.tools.segmentacja.interfaces.WarunekStopu;

/**
 *
 * @author kalkan
 */
public class StopAvg extends WarunekStopu {

    public int limit = 1;

    public StopAvg(double prog, int limit) {
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
                r.mean = r.var = (double) r.sum / (r.amount * 3);
                
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
            r.mean = r.var = (double) r.sum / (r.amount * 3);
            r.visited = true;
            return r.var <= prog;
        } else {
            if (r.visited) {
                return r.var <= prog;
            }

            for (Region region : r.composites) {
                r.sum += region.sum;
                r.amount += region.amount;
            }
            r.mean = r.var = (double) r.sum / (r.amount * 3);
            r.visited = true;
            return r.var <= prog;
        }
    }

    @SuppressWarnings("static-access")
    public void oblicz(Region r, int[] rgb) {
        r.sum = r.amount = 0;
        if (r.composites == null || r.composites.isEmpty()) {
            for (int j = r.y0; j < r.y1; j++) {
                for (int i = r.x0; i < r.x1; i++) {

                    int pix = rgb[i + j * r.W];
                    r.sum += (pix >>> 16 & 0xFF) + (pix >>> 8 & 0xFF) + (pix & 0xFF);
                    ++r.amount;
                }
            }
            r.mean = r.var = (double) r.sum / (r.amount * 3);
            r.visited = true;
        } else {
            for (Region region : r.composites) {
                r.sum += region.sum;
                r.amount += region.amount;
            }
            r.mean = r.var = (double) r.sum / (r.amount * 3);
            r.visited = true;
        }
    }
}
