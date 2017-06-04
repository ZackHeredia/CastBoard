package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.Image;
import java.net.URL;

public abstract class PopUp
{
	protected JFrame frmParent;
	protected String title;
	protected int optionType;
	protected ImageIcon icon;
	
	public PopUp (JFrame frmParent, String title, int optionType, String icon)
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL url = getClass().getClassLoader().getResource(icon);
		Image img = tk.getImage(url);

		this.frmParent = frmParent;
		this.title = title;
		this.optionType = optionType;
		this.icon = new ImageIcon(img);
	}
	
	public void display (Object content)
	{					
		int option = JOptionPane.showConfirmDialog
					 (
						 frmParent,
						 content,
						 title,
						 optionType,
						 JOptionPane.PLAIN_MESSAGE,
						 icon
					 );
							 
		optionSelected(option);
	}

	public abstract void optionSelected (int option);
}