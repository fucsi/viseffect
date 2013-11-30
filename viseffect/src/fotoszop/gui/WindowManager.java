package fotoszop.gui;

import fotoszop.tools.Histo;
import fotoszop.tools.Separator;
import fotoszop.tools.basic.Brightness;
import fotoszop.tools.basic.Contrast;
import fotoszop.tools.basic.Mirror;
import fotoszop.tools.basic.Negative;
import fotoszop.tools.ToolInterface;
import fotoszop.tools.basic.Scale;
import fotoszop.tools.basic.Zoom;
import fotoszop.tools.errorcomputation.MaxDiff;
import fotoszop.tools.errorcomputation.MeanSquare;
import fotoszop.tools.errorcomputation.PeakMeanSquare;
import fotoszop.tools.errorcomputation.PeakSignal2Noise;
import fotoszop.tools.errorcomputation.Signal2Noise;
import fotoszop.tools.filters.Splot;
import fotoszop.tools.filters.SplotKirsz;
import fotoszop.tools.filters.SplotO1;
import fotoszop.tools.filters.SplotO2;
import fotoszop.tools.filters.SplotOgolny;
import fotoszop.tools.filters.alfa.AlphaTrimmedFilter;
import fotoszop.tools.filters.contra.ContrHarmonic;

//import fotoszop.tools.fourier.userinterface.FFTGUI;
//import fotoszop.tools.fourier.userinterface.FiltrGornoprzepustowyKrawedzie;
//import fotoszop.tools.fourier.userinterface.FiltrPasmowyZaporowy;
//import fotoszop.tools.fourier.userinterface.FiltrPasmowyPrzepustowy;
//import fotoszop.tools.fourier.userinterface.FIltrGornoPrzepustowy;
//import fotoszop.tools.fourier.userinterface.FIltrDolnoPrzepustowy;
//import fotoszop.tools.fourier.userinterface.FiltrModyfikujacyFaze;
import fotoszop.tools.morfo.gui.InterfejsDylatacja;
import fotoszop.tools.morfo.gui.InterfejsErozja;
import fotoszop.tools.morfo.gui.InterfejsHMT;
import fotoszop.tools.morfo.gui.InterfejsMorfologia;
import fotoszop.tools.morfo.gui.InterfejsOtwarcie;
import fotoszop.tools.morfo.gui.InterfejsZamkniecie;
import fotoszop.tools.segmentacja.interfaces.InterfejsSegmentacja;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * 
 * @author kalkan
 */
public class WindowManager implements InternalFrameListener {

	static private WindowManager instance = null;
	static private JFileChooser fc = new JFileChooser();
	private JDesktopPane desktopPane = null;
	private List<MyInternalFrame> frames = new ArrayList<MyInternalFrame>();
	private HashMap<String, List<ToolInterface>> toolSets = new HashMap<String, List<ToolInterface>>();

	public HashMap<String, List<ToolInterface>> getToolSets() {
		return toolSets;
	}

	public void setToolSets(HashMap<String, List<ToolInterface>> toolSets) {
		this.toolSets = toolSets;
	}

