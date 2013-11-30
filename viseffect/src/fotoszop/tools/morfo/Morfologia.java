/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoszop.tools.morfo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author kalkan
 */
public class Morfologia {

    ElementStrukturalny B1;
    ElementStrukturalny B2;


    public Morfologia(){
        
    }
    public Morfologia(int[] element) {
        B1 = new ElementStrukturalny(element);
    }

    public Morfologia(int[] element, int[] element2) {
        B1 = new ElementStrukturalny(element);
        B2 = new ElementStrukturalny(element2);
    }

    public void wariant1(BufferedImage image) {
        Dylatacja dyl = new Dylatacja(B1);
        Hashtable<Integer, ArrayList<RLE>> oryg = new Hashtable<Integer, ArrayList<RLE>>();
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;
        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 1) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 0) {
                            RLE r = new RLE(w, k - 1, h);
                            ArrayList<RLE> orygWiersz = oryg.get(r.y);
                            if (orygWiersz == null) {
                                orygWiersz = new ArrayList<RLE>();
                                oryg.put(r.y, orygWiersz);
                            }
                            orygWiersz.add(r);
                            dyl.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        ArrayList<RLE> orygWiersz = oryg.get(r.y);
                        if (orygWiersz == null) {
                            orygWiersz = new ArrayList<RLE>();
                            oryg.put(r.y, orygWiersz);
                        }
                        orygWiersz.add(r);
                        dyl.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikDylatacji = dyl.getWynik();

        ArrayList<RLE> wynik = roznica(wynikDylatacji, oryg);

        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    protected ArrayList<RLE> roznica(Hashtable<Integer, ArrayList<RLE>> odjemna, Hashtable<Integer, ArrayList<RLE>> odjemnik) {

        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer key : odjemna.keySet()) {
            ArrayList<RLE> wOdjemna = odjemna.get(key);
            ArrayList<RLE> wOdjemnik = odjemnik.get(key);
            if (wOdjemnik == null) {
                wynik.addAll(wOdjemna);
                continue;
            }
            for (int j = 0; j < wOdjemnik.size(); j++) {
                RLE rleOrg = wOdjemnik.get(j);
                for (int i = 0; i < wOdjemna.size(); i++) {
                    RLE rle = wOdjemna.get(i);
                    if (rleOrg.x0 == rle.x0) {
                        if (rleOrg.x1 == rle.x1) {
                            continue;
                        } else if (rleOrg.x1 < rle.x1) {
                            rle.x0 = rleOrg.x1 + 1;
                            continue;
                        }
                    } else if (rleOrg.x1 == rle.x1) {
                        if (rleOrg.x0 > rle.x0) {
                            rle.x1 = rleOrg.x0 - 1;
                            continue;
                        }
                    } else if (rleOrg.x0 > rle.x0 && rleOrg.x1 < rle.x1) {
                        wOdjemna.add(i + 1, new RLE(rleOrg.x1 + 1, rle.x1, rle.y));
                        wOdjemna.add(i + 1, new RLE(rle.x0, rleOrg.x0 - 1, rle.y));
                        wOdjemna.remove(i);
                        i = i + 1;
                        continue;
                    }
                }
            }
            wynik.addAll(wOdjemna);

        }
        return wynik;
    }

    public void wariant2(BufferedImage image) {
        Dylatacja ero = new Dylatacja(B1);
        Hashtable<Integer, ArrayList<RLE>> oryg = new Hashtable<Integer, ArrayList<RLE>>();
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;


        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 0) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 1) {
                            RLE r = new RLE(w, k - 1, h);
                            ArrayList<RLE> orygWiersz = oryg.get(r.y);
                            if (orygWiersz == null) {
                                orygWiersz = new ArrayList<RLE>();
                                oryg.put(r.y, orygWiersz);
                            }
                            orygWiersz.add(r);
                            ero.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        ArrayList<RLE> orygWiersz = oryg.get(r.y);
                        if (orygWiersz == null) {
                            orygWiersz = new ArrayList<RLE>();
                            oryg.put(r.y, orygWiersz);
                        }
                        orygWiersz.add(r);
                        ero.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikErozji = ero.getWynik();

        Hashtable<Integer, ArrayList<RLE>> konwWynikE = flip(wynikErozji, W, H);
        Hashtable<Integer, ArrayList<RLE>> konwWynikA = flip(oryg, W, H);
        ArrayList<RLE> wynik = new ArrayList<RLE>();
        wynik = roznica(konwWynikA, konwWynikE);

        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    public Hashtable<Integer, ArrayList<RLE>> flip(Hashtable<Integer, ArrayList<RLE>> toFlip, int W, int H) {

        Hashtable<Integer, ArrayList<RLE>> result = new Hashtable<Integer, ArrayList<RLE>>();


        for (Integer y = 0; y < H; y++) {
            ArrayList<RLE> wynik = new ArrayList<RLE>();
            wynik.add(new RLE(0, W - 1, y));
            result.put(y, wynik);
        }
        for (Integer integer : toFlip.keySet()) {
            ArrayList<RLE> wynik = new ArrayList<RLE>();
            RLE rle0 = toFlip.get(integer).get(0);
            if (rle0.x0 >= 1) {
                wynik.add(new RLE(0, rle0.x0 - 1, integer));
            }
            for (int i = 1; i < toFlip.get(integer).size(); i++) {
                RLE rle = toFlip.get(integer).get(i);
                wynik.add(new RLE(rle0.x1 + 1, rle.x0 - 1, integer));
                rle0 = rle;
            }
            rle0 = toFlip.get(integer).get(toFlip.get(integer).size() - 1);
            if (rle0.x1 < W - 1) {
                wynik.add(new RLE(rle0.x1 + 1, W - 1, integer));
            }
            result.put(integer, wynik);
        }
        return result;
    }

    public void wariant3(BufferedImage image) {
        Dylatacja ero = new Dylatacja(B1);
        Dylatacja dyl = new Dylatacja(B1);

        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;
        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];

                if (current == 0) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 1) {
                            RLE r = new RLE(w, k - 1, h);
                            ero.morfuj(r);
                            w = k - 1;
                            index = index2 - 1;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        ero.morfuj(r);
                        continue;
                    }
                } else if (current == 1) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 0) {
                            RLE r = new RLE(w, k - 1, h);
                            dyl.morfuj(r);
                            w = k - 1;
                            index = index2 - 1;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        dyl.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikErozji = ero.getWynik();
        Hashtable<Integer, ArrayList<RLE>> konwWynikE = new Hashtable<Integer, ArrayList<RLE>>();
        for (Integer integer : wynikErozji.keySet()) {
            ArrayList<RLE> wynik = new ArrayList<RLE>();
            RLE rle0 = wynikErozji.get(integer).get(0);
            if (rle0.x0 >= 1) {
                wynik.add(new RLE(0, rle0.x0 - 1, integer));
            }
            for (int i = 1; i < wynikErozji.get(integer).size(); i++) {
                RLE rle = wynikErozji.get(integer).get(i);
                wynik.add(new RLE(rle0.x1 + 1, rle.x0 - 1, integer));
                rle0 = rle;
            }
            rle0 = wynikErozji.get(integer).get(wynikErozji.get(integer).size() - 1);
            if (rle0.x1 < W - 1) {
                wynik.add(new RLE(rle0.x1 + 1, W - 1, integer));
            }
            konwWynikE.put(integer, wynik);
        }


        Hashtable<Integer, ArrayList<RLE>> wynikDylatacji = dyl.getWynik();

        ArrayList<RLE> wynik = roznica(wynikDylatacji, konwWynikE);
        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    public void dylatacja(BufferedImage image) {
        Dylatacja dyl = new Dylatacja(B1);
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;
        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 1) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 0) {
                            RLE r = new RLE(w, k - 1, h);
                            dyl.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        dyl.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikDylatacji = dyl.getWynik();

        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : wynikDylatacji.keySet()) {
            wynik.addAll(wynikDylatacji.get(integer));
        }

        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    public void erozja(BufferedImage image) {
        Dylatacja ero = new Dylatacja(B1);
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;


        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 0) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 1) {
                            RLE r = new RLE(w, k - 1, h);
                            ero.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        ero.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikErozji = ero.getWynik();

        Hashtable<Integer, ArrayList<RLE>> konwWynikE = flip(wynikErozji, W, H);
        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : konwWynikE.keySet()) {
            wynik.addAll(konwWynikE.get(integer));
        }

        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }


    

    public void otwarcie(BufferedImage image) {
        Dylatacja ero = new Dylatacja(B1);
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;


        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 0) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 1) {
                            RLE r = new RLE(w, k - 1, h);
                            ero.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        ero.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikErozji = ero.getWynik();

        wynikErozji = flip(wynikErozji, W, H);
        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : wynikErozji.keySet()) {
            wynik.addAll(wynikErozji.get(integer));
        }
        Dylatacja dyl = new Dylatacja(B1);
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE rle = wynik.get(i);
            dyl.morfuj(rle);
        }
        wynikErozji = dyl.getWynik();
        wynik.clear();
        for (Integer integer : wynikErozji.keySet()) {
            wynik.addAll(0, wynikErozji.get(integer));
        }
        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    public void zamkniecie(BufferedImage image) {
        Dylatacja dyl = new Dylatacja(B1);
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;
        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 1) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 0) {
                            RLE r = new RLE(w, k - 1, h);
                            dyl.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        dyl.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikDylatacji = dyl.getWynik();
        wynikDylatacji = flip(wynikDylatacji, W, H);
        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : wynikDylatacji.keySet()) {
            wynik.addAll(0, wynikDylatacji.get(integer));
        }

        Dylatacja ero = new Dylatacja(B1);
        for (RLE rle : wynik) {
            ero.morfuj(rle);
        }
        wynikDylatacji = ero.getWynik();
        wynikDylatacji = flip(wynikDylatacji, W, H);
        wynik.clear();
        for (Integer integer : wynikDylatacji.keySet()) {
            wynik.addAll(0, wynikDylatacji.get(integer));
        }

        int[] newbw = new int[bw.length];
        for (int i = 0; i < wynik.size(); i++) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    public void hitmiss(BufferedImage image) {
        Dylatacja ero1 = new Dylatacja(B1);
        Dylatacja ero2 = new Dylatacja(B2);

        Hashtable<Integer, ArrayList<RLE>> A = new Hashtable<Integer, ArrayList<RLE>>();
        int H = image.getHeight();
        int W = image.getWidth();

        RLE.setSize(H, W);

        byte[] bw = (byte[]) image.getData().getDataElements(0, 0, W, H, null);
        int h = 0, w, k;
        int index = -1;
        int index2;
        byte current = 0;
        boolean czyNieZnalazl = true;


        while (h < H) {
            w = 0;
            while (w < W) {
                current = bw[++index];
                if (current == 1) {
                    k = w + 1;
                    index2 = index + 1;
                    czyNieZnalazl = true;
                    while (k < W) {
                        if (bw[index2] == 0) {
                            RLE r = new RLE(w, k - 1, h);
                            ArrayList<RLE> orygWiersz = A.get(r.y);
                            if (orygWiersz == null) {
                                orygWiersz = new ArrayList<RLE>();
                                A.put(r.y, orygWiersz);
                            }
                            orygWiersz.add(r);
                            ero2.morfuj(r);
                            w = k;
                            index = index2;
                            czyNieZnalazl = false;
                            break;
                        }
                        ++k;
                        ++index2;
                    }
                    if (czyNieZnalazl) {
                        RLE r = new RLE(w, W - 1, h);
                        index += W - 1 - w;
                        w = W;
                        ArrayList<RLE> orygWiersz = A.get(r.y);
                        if (orygWiersz == null) {
                            orygWiersz = new ArrayList<RLE>();
                            A.put(r.y, orygWiersz);
                        }
                        orygWiersz.add(r);
                        ero2.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> notA = flip(A, W, H);
        for (Integer key : notA.keySet()) {
            for (RLE rle : notA.get(key)) {
                ero1.morfuj(rle);
            }
        }
        Hashtable<Integer, ArrayList<RLE>> wynik1 = ero1.getWynik();
        wynik1 = flip(wynik1, W, H);

        Hashtable<Integer, ArrayList<RLE>> wynik2 = ero2.getWynik();

        wynik2 = flip(wynik2, W, H);


        wynik1 = intersect(wynik1,wynik2);

        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : wynik1.keySet()) {
            wynik.addAll(wynik1.get(integer));
        }

        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    protected Hashtable<Integer, ArrayList<RLE>> intersect(Hashtable<Integer, ArrayList<RLE>> wynik1, Hashtable<Integer, ArrayList<RLE>> wynik2) {
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
