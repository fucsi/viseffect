/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotoszop.tools.morfo;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author kalkan
 */
abstract public class Morfizm {

    abstract public Hashtable<Integer, ArrayList<RLE>> getWynik();

    abstract public void morfuj(RLE rle);

    protected void binaryAdd(ArrayList<RLE> list, RLE rle) {
        int index = binarySearch(list, 0, list.size(), rle.x0);
        list.add(index, rle);
    }

    protected int binarySearch(ArrayList<RLE> a, int low, int high, int key) {
        int mid;

        if (low == high) {
            return low;
        }

        mid = low + ((high - low) / 2);

        if (key > a.get(mid).x0) {
            return binarySearch(a, mid + 1, high, key);
        } else if (key < a.get(mid).x0) {
            return binarySearch(a, low, mid, key);
        }
        return mid;
    }

    protected void merge(ArrayList<RLE> wiersz) {
        if (wiersz.isEmpty()) {
            return;
        }
        int size = wiersz.size();
        RLE rle1, rle2;
        rle1 = wiersz.get(0);
        for (int i = 0; i + 1 < size; i++) {
            rle2 = wiersz.get(i + 1);

            if (rle1.x0 <= rle2.x0 && rle1.x1 >= rle2.x0) {
                rle2.x0 = rle1.x0;
                wiersz.remove(i);
                i--;
                size--;
            }

            rle1 = rle2;
        }
    }
}
