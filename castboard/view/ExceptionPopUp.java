package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class ExceptionPopUp extends PopUp
{
	public ExceptionPopUp (JFrame frmParent)
	{
		super(frmParent, "Excepción - CastBoard", JOptionPane.DEFAULT_OPTION, 
			  (new ImageIcon("castboard/res/icons/_64_cb.png")));
	}
	
	public void optionSelected (int option) {}
}