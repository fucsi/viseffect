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
public class MorfologiaNowa extends Morfologia{

    public MorfologiaNowa(int[] element, int[] element2) {
        super(element, element2);
    }

    public MorfologiaNowa(int[] element) {
        super(element);
    }

    @Override
    public void erozja(BufferedImage image) {
        Erozja ero = new Erozja(B1);
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

        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : wynikErozji.keySet()) {
            wynik.addAll(wynikErozji.get(integer));
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

    @Override
    public void hitmiss(BufferedImage image) {
        Erozja ero1 = new Erozja(B1);
        Erozja ero2 = new Erozja(B2);

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
                            ero2.morfuj(r);
                            ero1.morfuj(r);
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
                        ero1.morfuj(r);
                        ero2.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynik1 = ero1.getWynik();
        Hashtable<Integer, ArrayList<RLE>> wynik2 = ero2.getWynik();
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

    @Override
    public void otwarcie(BufferedImage image) {
        Erozja ero = new Erozja(B1);
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

    @Override
    public void wariant2(BufferedImage image) {
        Erozja ero = new Erozja(B1);
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

        ArrayList<RLE> wynik = roznica(oryg, wynikErozji);

        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    @Override
    public void wariant3(BufferedImage image) {
        Erozja ero = new Erozja(B1);
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
                            ero.morfuj(r);
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
                        ero.morfuj(r);
                        dyl.morfuj(r);
                        continue;
                    }
                }
                ++w;
            }
            ++h;
        }

        Hashtable<Integer, ArrayList<RLE>> wynikErozji = ero.getWynik();
        Hashtable<Integer, ArrayList<RLE>> wynikDylat = dyl.getWynik();
       

        ArrayList<RLE> wynik = roznica(wynikDylat, wynikErozji);
        int[] newbw = new int[bw.length];
        for (int i = wynik.size() - 1; i >= 0; i--) {
            RLE tmp = wynik.get(i);
            for (k = tmp.x0 + tmp.y * W; k <= tmp.x1 + tmp.y * W; k++) {
                newbw[k] = 0xFFFFFF;
            }
        }
        image.setRGB(0, 0, W, H, newbw, 0, W);
    }

    @Override
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
        ArrayList<RLE> wynik = new ArrayList<RLE>();
        for (Integer integer : wynikDylatacji.keySet()) {
            wynik.addAll(0, wynikDylatacji.get(integer));
        }

        Erozja ero = new Erozja(B1);
        for (RLE rle : wynik) {
            ero.morfuj(rle);
        }
        wynikDylatacji = ero.getWynik();
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


}
