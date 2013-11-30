package fotoszop.tools.histoproperties;

public class ScrunchCoefficient implements HistogramProperty {

    public double calculate(int N, int[] histogram) {
        double sd = new StandardDeviation().calculate(N, histogram);
        sd = sd * sd;
        sd *= sd;//sd^4;
        double avarageOfEffort = new AverageOfEffort().calculate(N, histogram);
        double scrunch = 0;
        double tmp;
        for (int i = 0; i < histogram.length; i++) {
            tmp = i - avarageOfEffort;
            tmp *= tmp;
            scrunch += tmp * tmp * histogram[i] - 3;
        }
        return (1.0 / (sd * N) * scrunch);
    }
}
