/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.morfo;

import java.util.ArrayList;

/**
 *
 * @author kalkan
 */
public class ElementStrukturalny {

    ArrayList<RLE> element = new ArrayList<RLE>();

    public ElementStrukturalny(int[] tab) {
        int[] lookup = {1, 0, -1};
        int width = 3;
        int height = 3;
        boolean flag = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tab[i * width + j] == 1) {
                    for (int k = j + 1; k < width; k++) {
                        if (tab[i * width + k] == 0) {
                            element.add(new RLE(lookup[k - 1], lookup[j], lookup[i]));
                            j = k;
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        element.add(new RLE(lookup[width - 1], lookup[j], lookup[i]));
                        j = width;
                    }
                    flag = false;
                }
            }
        }
    }

    public ArrayList<RLE> dyluj(RLE rle) {
        ArrayList<RLE> res = new ArrayList<RLE>();
        for (int i = 0; i < element.size(); i++) {
            RLE el = element.get(i);
            res.add(RLE.createNewBounded(rle.x0 + el.x0, rle.x1 + el.x1, rle.y + el.y));
        }
        return res;
    }

    public ArrayList<RLE> eruj(RLE rle) {
        ArrayList<RLE> res = new ArrayList<RLE>();
        for (int i = 0; i < element.size(); i++) {
            RLE el = element.get(i);
            if (rle.x1 - rle.x0 > el.x1 - el.x0) {
                res.add(RLE.createNewBounded(rle.x0 - el.x0, rle.x1 - el.x1, rle.y - el.y));
            }else{
                res.add(null);
            }
        }
        return res;
    }
}
