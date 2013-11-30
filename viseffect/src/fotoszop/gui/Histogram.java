package fotoszop.gui;

import fotoszop.tools.histoproperties.AsymmetryCoefficient;
import fotoszop.tools.histoproperties.AverageOfEffort;
import fotoszop.tools.histoproperties.ChangeabilityCoeff2;
import fotoszop.tools.histoproperties.ChangeabilityCoefficient;
import fotoszop.tools.histoproperties.Entrophy;
import fotoszop.tools.histoproperties.ScrunchCoefficient;
import fotoszop.tools.histoproperties.StandardDeviation;
import fotoszop.tools.histoproperties.Variance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * @author KAMIL L.
 */
public class Histogram extends JDialog implements ActionListener,
		FocusListener, ItemListener {

	private static final long serialVersionUID = -5344600423904123775L;
	private JButton exit;
	private JButton histFcn;
	private JTextField txt1;
	public String property1 = "Histogram obrazu";
	private JFreeChart chartb;
	public int[] brightness;
	public int[] rgb;
	public int[] red;
	public int[] green;
	public int[] blue;
	public BufferedImage image;
	public int g0;
	public double alfa;
	private XYSeries bseria;
	private XYSeries seria;
	private XYSeries rseria;
	private XYSeries gseria;
	private JComboBox combo;
	private Range yrange;
	public boolean isGray;
	private ChartPanel chartPanel;
	private JCheckBox optyCheckBox;
	private JLabel[] jLabelAssymetryCoeff = new JLabel[4],
			jLabelAverageOfEffort = new JLabel[4],
			jLabelChangeanilityCoeff2 = new JLabel[4],
			jLabelChangeabilityCoeff = new JLabel[4],
			jLabelEnthropy = new JLabel[4], jLabelScrunchCoeff = new JLabel[4],
			jLabelStdDev = new JLabel[4], jLabelVariance = new JLabel[4];

	public Histogram(BufferedImage image) {
		this.setContentPane(new JPanel(new BorderLayout()));
		exit = new JButton("Wyjscie");
		exit.addActionListener(this);
		histFcn = new JButton("Popraw");
		histFcn.addActionListener(this);
		txt1 = new JTextField("0");
		txt1.setPreferredSize(new Dimension(40, 25));
		txt1.addFocusListener(this);

		this.setModal(true);
		Object[] items = { "Jasnosc", "Czerwony", "Zielony", "Niebieski", "Wszystkie" };
		combo = new JComboBox(items);
		combo.setSelectedIndex(4);
		combo.addItemListener(this);
		optyCheckBox = new JCheckBox("Bez rozdzielania", false);
		optyCheckBox.addActionListener(this);
		JPanel panel = new JPanel();

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		int rpx, bpx, gpx;
		this.image = image;
		rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0,
				image.getWidth());
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
				|| image.getType() == BufferedImage.TYPE_USHORT_GRAY) {
			System.out.println("Szary");
			isGray = true;
			brightness = new int[256];
			for (int i = 0; i < rgb.length; i++) {
				brightness[rgb[i] & 0xFF]++;
			}
			red = green = blue = brightness;
		} else {
			isGray = false;
			System.out.println("Inny niz szary");
			brightness = new int[256];
			red = new int[256];
			green = new int[256];
			blue = new int[256];
			for (int i = 0; i < rgb.length; i++) {
				rpx = (rgb[i] >>> 16) & 0xFF;
				bpx = rgb[i] & 0xFF;
				gpx = (rgb[i] >>> 8) & 0xFF;
				red[rpx]++;
				blue[bpx]++;
				green[gpx]++;
				brightness[(int) Math.round((rpx + bpx + gpx) / 3.0)]++;
			}
		}

		seria = new XYSeries("Jasnosc");
		rseria = new XYSeries("Czerwony");
		gseria = new XYSeries("Zielony");
		bseria = new XYSeries("Niebieski");

		for (int i = 0; i < red.length; i++) {
			seria.add(i, (double) brightness[i] / rgb.length * 100);
			rseria.add(i, (double) red[i] / rgb.length * 100);
			bseria.add(i, (double) blue[i] / rgb.length * 100);
			gseria.add(i, (double) green[i] / rgb.length * 100);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(seria);
		dataset.addSeries(rseria);
		dataset.addSeries(gseria);
		dataset.addSeries(bseria);

		chartb = ChartFactory.createXYAreaChart("Histogram", "Jasnosc",
				"Czestosc", dataset, PlotOrientation.VERTICAL, true, true,
				false);
		chartb.getXYPlot().setForegroundAlpha(0.5f);
		chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.white);
		chartb.getXYPlot().getRenderer().setSeriesPaint(1, Color.red);
		chartb.getXYPlot().getRenderer().setSeriesPaint(2, Color.green);
		chartb.getXYPlot().getRenderer().setSeriesPaint(3, Color.BLUE);
		yrange = chartb.getXYPlot().getRangeAxis().getRange();

		this.setSize(930, 600);
		chartPanel = new ChartPanel(chartb);
		panel.add(chartPanel);
		panel.add(histFcn);
		panel.add(txt1);
		panel.add(optyCheckBox);

		this.getContentPane().add(exit, BorderLayout.SOUTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.getContentPane().add(combo, BorderLayout.NORTH);

		for (int i = 0; i < jLabelAssymetryCoeff.length; i++) {
			jLabelAssymetryCoeff[i] = new JLabel();
		}

		for (int i = 0; i < jLabelAverageOfEffort.length; i++) {
			jLabelAverageOfEffort[i] = new JLabel();
		}

		for (int i = 0; i < jLabelChangeabilityCoeff.length; i++) {
			jLabelChangeabilityCoeff[i] = new JLabel();
		}

		for (int i = 0; i < jLabelChangeanilityCoeff2.length; i++) {
			jLabelChangeanilityCoeff2[i] = new JLabel();
		}

		for (int i = 0; i < jLabelEnthropy.length; i++) {
			jLabelEnthropy[i] = new JLabel();
		}

		for (int i = 0; i < jLabelScrunchCoeff.length; i++) {
			jLabelScrunchCoeff[i] = new JLabel();
		}

		for (int i = 0; i < jLabelStdDev.length; i++) {
			jLabelStdDev[i] = new JLabel();
		}

		for (int i = 0; i < jLabelVariance.length; i++) {
			jLabelVariance[i] = new JLabel();
		}

		updateProperties();

		JPanel props = new JPanel(new GridLayout(32, 2));

		// <editor-fold desc="setting labels">
		props.add(new JLabel("Assymetry Coeff:"));
		props.add(jLabelAssymetryCoeff[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelAssymetryCoeff[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelAssymetryCoeff[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelAssymetryCoeff[3]);

		props.add(new JLabel("Avg of Effort:"));
		props.add(jLabelAverageOfEffort[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelAverageOfEffort[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelAverageOfEffort[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelAverageOfEffort[3]);

		props.add(new JLabel("Changeability Coeff:"));
		props.add(jLabelChangeabilityCoeff[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelChangeabilityCoeff[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelChangeabilityCoeff[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelChangeabilityCoeff[3]);

		props.add(new JLabel("Changeability Coeff 2:"));
		props.add(jLabelChangeanilityCoeff2[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelChangeanilityCoeff2[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelChangeanilityCoeff2[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelChangeanilityCoeff2[3]);

		props.add(new JLabel("Enthropy:"));
		props.add(jLabelEnthropy[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelEnthropy[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelEnthropy[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelEnthropy[3]);

		props.add(new JLabel("Scrunch Coeff:"));
		props.add(jLabelScrunchCoeff[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelScrunchCoeff[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelScrunchCoeff[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelScrunchCoeff[3]);

		props.add(new JLabel("Std dev:"));
		props.add(jLabelStdDev[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelStdDev[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelStdDev[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelStdDev[3]);

		props.add(new JLabel("Variance:"));
		props.add(jLabelVariance[0]);
		props.add(new JLabel("R:"));
		props.add(jLabelVariance[1]);
		props.add(new JLabel("G:"));
		props.add(jLabelVariance[2]);
		props.add(new JLabel("B:"));
		props.add(jLabelVariance[3]);
		// </editor-fold>

		this.getContentPane().add(new JScrollPane(props), BorderLayout.EAST);
	}

	private void updateProperties() {
		int N = image.getWidth() * image.getHeight();
		jLabelVariance[0].setText("" + new Variance().calculate(N, brightness));
		jLabelVariance[1].setText("" + new Variance().calculate(N, red));
		jLabelVariance[2].setText("" + new Variance().calculate(N, green));
		jLabelVariance[3].setText("" + new Variance().calculate(N, blue));

		jLabelStdDev[0].setText(""
				+ new StandardDeviation().calculate(N, brightness));
		jLabelStdDev[1].setText("" + new StandardDeviation().calculate(N, red));
		jLabelStdDev[2].setText(""
				+ new StandardDeviation().calculate(N, green));
		jLabelStdDev[3]
				.setText("" + new StandardDeviation().calculate(N, blue));

		jLabelScrunchCoeff[0].setText(""
				+ new ScrunchCoefficient().calculate(N, brightness));
		jLabelScrunchCoeff[1].setText(""
				+ new ScrunchCoefficient().calculate(N, red));
		jLabelScrunchCoeff[2].setText(""
				+ new ScrunchCoefficient().calculate(N, green));
		jLabelScrunchCoeff[3].setText(""
				+ new ScrunchCoefficient().calculate(N, blue));

		jLabelEnthropy[0].setText("" + new Entrophy().calculate(N, brightness));
		jLabelEnthropy[1].setText("" + new Entrophy().calculate(N, red));
		jLabelEnthropy[2].setText("" + new Entrophy().calculate(N, green));
		jLabelEnthropy[3].setText("" + new Entrophy().calculate(N, blue));

		jLabelChangeanilityCoeff2[0].setText(""
				+ new ChangeabilityCoeff2().calculate(N, brightness));
		jLabelChangeanilityCoeff2[1].setText(""
				+ new ChangeabilityCoeff2().calculate(N, red));
		jLabelChangeanilityCoeff2[2].setText(""
				+ new ChangeabilityCoeff2().calculate(N, green));
		jLabelChangeanilityCoeff2[3].setText(""
				+ new ChangeabilityCoeff2().calculate(N, blue));

		jLabelChangeabilityCoeff[0].setText(""
				+ new ChangeabilityCoefficient().calculate(N, brightness));
		jLabelChangeabilityCoeff[1].setText(""
				+ new ChangeabilityCoefficient().calculate(N, red));
		jLabelChangeabilityCoeff[2].setText(""
				+ new ChangeabilityCoefficient().calculate(N, green));
		jLabelChangeabilityCoeff[3].setText(""
				+ new ChangeabilityCoefficient().calculate(N, blue));

		jLabelAverageOfEffort[0].setText(""
				+ new AverageOfEffort().calculate(N, brightness));
		jLabelAverageOfEffort[1].setText(""
				+ new AverageOfEffort().calculate(N, red));
		jLabelAverageOfEffort[2].setText(""
				+ new AverageOfEffort().calculate(N, green));
		jLabelAverageOfEffort[3].setText(""
				+ new AverageOfEffort().calculate(N, blue));

		jLabelAssymetryCoeff[0].setText(""
				+ new AsymmetryCoefficient().calculate(N, brightness));
		jLabelAssymetryCoeff[1].setText(""
				+ new AsymmetryCoefficient().calculate(N, red));
		jLabelAssymetryCoeff[2].setText(""
				+ new AsymmetryCoefficient().calculate(N, green));
		jLabelAssymetryCoeff[3].setText(""
				+ new AsymmetryCoefficient().calculate(N, blue));
	}

	public void changeDisplayChannel(int channel) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		switch (channel) {
		case 0:
			dataset.addSeries(seria);
			break;
		case 1:
			dataset.addSeries(rseria);
			break;
		case 2:
			dataset.addSeries(gseria);
			break;
		case 3:
			dataset.addSeries(bseria);
			break;
		case 4:
			dataset.addSeries(seria);
			dataset.addSeries(rseria);
			dataset.addSeries(gseria);
			dataset.addSeries(bseria);
			break;
		}
		chartb = ChartFactory.createXYAreaChart("Histogram", "Jasnosc",
				"Czestosc", dataset, PlotOrientation.VERTICAL, true, true,
				false);
		chartb.getXYPlot().setForegroundAlpha(0.5f);
		chartb.getXYPlot().getRangeAxis().setRange(yrange);
		switch (channel) {
		case 0:
			chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLACK);
			break;
		case 1:
			chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.red);
			break;
		case 2:
			chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.green);
			break;
		case 3:
			chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.BLUE);
			break;
		case 4:
			chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.white);
			chartb.getXYPlot().getRenderer().setSeriesPaint(1, Color.red);
			chartb.getXYPlot().getRenderer().setSeriesPaint(2, Color.green);
			chartb.getXYPlot().getRenderer().setSeriesPaint(3, Color.BLUE);
			break;
		}

		chartPanel.setChart(chartb);
	}

	public void updateHistogram() {

		if (isGray) {

			brightness = new int[256];
			for (int i = 0; i < rgb.length; i++) {
				brightness[rgb[i] & 0xFF]++;
			}
			red = green = blue = brightness;
		} else {
			int rpx, bpx, gpx;
			brightness = new int[256];
			red = new int[256];
			blue = new int[256];
			green = new int[256];
			for (int i = 0; i < rgb.length; i++) {
				rpx = (rgb[i] >>> 16) & 0xFF;
				bpx = rgb[i] & 0xFF;
				gpx = (rgb[i] >>> 8) & 0xFF;
				red[rpx]++;
				blue[bpx]++;
				green[gpx]++;
				brightness[(int) Math.round((rpx + bpx + gpx) / 3.0)]++;
			}
		}

		seria = new XYSeries("Jasnosc");
		rseria = new XYSeries("Czerwony");
		gseria = new XYSeries("Zielony");
		bseria = new XYSeries("Niebieski");

		for (int i = 0; i < red.length; i++) {
			seria.add(i, (double) brightness[i] / rgb.length * 100);
			rseria.add(i, (double) red[i] / rgb.length * 100);
			bseria.add(i, (double) blue[i] / rgb.length * 100);
			gseria.add(i, (double) green[i] / rgb.length * 100);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(seria);
		dataset.addSeries(rseria);
		dataset.addSeries(gseria);
		dataset.addSeries(bseria);

		chartb = ChartFactory.createXYAreaChart("Histogram", "Jasnosc",
				"Czestosc", dataset, PlotOrientation.VERTICAL, true, true,
				false);
		chartb.getXYPlot().setForegroundAlpha(0.5f);
		chartb.getXYPlot().getRenderer().setSeriesPaint(0, Color.white);
		chartb.getXYPlot().getRenderer().setSeriesPaint(1, Color.red);
		chartb.getXYPlot().getRenderer().setSeriesPaint(2, Color.green);
		chartb.getXYPlot().getRenderer().setSeriesPaint(3, Color.BLUE);
		yrange = chartb.getXYPlot().getRangeAxis().getRange();
		combo.setSelectedIndex(4);
		updateProperties();
		chartPanel.setChart(chartb);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exit) {
			this.dispose();
		} else if (e.getSource() == histFcn) {
			firePropertyChange(property1, 0, optyCheckBox.isSelected() ? 1 : 2);
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent evt) {
		JTextField txt = (JTextField) evt.getSource();
		if (txt == txt1) {
			int tmp = 0;
			try {
				tmp = Integer.parseInt(txt.getText());
			} catch (NumberFormatException e) {
				txt.requestFocus();
			}
			if (tmp < 0.0 || tmp > 255) {
				txt.requestFocus();
				return;
			}
			this.g0 = tmp;
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == combo) {
			this.changeDisplayChannel(combo.getSelectedIndex());
		}
	}
}
