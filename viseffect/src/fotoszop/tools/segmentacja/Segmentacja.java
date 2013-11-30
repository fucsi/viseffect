/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.segmentacja;

import fotoszop.tools.segmentacja.interfaces.Scalacz;
import fotoszop.tools.segmentacja.interfaces.WarunekStopu;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kalkan
 */
public class Segmentacja {

    WarunekStopu stop;
    Scalacz scalacz;

    public Segmentacja(WarunekStopu stop, Scalacz scalacz) {
        this.stop = stop;
        this.scalacz = scalacz;
    }

    public void segmentuj(BufferedImage image) {
        int W = image.getWidth();
        int H = image.getHeight();
        int[] rgb = image.getRGB(0, 0, W, H, null, 0, W);
        leaves.clear();
        Region.W = W;
        Region root = new Region(0, W, 0, H, null);
        try {
            dziel(rgb, root);
        } catch (StackOverflowError e) {
            e.printStackTrace();
            return;
        }
        System.out.println(leaves.size());

        /*for (int i = 0; i < leaves.size() - 1; i++) {
        for (int j = i + 1; j < leaves.size(); j++) {
        System.out.println(i+"-"+j);
        Region ri = leaves.get(i);
        Region rj = leaves.get(j);
        for (int ik = 0; ik < ri.children.size(); ik++) {
        for (int jk = 0; jk < rj.children.size(); jk++) {
        Region rik = ri.children.get(ik);
        Region rjk = rj.children.get(jk);
        if (scalacz.czySasiadujace(rik, rjk)) {
        if (scalacz.czyMergowac(rik, rjk, rgb)) {
        scalacz.merguj(rik, rjk);
        }
        }
        }
        }
        }
        }*/
        for (Region region : leaves) {

            fill(rgb, region);

        }
        image.setRGB(0, 0, W, H, rgb, 0, W);
    }
    private ArrayList<Region> leaves = new ArrayList<Region>();

    public void dziel(int[] rgb, Region r) {
        if (stop.czyKoniec(rgb, r)) {
            leaves.add(r);
            //fill(rgb, r);
            return;
        } else {
            int x1 = r.x0 + ((r.x1 - r.x0) >>> 1);
            int y1 = r.y0 + ((r.y1 - r.y0) >>> 1);

            Region nw, ne, sw, se;
            nw = new Region(r.x0, x1, r.y0, y1, r);
            ne = new Region(x1, r.x1, r.y0, y1, r);
            sw = new Region(r.x0, x1, y1, r.y1, r);
            se = new Region(x1, r.x1, y1, r.y1, r);

            stop.oblicz(nw, rgb);
            stop.oblicz(sw, rgb);
            stop.oblicz(ne, rgb);
            stop.oblicz(se, rgb);

            mozeZlacz(rgb, nw, sw, ne, se);
            for (Region region : r.children) {
                dziel(rgb, region);
            }

        }
    }

    @SuppressWarnings("static-access")
    public void fill(int[] rgb, Region r) {
        if (!r.visited) {
            this.stop.oblicz(r, rgb);
        }
        if (r.composites == null || r.composites.isEmpty()) {
            for (int j = r.y0; j < r.y1; j++) {
                for (int i = r.x0; i < r.x1; i++) {
                    int pix = (int) r.mean;
                    rgb[i + j * r.W] = (pix << 16) | (pix << 8) | (pix);
                }
            }
        } else {
            for (Region region : r.composites) {
                for (int j = region.y0; j < region.y1; j++) {
                    for (int i = region.x0; i < region.x1; i++) {
                        int pix = (int) r.mean;
                        rgb[i + j * r.W] = (pix << 16) | (pix << 8) | (pix);
                    }
                }
            }
        }
    }

