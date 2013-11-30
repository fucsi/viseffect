package fotoszop.gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author KAMIL L
 */
public class ArrayDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	public JOptionPane optionPane;
    public JTextField[] txtFields = new JTextField[9];

    public ArrayDialog(String title, String message, PropertyChangeListener listener) {
        super((Frame) null, true);

        setTitle(title);
        JPanel windowsPane = new JPanel();
        windowsPane.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < txtFields.length; i++) {
            windowsPane.add(txtFields[i] = new JTextField("0.111"));
        }
        Object[] array = {message, windowsPane};

        Object[] options = {"OK"};
        optionPane = new JOptionPane(array,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        this.setSize(300, 200);
        this.setLocationByPlatform(true);
        setDefaultCloseOperation(ArrayDialog.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });
        optionPane.addPropertyChangeListener(listener);
    }

    public ArrayDialog(String title, String message, PropertyChangeListener listener, int beg) {
        super((Frame) null, true);

        setTitle(title);
        JPanel windowsPane = new JPanel();
        windowsPane.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < txtFields.length; i++) {
            windowsPane.add(txtFields[i] = new JTextField(beg + ""));
        }
        Object[] array = {message, windowsPane};

        Object[] options = {"OK"};
        optionPane = new JOptionPane(array,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        this.setSize(300, 200);
        this.setLocationByPlatform(true);
        setDefaultCloseOperation(ArrayDialog.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });
        optionPane.addPropertyChangeListener(listener);
    }

    
}
