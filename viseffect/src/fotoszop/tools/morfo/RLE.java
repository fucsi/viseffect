/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.morfo;

import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 *
 * @author kalkan
 */
public class RLE {

    static private int width,  height;
    public int x0,  x1,  y;

    public static void setSize(int height, int width) {
        RLE.height = height;
        RLE.width = width;
    }

    public RLE(int x0, int x1, int y) {
        this.x0 = x0;
        this.x1 = x1;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RLE other = (RLE) obj;
        if (this.x0 != other.x0) {
            return false;
        }
        if (this.x1 != other.x1) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.x0;
        hash = 19 * hash + this.x1;
        hash = 19 * hash + this.y;
        return hash;
    }

    static public RLE createNewBounded(int x0, int x1, int y) {
        x0 = x0 < 0 ? 0 : x0;
        x1 = x1 >= width ? width - 1 : x1;
        y = y < 0 ? 0 : (y >= height ? height - 1 : y);
        return new RLE(x0, x1, y);
    }

    @Override
    public String toString() {
        return "<" + x0 + "," + x1 + "," + y + ">";
    }

    public boolean isOutisdeOf(RLE r) {
        if (r.x0 > this.x1 || r.x1 < this.x0) {
            return true;
        }
        return false;
    }

    public boolean isInsideOf(RLE r) {
        if (r.x0 <= this.x0 && r.x1 >= this.x1) {
            return true;
        }
        return false;
    }

    
}
