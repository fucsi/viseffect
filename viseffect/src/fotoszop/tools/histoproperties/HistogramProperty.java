package fotoszop.tools.histoproperties;

import java.awt.image.BufferedImage;

public interface HistogramProperty {

	public double calculate(int N,int[] histogram);
}
