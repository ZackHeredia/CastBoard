package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class ConfirmationPopUp extends PopUp
{
	private boolean response;

	public ConfirmationPopUp (JFrame frmParent)
	{
		super(frmParent, "Confirmación - CastBoard", JOptionPane.YES_NO_OPTION, 
			  (new ImageIcon("castboard/res/icons/qst_64_cb.png")));
	}

	public boolean display (String message)
	{
		super.display(message);

		return response;
	}
	public void display (Object content) {}
	
	public void optionSelected (int option) 
	{
		if (option == JOptionPane.YES_OPTION)
			response = true;
		else
			response = false;
	}
}