	private WindowManager() {
		List<ToolInterface> filters = new ArrayList<ToolInterface>();
		List<ToolInterface> tools = new ArrayList<ToolInterface>();
		List<ToolInterface> errorStatistics = new ArrayList<ToolInterface>();
		List<ToolInterface> morfologia = new ArrayList<ToolInterface>();
		List<ToolInterface> fourier = new ArrayList<ToolInterface>();

		tools.add(new Brightness());
		tools.add(new Negative());
		tools.add(new Mirror());
		tools.add(new Contrast());
		tools.add(new Separator());
		tools.add(new Zoom());
		tools.add(new Scale());
		tools.add(new Separator());
		tools.add(new Histo());
		tools.add(new InterfejsSegmentacja());

		morfologia.add(new InterfejsMorfologia());
		morfologia.add(new Separator());
		morfologia.add(new InterfejsDylatacja());
		morfologia.add(new InterfejsErozja());
		morfologia.add(new Separator());
		morfologia.add(new InterfejsOtwarcie());
		morfologia.add(new InterfejsZamkniecie());
		morfologia.add(new Separator());
		morfologia.add(new InterfejsHMT());

		errorStatistics.add(new MeanSquare());
		errorStatistics.add(new PeakMeanSquare());
		errorStatistics.add(new Separator());
		errorStatistics.add(new Signal2Noise());
		errorStatistics.add(new PeakSignal2Noise());
		errorStatistics.add(new Separator());
		errorStatistics.add(new MaxDiff());

		filters.add(new AlphaTrimmedFilter());
		filters.add(new ContrHarmonic());
		filters.add(new Separator());
		filters.add(new Splot());
		filters.add(new SplotO1());
		filters.add(new SplotO2());
		filters.add(new SplotOgolny());
		filters.add(new Separator());
		filters.add(new SplotKirsz());

		toolSets.put("Obliczanie bledow", errorStatistics);
		toolSets.put("Filtry", filters);
		toolSets.put("Morfologia", morfologia);
		toolSets.put("Podstawowe narzedzia", tools);
		
	}

	/**
	 * 
	 * @return
	 */
	public List<JMenu> getMenus() {
		List<JMenu> result = new ArrayList<JMenu>();
		Set<String> keys = toolSets.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String string = it.next();
			JMenu menu = new JMenu(string);
			List<ToolInterface> tools = toolSets.get(string);
			for (int i = 0; i < tools.size(); i++) {
				ToolInterface toolInterface = tools.get(i);
				menu.add(toolInterface.getJMenuItem());
			}
			result.add(menu);
		}
		return result;
	}

	static public WindowManager getInstance() {
		if (instance == null) {
			instance = new WindowManager();
		}
		return instance;
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public List<MyInternalFrame> getFrames() {
		return frames;
	}

	public void setDesktopPane(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public void openImage() {
		int returnVal = fc.showOpenDialog(desktopPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			MyInternalFrame frame = new MyInternalFrame(file.getPath());
			frame.addInternalFrameListener(this);
			frames.add(frame);
			desktopPane.add(frame);
			frame.show();
		}
	}

	public void saveImage() {
		MyInternalFrame selected = null;
		for (MyInternalFrame myInternalFrame : frames) {
			if (myInternalFrame.isSelected()) {
				selected = myInternalFrame;
				break;
			}
		}
		if (selected == null) {
			JOptionPane.showMessageDialog(desktopPane,
					"Zazancz jakiekolwiek okno z obrazkiem");
			return;
		}
		int returnVal = fc.showSaveDialog(desktopPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			BufferedImage image = selected.getPicture().getImage();
			try {
				ImageIO.write(image, "jpg", file);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(desktopPane,
						"Blad podczas zapisywania");
				return;
			}
		}
	}
	
	public void saveImageAs() {
		MyInternalFrame selected = null;
		for (MyInternalFrame myInternalFrame : frames) {
			if (myInternalFrame.isSelected()) {
				selected = myInternalFrame;
				break;
			}
		}
		if (selected == null) {
			JOptionPane.showMessageDialog(desktopPane,
					"Zazancz jakiekolwiek okno z obrazkiem");
			return;
		}
		int returnVal = fc.showSaveDialog(desktopPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			BufferedImage image = selected.getPicture().getImage();
			try {
				ImageIO.write(image, "jpg", file);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(desktopPane,
						"Blad podczas zapisywania");
				return;
			}
		}
	}

	public void internalFrameOpened(InternalFrameEvent e) {

	}

	public void internalFrameClosing(InternalFrameEvent e) {
		frames.remove(e.getInternalFrame());
	}

	public void internalFrameClosed(InternalFrameEvent e) {

	}

	public void internalFrameIconified(InternalFrameEvent e) {

	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameActivated(InternalFrameEvent e) {
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
	}
}
