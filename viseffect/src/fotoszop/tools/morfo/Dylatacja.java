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
public class Dylatacja extends Morfizm{

    Hashtable<Integer, ArrayList<RLE>> ciagi = new Hashtable<Integer, ArrayList<RLE>>();

    public Hashtable<Integer, ArrayList<RLE>> getWynik() {
        return ciagi;
    }
    ElementStrukturalny el;

    public Dylatacja(ElementStrukturalny el) {
        this.el = el;
    }

    public void morfuj(RLE rle) {
        ArrayList<RLE> r = el.dyluj(rle);
        boolean czyDodac = true;
        for (int i = 0; i < r.size(); i++) {
            RLE tmpRLE = r.get(i);
            if (ciagi.containsKey(tmpRLE.y)) {
                ArrayList<RLE> wiersz = ciagi.get(tmpRLE.y);
                for (int j = 0; j < wiersz.size(); j++) {
                    RLE mnoznik = wiersz.get(j);

                    if (tmpRLE.isOutisdeOf(mnoznik)) {
                        continue;
                    } else if (tmpRLE.isInsideOf(mnoznik)) {
                        czyDodac = false;
                        break;
                    } else {
                        if (tmpRLE.x0 >= mnoznik.x0) {
                            mnoznik.x1 = tmpRLE.x1;
                            czyDodac = false;
                            continue;
                        }
                        if (tmpRLE.x1 <= mnoznik.x1) {
                            mnoznik.x0 = tmpRLE.x0;
                            czyDodac = false;
                            continue;
                        }

                        mnoznik.x0 = tmpRLE.x0;
                        mnoznik.x1 = tmpRLE.x1;
                        czyDodac = false;
                    }
                }
                if (czyDodac) {
                    binaryAdd(wiersz, tmpRLE);
                //System.out.println("d:"+tmpRLE);
                } else {
                    czyDodac = true;
                    merge(wiersz);
                }

            } else {
                ArrayList<RLE> nowy = new ArrayList<RLE>();
                nowy.add(tmpRLE);
                ciagi.put(tmpRLE.y, nowy);
            }

        }
    }
}
