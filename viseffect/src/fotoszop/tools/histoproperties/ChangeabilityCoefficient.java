package fotoszop.tools.histoproperties;


public class ChangeabilityCoefficient implements HistogramProperty {

	
	public double calculate(int N,int[] histogram) {
		return new StandardDeviation().calculate(N,histogram) / new AverageOfEffort().calculate(N,histogram);
	}

}
