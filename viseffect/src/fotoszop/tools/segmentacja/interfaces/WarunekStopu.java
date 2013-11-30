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
 abstract public class WarunekStopu {

    protected double prog;

    public WarunekStopu(double prog) {
        this.prog = prog;
    }

    public WarunekStopu() {
        this.prog = 100;
    }
    abstract public void oblicz(Region r, int[] rgb);

    abstract public boolean czyKoniec(int[] rgb, Region r);
}
