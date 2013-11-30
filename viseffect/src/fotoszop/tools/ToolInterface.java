package fotoszop.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JMenuItem;

import fotoszop.FotoszopAboutBox;

/**
 * 
 * @author kalkan
 */
public class ToolInterface implements ActionListener, PropertyChangeListener {

	protected org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
			.getInstance(fotoszop.FotoszopApp.class).getContext()
			.getResourceMap(FotoszopAboutBox.class);

	public JMenuItem getJMenuItem() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void propertyChange(PropertyChangeEvent evt) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List runThisTool(List<BufferedImage> images, List arguments) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setResourceMap(org.jdesktop.application.ResourceMap resourceMap) {
		this.resourceMap = resourceMap;
	}

	public org.jdesktop.application.ResourceMap getResourceMap() {
		return resourceMap;
	};
}
