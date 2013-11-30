package fotoszop.gui;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author kalkan
 */
public class Picture extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8470621637130185956L;
	private BufferedImage picture = null;
    private double zoomX = 1.0;
//    public Complex[][] fr, fg, fb;

    public double getZoomX() {
        return zoomX;
    }

    public void setZoomX(double zoomX) {
        this.zoomX = zoomX;
    }

    public double getZoomY() {
        return zoomY;
    }

    public void setZoomY(double zoomY) {
        this.zoomY = zoomY;
    }
    private double zoomY = 1.0;
    public BufferedImage getImage() {
        return picture;
    }

    public void setImage(BufferedImage picture) {
        this.picture = picture;
    }

    public Picture(String filename) {
        try {
            this.picture = ImageIO.read(new File(filename));
            //BufferedImage pic = ImageIO.read(new File(filename));
            //picture = new BufferedImage(pic.getWidth(), pic.getHeight(), BufferedImage.TYPE_INT_RGB);
            //picture.getGraphics().drawImage(pic, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(picture,
                0, 0, (int)Math.round(picture.getWidth()*zoomX), (int)Math.round(picture.getHeight()*zoomY),
                null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int)(this.picture.getWidth()*zoomX),(int)(this.picture.getHeight()*zoomX));
    }
   


}
