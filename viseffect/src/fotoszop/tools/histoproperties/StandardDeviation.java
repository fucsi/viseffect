package fotoszop.tools.histoproperties;



public class StandardDeviation implements HistogramProperty {

	
	public double calculate(int N,int[] histogram) {
		return java.lang.Math.sqrt(new Variance().calculate(N,histogram));
	}

}
