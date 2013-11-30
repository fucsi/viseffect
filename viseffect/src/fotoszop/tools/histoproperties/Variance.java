package fotoszop.tools.histoproperties;



public class Variance implements HistogramProperty{

	
	public double calculate(int N,int[] histogram) {
		double averageOfEffort = new AverageOfEffort().calculate(N,histogram);
		int variance = 0;
        double tmp;
		for(int i = 0; i < histogram.length ; i++ ){
            tmp = i - averageOfEffort;
			variance += tmp*tmp * histogram[i];
		}
		return (double) variance / (double)(N) ;
	}

}
