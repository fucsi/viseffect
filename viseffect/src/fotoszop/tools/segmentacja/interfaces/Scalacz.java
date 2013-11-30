/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.segmentacja.interfaces;

import fotoszop.tools.segmentacja.*;

/**
 *
 * @author kalkan
 */
public abstract class Scalacz {

    protected double prog;

    public Scalacz(double prog) {
        this.prog = prog;
    }

    public Scalacz() {
        this.prog = 0.003;
    }

    public boolean czySasiadujace(Region r1, Region r2) {
        if ((r1.composites == null || r1.composites.isEmpty()) && (r2.composites == null || r2.composites.isEmpty())) {
            if (r1.isLeaf && r2.isLeaf == false) {
                return false;
            }
            if (r1.y0 == r2.y1) {
                if ((r2.x0 < r1.x1 && r2.x0 >= r1.x0) || (r2.x1 > r1.x0 && r2.x1 <= r1.x1)) {
                    return true;
                }
            }
            if (r2.y0 == r1.y1) {
                if ((r1.x0 < r2.x1 && r1.x0 >= r2.x0) || (r1.x1 > r2.x0 && r1.x1 <= r2.x1)) {
                    return true;
                }
            }
            if (r1.x0 == r2.x1) {
                if ((r2.y0 < r1.y1 && r2.y0 >= r1.y0) || (r2.y1 > r1.y0 && r2.y1 <= r1.y1)) {
                    return true;
                }
            }
            if (r2.x0 == r1.x1) {
                if ((r1.y0 < r2.y1 && r1.y0 >= r2.y0) || (r1.y1 > r2.y0 && r1.y1 <= r2.y1)) {
                    return true;
                }
            }
            return false;
        }else{
            if(r1.composites != null && !r1.composites.isEmpty() && r2.composites != null && !r2.composites.isEmpty()){
                for (Region reg1 : r1.composites) {
                    for (Region reg2 : r2.composites) {
                        if (czySasiadujace(reg1, reg2)) {
                            return true;
                        }
                    }
                }
                return false;
            }else{
                Region zmer,nzmer;
                if (r1.composites.isEmpty()) {
                    zmer=r2;
                    nzmer=r1;
                }else{
                    zmer=r1;
                    nzmer=r2;
                }
                for (Region reg1 : zmer.composites) {
                    if (czySasiadujace(reg1, nzmer)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    abstract public boolean czyMergowac(Region r1, Region r2, int[] rgb, WarunekStopu stop);

    abstract public Region merguj(Region r1, Region r2);
}
