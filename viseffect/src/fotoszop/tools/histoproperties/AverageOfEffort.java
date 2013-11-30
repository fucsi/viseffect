package fotoszop.tools.histoproperties;

public class AverageOfEffort implements HistogramProperty {

	public double calculate(int N,int[] histogram) {
		int average = 0;
		for(int i = 0; i < histogram.length ; i++ ){
			average += i * histogram[i];
		}
		return ((double) average / (double)(N));
	}

}
