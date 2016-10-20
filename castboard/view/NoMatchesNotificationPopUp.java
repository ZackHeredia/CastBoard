package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class NoMatchesNotificationPopUp extends NotificationPopUp
{	
	public NoMatchesNotificationPopUp (JFrame frmParent)
	{
		super(frmParent, (new ImageIcon("castboard/res/icons/pzl_64_cb.png")));
	}
	
	public void optionSelected (int option) {}	
}