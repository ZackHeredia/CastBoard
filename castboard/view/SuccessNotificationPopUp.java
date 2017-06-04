package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class SuccessNotificationPopUp extends NotificationPopUp
{	
	public SuccessNotificationPopUp (JFrame frmParent)
	{
		super(frmParent, "castboard/res/icons/chk_64_cb.png");
	}
	
	public void optionSelected (int option) {}	
}