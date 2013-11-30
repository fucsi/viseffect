package fotoszop.gui;

import java.awt.Dimension;
import java.util.Random;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author KAMIL L.
 */
public class MyInternalFrame extends JInternalFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7339174626962468150L;
	private static int idCount = 1;
    private int id = idCount++;
    
    private Picture picture = null;
    static private Random rand = new Random(System.currentTimeMillis());
    public int getId() {
        return id;
    }

    public MyInternalFrame(String filename) {

        super(filename,
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable

        picture = new Picture(filename);
        int w = picture.getImage().getWidth(),h = picture.getImage().getHeight();
        setSize(w<100?100:w,h<100?100:h);
        setTitle(filename + " " + id);
        JScrollPane scroll = new JScrollPane(picture);
        scroll.setPreferredSize(new Dimension(200,200));
        this.add(scroll);
        this.setDoubleBuffered(true);
        setLocation(60 + rand.nextInt()%30, 60 + rand.nextInt()%30);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    
}
