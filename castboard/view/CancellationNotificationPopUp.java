package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class CancellationNotificationPopUp extends NotificationPopUp
{	
	public CancellationNotificationPopUp (JFrame frmParent)
	{
		super(frmParent, "castboard/res/icons/fbd_64_cb.png");
	}
	
	public void optionSelected (int option) {}	
}