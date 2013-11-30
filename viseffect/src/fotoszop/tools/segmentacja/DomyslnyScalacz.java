/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.segmentacja;

import fotoszop.tools.segmentacja.interfaces.Scalacz;
import fotoszop.tools.segmentacja.interfaces.WarunekStopu;

/**
 *
 * @author kalkan
 */
public class DomyslnyScalacz extends Scalacz {

    public DomyslnyScalacz(double progScal) {
        super(progScal);
    }

    @Override
    public boolean czyMergowac(Region r1, Region r2, int[] rgb, WarunekStopu stop) {
        if (r1.visited && r2.visited) {
            return Math.abs(r1.mean - r2.mean) / Math.sqrt(r1.var * r1.var + r2.var * r2.var) <= prog;
        } else {
            Region[] rtab = {r1, r2};
            for (Region r : rtab) {
                if (!r.visited) {
                    stop.oblicz(r, rgb);
                }
            }
            return Math.abs(r1.mean - r2.mean) / Math.sqrt(r1.var * r1.var + r2.var * r2.var) <= prog;
        }
    }

    @Override
    public Region merguj(Region r1, Region r2) {
        return new Region(r1, r2);
    }
}
