package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public abstract class NotificationPopUp extends PopUp
{	
	public NotificationPopUp (JFrame frmParent, ImageIcon icon)
	{
		super(frmParent, "Notificación - CastBoard", JOptionPane.DEFAULT_OPTION, icon);
	}
}