    private void mozeZlacz(int[] rgb, Region nw, Region sw, Region ne, Region se) {
        if (!(nw.isLeaf && sw.isLeaf && ne.isLeaf && se.isLeaf)) {
            return;
        }
        int c = 0;

        Region n = null,s = null,w = null,e = null;

        if (scalacz.czyMergowac(nw, sw, rgb, this.stop)) {
           w = new Region(null);
           w.x0 = nw.x0;
           w.x1 = nw.x1;
           w.y0 = nw.y0;
           w.y1 = sw.y1;
           w.isLeaf = true;
           w.visited = false;
           w.parent = nw.parent;           
        }
        if (scalacz.czyMergowac(nw, ne, rgb, this.stop)) {
           n = new Region(null);
           n.x0 = nw.x0;
           n.x1 = ne.x1;
           n.y0 = nw.y0;
           n.y1 = nw.y1;
           n.isLeaf = true;
           n.visited = false;
           n.parent = nw.parent;
        }
        if (scalacz.czyMergowac(ne, se, rgb, this.stop)) {
           e = new Region(null);
           e.x0 = ne.x0;
           e.x1 = ne.x1;
           e.y0 = ne.y0;
           e.y1 = se.y1;
           e.isLeaf = true;
           e.visited = false;
           e.parent = ne.parent;
        }
        if (scalacz.czyMergowac(sw, se, rgb, this.stop)) {
           s = new Region(null);
           s.x0 = sw.x0;
           s.x1 = se.x1;
           s.y0 = sw.y0;
           s.y1 = sw.y1;
           s.isLeaf = true;
           s.visited = false;
           s.parent = sw.parent;
        }

        if(n!=null && s!=null){
            Region p = nw.parent;
            p.children.remove(nw);
            p.children.remove(sw);
            p.children.remove(ne);
            p.children.remove(se);
            p.children.add(n);
            p.children.add(s);
            return;
        }
        if(w!=null && e!=null){
            Region p = nw.parent;
            p.children.remove(nw);
            p.children.remove(sw);
            p.children.remove(ne);
            p.children.remove(se);
            p.children.add(w);
            p.children.add(e);
            return;
        }
        if(n!=null){
            Region p = nw.parent;
            p.children.remove(nw);
            p.children.remove(ne);
            p.children.add(n);
            return;
        }
        if(s!=null){
            Region p = nw.parent;
            p.children.remove(sw);
            p.children.remove(se);
            p.children.add(s);
            return;
        }
        if(w!=null){
            Region p = nw.parent;
            p.children.remove(nw);
            p.children.remove(sw);
            p.children.add(w);
            return;
        }
        if(e!=null){
            Region p = nw.parent;
            p.children.remove(ne);
            p.children.remove(se);
            p.children.add(e);
            return;
        }

    }
    /*private void mozeZlacz(int[] rgb, Region nw, Region sw, Region ne, Region se) {

    if (scalacz.czyMergowac(nw, sw, rgb, this.stop)) {
    Region w = scalacz.merguj(nw, sw);
    if (scalacz.czyMergowac(w, ne, rgb, this.stop)) {
    Region wne = scalacz.merguj(w, ne);
    if (scalacz.czyMergowac(wne, se, rgb, this.stop)) {
    scalacz.merguj(wne, se);
    }
    } else if (scalacz.czyMergowac(w, se, rgb, this.stop)) {

    Region wse = scalacz.merguj(w, se);
    if (scalacz.czyMergowac(wse, ne, rgb, this.stop)) {
    scalacz.merguj(wse, ne);
    }
    }
    } else if (scalacz.czyMergowac(nw, ne, rgb, this.stop)) {

    Region n = scalacz.merguj(nw, ne);
    if (scalacz.czyMergowac(n, se, rgb, this.stop)) {

    Region nse = scalacz.merguj(n, se);
    if (scalacz.czyMergowac(nse, sw, rgb, this.stop)) {
    scalacz.merguj(nse, sw);
    }
    } else if (scalacz.czyMergowac(n, sw, rgb, this.stop)) {

    Region nsw = scalacz.merguj(n, sw);
    if (scalacz.czyMergowac(nsw, se, rgb, this.stop)) {
    scalacz.merguj(nsw, se);
    }
    }
    } else if (scalacz.czyMergowac(sw, se, rgb, this.stop)) {

    Region s = scalacz.merguj(sw, se);
    if (scalacz.czyMergowac(s, ne, rgb, this.stop)) {

    Region sne = scalacz.merguj(s, ne);
    if (scalacz.czyMergowac(sne, nw, rgb, this.stop)) {
    scalacz.merguj(sne, nw);
    }
    } else if (scalacz.czyMergowac(s, nw, rgb, this.stop)) {

    Region snw = scalacz.merguj(s, nw);
    if (scalacz.czyMergowac(snw, ne, rgb, this.stop)) {
    scalacz.merguj(snw, ne);
    }
    }
    } else if (scalacz.czyMergowac(ne, se, rgb, this.stop)) {

    Region e = scalacz.merguj(ne, se);
    if (scalacz.czyMergowac(e, nw, rgb, this.stop)) {

    Region enw = scalacz.merguj(e, nw);
    if (scalacz.czyMergowac(enw, sw, rgb, this.stop)) {
    scalacz.merguj(enw, sw);
    }
    } else if (scalacz.czyMergowac(e, sw, rgb, this.stop)) {

    Region esw = scalacz.merguj(e, sw);
    if (scalacz.czyMergowac(esw, nw, rgb, this.stop)) {
    scalacz.merguj(esw, nw);
    }
    }
    }
    }
     */
}
