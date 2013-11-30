/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.segmentacja;

import java.util.ArrayList;

/**
 *
 * @author kalkan
 */
public class Region {

    public static int W;
    public int x0;
    public int x1;
    public int y0;
    public int y1;
    public boolean visited = false;
    public int sum = 0;
    public int amount = 0;
    public double mean = 0;
    public double var = 0;
    public boolean isLeaf = true;
    public Region parent = null;
    public ArrayList<Region> children = null;
    public ArrayList<Region> composites = null;

    Object[] dane = null;

    public Region(Region parent) {
        this.parent = parent;
        if (parent != null) {
            if (parent.children == null) {
                parent.children = new ArrayList<Region>();
            }
            parent.children.add(this);
            parent.isLeaf = false;
        }
    }

    public Region(int x0, int x1, int y0, int y1, Region parent) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.parent = parent;
        if (parent != null) {
            if (parent.children == null) {
                parent.children = new ArrayList<Region>();
            }
            parent.children.add(this);
            parent.isLeaf = false;
        }
    }

    public void getComposites(ArrayList<Region> result) {
        if (composites == null || composites.isEmpty()) {
            result.add(this);
            return;
        }

        for (Region region : composites) {
            region.getComposites(result);
        }
    }

    public Region(Region region1, Region region2) {

        if (region1.x0 == region2.x0) {
            this.x0 = region1.x0;
            this.x1 = region1.x1;
            this.y0 = region1.y0;
            this.y1 = region2.y1;
        } else if (region1.y0 == region2.y0) {
            this.x0 = region1.x0;
            this.x1 = region2.x1;
            this.y0 = region1.y0;
            this.y1 = region1.y1;
        }
        this.parent = region1.parent;

        if (this.parent != null) {
            this.parent.children.remove(region1);
            this.parent.children.add(0, this);
            this.parent.isLeaf = false;
        }
        if (region2.parent != null) {
            region2.parent.children.remove(region2);
        }
        this.isLeaf = true;
    }
    /*
     * public Region(Region region1, Region region2) {
    this.parent = region1.parent;
    //region1.parent = null;
    if (this.parent != null) {
    this.parent.children.remove(region1);
    this.parent.children.add(0,this);
    this.parent.isLeaf = false;
    }
    if(region2.parent!=null){
    region2.parent.children.remove(region2);

    //Region toDelete = region2.parent;
    //while(toDelete.children.isEmpty()){
    //toDelete.parent.children.remove(toDelete);
    //toDelete = toDelete.parent;
    //}
    }
    this.composites = new ArrayList<Region>();
    region1.getComposites(composites);
    region2.getComposites(composites);
    this.isLeaf = true;
    }*/

    @Override
    public String toString() {
        if (composites == null || composites.isEmpty()) {
            return "<" + x0 + "," + x1 + ";" + y0 + "," + y1 + ">";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Region region : composites) {
                sb.append(region.toString());
                sb.append(" ");
            }
            return sb.toString();
        }
    }
}
