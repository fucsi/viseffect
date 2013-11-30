package fotoszop.tools.histoproperties;

public class Entrophy implements HistogramProperty {
	
	public double calculate(int N,int[] histogram) {
		double entro = 0;
		double size = (double)(N);
        final double div = Math.log10(2.0);
		for (int i = 0; i < histogram.length; i++) {
            if (histogram[i]==0) {
                continue;
            }
			entro += histogram[i] * Math.log10(histogram[i] / size) / div;
		}
		return (-entro / size);
	}

}
