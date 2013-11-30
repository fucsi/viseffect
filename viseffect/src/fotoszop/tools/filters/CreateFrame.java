/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fotoszop.tools.filters;

/**
 *
 * @author kalkan
 */
public class CreateFrame implements Runnable {

    static final public int EXPAND_UP = 1,  EXPAND_DOWN = 2,  EXPAND_LEFT = 3,  EXPAND_RIGHT = 4;
    static int[] original;
    static int[] copy;
    static int m,  n,  w,  h;
    private int mode;
    private int offset,  tmp,  copyIndex,  origIndex;

    public CreateFrame(int[] original, int[] copy, int m, int n, int h, int w, int mode) {
        CreateFrame.original = original;
        CreateFrame.copy = copy;
        CreateFrame.m = m;
        CreateFrame.n = n;
        CreateFrame.h = h;
        CreateFrame.w = w;
        this.mode = mode;
    }

    public CreateFrame(int mode) {
        this.mode = mode;
    }

    public void doTop() {
        //  gorny margines
        for (int i = 0; i < m; ++i) {
            origIndex = (m - i) * w;
            copyIndex = n * ((i << 1) + 1) + i * w;
            for (int j = 0; j < w; ++j) {
                copy[copyIndex] = original[origIndex];
                ++origIndex;
                ++copyIndex;
            }
        }
    }

    public void doBottom() {
        //dolny margines
        offset = (w + (n << 1)) * (h + m);
        for (int i = 0; i < m; ++i) {
            tmp = i * w;
            copyIndex = offset + n * ((i << 1) + 1) + tmp;
            origIndex = original.length - tmp - (w << 1);
            for (int j = 0; j < w; ++j) {
                copy[copyIndex] = original[origIndex];
                ++copyIndex;
                ++origIndex;
            }
        }
    }

    public void doLeft() {
        //lewy bok
        offset = (w + (n << 1));
        for (int i = 0; i < h + (m << 1); ++i) {
            tmp = i * offset;
            for (int j = 0; j < n; ++j) {
                copy[tmp + j] = copy[tmp + j + (n - j << 1)];
            }
        }
    }

    public void doRight() {
        //prawy bok
        offset = (w + (n << 1));
        for (int i = 0; i < h + (m << 1); ++i) {
            tmp = (i + 1) * offset - 1;
            for (int j = 0; j < n; ++j) {
                copy[tmp - j] = copy[tmp - (n << 1) + j];
            }
        }
    }

    public void run() {
        switch (mode) {
            case EXPAND_UP:
                doTop();
                break;
            case EXPAND_DOWN:
                doBottom();
                break;
            case EXPAND_LEFT:
                doLeft();
                break;
            case EXPAND_RIGHT:
                doRight();
                break;
            default:
                break;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
