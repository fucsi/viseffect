package fotoszop.tools.histoproperties;

public class ChangeabilityCoeff2 implements HistogramProperty {

    public double calculate(int N, int[] histogram) {
        double coeff = 0;
        double NN = (double)N * N;
        for (int i = 0; i < histogram.length; i++) {
            coeff += histogram[i] * histogram[i];
        }
        return coeff / NN;
    }
}
