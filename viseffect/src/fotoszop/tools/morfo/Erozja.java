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
public class Erozja extends Morfizm {

    ArrayList<Hashtable<Integer, ArrayList<RLE>>> ciagi = new ArrayList<Hashtable<Integer, ArrayList<RLE>>>();

    public Hashtable<Integer, ArrayList<RLE>> getWynik() {
        if (ciagi.isEmpty()) {
            return null;
        }
        if (ciagi.size()==1) {
            return ciagi.get(0);
        }
        Hashtable<Integer, ArrayList<RLE>> res = intersect(ciagi.get(0), ciagi.get(1));
        for(int i = 2; i<ciagi.size();i++){
            res = intersect(res, ciagi.get(i));
        }
        return res;
    }
    ElementStrukturalny el;

    public Erozja(ElementStrukturalny el) {
        this.el = el;
        for (int i = 0; i < el.element.size(); i++) {
            ciagi.add(new Hashtable<Integer, ArrayList<RLE>>());
        }
    }

    public void morfuj(RLE rle) {
        ArrayList<RLE> r = el.eruj(rle);
        boolean czyDodac = true;
        for (int i = 0; i < r.size(); i++) {
            RLE tmpRLE = r.get(i);
            if (tmpRLE == null) {
                continue;
            }
            Hashtable<Integer, ArrayList<RLE>> kolumna = ciagi.get(i);
            ArrayList<RLE> wiersz = kolumna.get(tmpRLE.y);
            if (wiersz == null) {
                wiersz = new ArrayList<RLE>();
                kolumna.put(tmpRLE.y, wiersz);
            }
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
        }
    }

    private Hashtable<Integer, ArrayList<RLE>> intersect(Hashtable<Integer, ArrayList<RLE>> wynik1, Hashtable<Integer, ArrayList<RLE>> wynik2) {
        Hashtable<Integer, ArrayList<RLE>> result = new Hashtable<Integer, ArrayList<RLE>>();
        boolean flaga = false;
        for (Integer key : wynik1.keySet()) {
            ArrayList<RLE> wiersz2 = wynik2.get(key);
            if (wiersz2 == null) {
                continue;
            }
            flaga = false;
            ArrayList<RLE> wierszRes = result.get(key);
            if (wierszRes == null) {
                wierszRes = new ArrayList<RLE>();
                flaga = true;
            }

            ArrayList<RLE> wiersz1 = wynik1.get(key);
            for (RLE rle1 : wiersz1) {
                for (RLE rle2 : wiersz2) {
                    if (rle1.isInsideOf(rle2)) {
                        wierszRes.add(rle1);
                    } else if (rle2.isInsideOf(rle1)) {
                        wierszRes.add(rle2);
                    } else {
                        if (rle1.x0 < rle2.x0 && rle1.x1 > rle2.x0) {
                            wierszRes.add(new RLE(rle2.x0, rle1.x1, key));
                        } else if (rle2.x0 < rle1.x0 && rle2.x1 > rle1.x0) {
                            wierszRes.add(new RLE(rle1.x0, rle2.x1, key));
                        }
                    }
                }
            }

            if (flaga && !wierszRes.isEmpty()) {
                result.put(key, wierszRes);
            }
        }
        return result;
    }
}
