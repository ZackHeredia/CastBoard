package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class FailureNotificationPopUp extends NotificationPopUp
{	
	public FailureNotificationPopUp (JFrame frmParent)
	{
		super(frmParent, "castboard/res/icons/crs_64_cb.png");
	}
	
	public void optionSelected (int option) {}	
}