package fotoszop.gui;

import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author KAMIL L.
 */
public class FF extends JDialog {

	private static final long serialVersionUID = 2358955008671968943L;
	static Object[] args;
    static int type;

    static public JPanel getLowpass(BufferedImage image) {
        JPanel root = new JPanel();
        root.add(new JLabel("Ograniczenie"));
        JTextField text = new JTextField("40");
        root.add(text);
        args = new Object[]{text};
        return root;
    }

    static public JPanel getHighpass(BufferedImage image) {
        JPanel root = new JPanel();
        root.add(new JLabel("Ograniczenie"));
        JTextField text = new JTextField("40");
        root.add(text);
        args = new Object[]{text};
        return root;
    }

    static public JPanel getBandpass(BufferedImage image) {
        JPanel root = new JPanel();
        root.add(new JLabel("Ograniczenie"));
        JTextField text1 = new JTextField("20");
        root.add(text1);
        JTextField text2 = new JTextField("40");
        root.add(text2);
        args = new Object[]{text1, text2};
        return root;
    }

    static public JPanel getBandblock(BufferedImage image) {
        JPanel root = new JPanel();
        root.add(new JLabel("Ograniczenie"));
        JTextField text1 = new JTextField("20");
        root.add(text1);
        JTextField text2 = new JTextField("40");
        root.add(text2);
        args = new Object[]{text1, text2};
        return root;
    }

    static public JPanel getHighpassWDetection(BufferedImage image) {
        JPanel root = new JPanel();
        root.add(new JLabel("Ograniczenie"));
        JTextField radius = new JTextField("100");
        root.add(radius);
        JTextField text = new JTextField("90,180");
        root.add(text);
        args = new Object[]{radius,text};
        return root;
    }

    static public JPanel getPhaseshift(BufferedImage image) {
        JPanel root = new JPanel();
        root.add(new JLabel("Ograniczenie"));
        JTextField text1 = new JTextField("200");
        root.add(text1);
        JTextField text2 = new JTextField("400");
        root.add(text2);
        args = new Object[]{text1, text2};
        return root;
    }
    JOptionPane optionPane;
    String ok, an;

    public FF(final Picture pic, String title, JPanel content) {
        super((Frame) null, true);
        setTitle(title);
        ok = "ok";
        an = "anuluj";
        Object[] options = {ok, an};
        optionPane = new JOptionPane(content,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        this.setSize(300, 150);
        this.setLocationByPlatform(true);
        setDefaultCloseOperation(MyDialog.DISPOSE_ON_CLOSE);
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                JOptionPane optionPane = FF.this.optionPane;
                String btnString1 = FF.this.ok, btnString2 = FF.this.an;
                if (FF.this.isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
                        JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
                    Object value = optionPane.getValue();
                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        return;
                    }
                    optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

                    if (btnString1.equals(value)) {
                        pic.repaint();
                        FF.this.setVisible(false);
                    } else if (btnString2.equals(value)) {
                        System.out.println("anuluj");
                        FF.this.setVisible(false);
                    }
                }
            }
        });

    }
}
