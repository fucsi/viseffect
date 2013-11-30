package fotoszop.tools;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;


public class Separator extends ToolInterface{
	
	private JMenuItem menuItem;
	
	public Separator(){
		menuItem = new JMenuItem();
		menuItem.setEnabled(false);
		menuItem.add(new JSeparator());
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menuItem;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
