/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotoszop.tools.filters;

/**
 *
 * @author kalkan
 */
public class LookupTable {
    double[][] lookup;

    public LookupTable(int cathegoryNumber){
        lookup = new double[cathegoryNumber][];
    }

    public void populate(int[] base,double w){
        for (int i = 0; i < lookup.length; i++) {
            lookup[i] = new double[256];
            for (int j = 0; j < lookup[i].length; j++) {
                lookup[i][j] = j * base[i] *w;
            }
        }
    }

    public double get(int cathegory,int index){
        return lookup[cathegory][index];
    }
}
