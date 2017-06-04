package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class CautionPopUp extends PopUp
{
	public CautionPopUp (JFrame frmParent)
	{
		super(frmParent, "Precaución - CastBoard", JOptionPane.DEFAULT_OPTION, 
			  "castboard/res/icons/xcm_64_cb.png");
	}
	
	public void optionSelected (int option) {}
}