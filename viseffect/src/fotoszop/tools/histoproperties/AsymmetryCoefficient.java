package fotoszop.tools.histoproperties;

public class AsymmetryCoefficient implements HistogramProperty {

    public double calculate(int N, int[] histogram) {
        double sd = new StandardDeviation().calculate(N, histogram);
        double avarageOfEffort = new AverageOfEffort().calculate(N, histogram);
        double asymmetry = 0;
        double tmp;
        for (int i = 0; i < histogram.length; i++) {
            tmp = (i - avarageOfEffort);
            asymmetry += tmp * tmp * tmp * histogram[i];
        }
        return (1.0 / (sd * sd * sd * N) * asymmetry);
    }
}
