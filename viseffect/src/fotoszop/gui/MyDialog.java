package fotoszop.gui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Kamil
 */
public class MyDialog extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 723801754834630565L;
	private boolean showWindows = false;
    private int howManyWindowSelectors = 1;
    private JPanel windowsPane;

    public void setHowManyWindowSelectors(int howManyWindowSelectors) {
        this.howManyWindowSelectors = howManyWindowSelectors;
    }
    public JOptionPane optionPane;
    private List<JList> windowSelectors = new ArrayList<JList>();
    public List<JSlider> sliders = new ArrayList<JSlider>();
    public List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
    public List<JTextField> textFields = new ArrayList<JTextField>();
    public List<JTextArea> textAreas = new ArrayList<JTextArea>();
    public String btnString1 = "OK",  btnString2 = "Cancel";

    public List<MyInternalFrame> getSelectedWindows() {
        List<MyInternalFrame> resultList = new ArrayList<MyInternalFrame>();
        List<MyInternalFrame> frames = WindowManager.getInstance().getFrames();
        for (int j = 0; j < windowSelectors.size(); ++j) {
            int[] selected = windowSelectors.get(j).getSelectedIndices();
            for (int i = 0; i < selected.length; i++) {
                try {
                    int g = selected[i];
                    resultList.add(frames.get(g));
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        return resultList;
    }

    public void reset() {
        for (JSlider jSlider : sliders) {
            jSlider.setValue(0);
        }
    }

    public boolean isShowWindows() {
        return showWindows;
    }

    public void setShowWindows(boolean showWindows) {
        this.showWindows = showWindows;
    }

    public void addIntegerSlider(int min, int max, int zero, ChangeListener listener) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, zero);
        slider.setToolTipText(0 + "");
        slider.setMajorTickSpacing((max - min) / 10);
        slider.setMinorTickSpacing((max - min) / 50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        if (listener != null) {
            slider.addChangeListener(listener);
        }
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    slider.setToolTipText(slider.getValue() + "");
                }
            }
        });

        this.getContentPane().add(slider, this.getContentPane().getComponentCount() - 1);
        this.sliders.add(slider);

    }

    public void addCheckbox(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        this.getContentPane().add(checkbox, this.getContentPane().getComponentCount() - 1);
        this.checkboxes.add(checkbox);
    }

    public void addTextField(String text,boolean editable) {
        JTextField textField = new JTextField(text);
        this.getContentPane().add(textField, this.getContentPane().getComponentCount() - 1);
        textField.setEditable(editable);
        this.textFields.add(textField);
    }
    
    public void addTextArea(String text,boolean editable) {
        JTextArea textArea = new JTextArea(text);
        this.getContentPane().add(textArea, this.getContentPane().getComponentCount() - 1);
        textArea.setEditable(editable);
        textArea.setAutoscrolls(true);
        textArea.setSize(400, 500);
        this.textAreas.add(textArea);
    }

    private void addWindowSelector() {
        List<MyInternalFrame> frames = WindowManager.getInstance().getFrames();
        int index = frames.size();
        for (int i = 0; i < frames.size(); i++) {
            if (frames.get(i).isSelected()) {
                index = i;
                break;
            }
        }
        if (windowSelectors.size() == 0) {
            DefaultListModel model = new DefaultListModel();
            for (MyInternalFrame myInternalFrame : frames) {
                model.addElement(myInternalFrame.getTitle());
            }
            int selectionModel;
            if (howManyWindowSelectors == 1) {
                selectionModel = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
            } else {
                selectionModel = ListSelectionModel.SINGLE_SELECTION;
            }
            for (int it = 0; it < howManyWindowSelectors; ++it) {
                JList windowSelector = new JList(model);
                windowSelector.setSelectionMode(selectionModel);
                windowSelector.setLayoutOrientation(JList.VERTICAL);
                //windowSelector.setVisibleRowCount(-1);
                windowSelector.setSelectedIndex(index);
                
                windowSelectors.add(windowSelector);
                windowsPane.add(new JScrollPane(windowSelector));
            //optionPane.add(new JScrollPane(windowSelector),0);
            }
        } else {
            for (int it = 0; it < windowSelectors.size(); ++it) {
                DefaultListModel model = (DefaultListModel) windowSelectors.get(it).getModel();
                model.removeAllElements();
                for (MyInternalFrame myInternalFrame : frames) {
                    model.addElement(myInternalFrame.getTitle());
                }
                windowSelectors.get(it).setSelectedIndex(index);
            }
        }
    }

    public MyDialog(String title, String message, PropertyChangeListener listener) {
        super((Frame) null, true);

        setTitle(title);
        windowsPane = new JPanel();
        windowsPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        Object[] array = {new JScrollPane(windowsPane), message};

        Object[] options = {btnString1, btnString2};
        optionPane = new JOptionPane(array,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        this.setSize(900, 300);
        this.setLocationByPlatform(true);
//        setDefaultCloseOperation(MyDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });
        optionPane.addPropertyChangeListener(listener);
    }

    @Override
    public void setVisible(boolean b) {
        if (showWindows && b) {
            this.addWindowSelector();
        }
        super.setVisible(b);
    }
}
