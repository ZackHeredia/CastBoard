package castboard.view;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Component;


public class PetitionPopUp extends PopUp
{
	private JPanel pnlForm;
	private String[] values;

	public PetitionPopUp (JFrame frmParent)
	{
		super(frmParent, "Petición - CastBoard", JOptionPane.OK_CANCEL_OPTION, 
			  "castboard/res/icons/bln_64_cb.png");
	}

	public String[] display (JPanel pnlForm)
	{
		this.pnlForm = pnlForm;

		super.display(pnlForm);

		return values;
	}
	public void display (Object content) {}
	
	public void optionSelected (int option) 
	{
		Component[] components;

		if (option == JOptionPane.OK_OPTION)
		{
			components = pnlForm.getComponents();
			values = new String[components.length];
			
			for (int i = 0; i < components.length; i++)
			{
				if (((JPanel) components[i]).getComponents()[1] instanceof JComboBox)
					values[i] = (String) ((JComboBox) ((JPanel) components[i]).getComponents()[1]).getSelectedItem();
				else
					values[i] = ((JTextField) ((JPanel) components[i]).getComponents()[1]).getText();
			}
		}
		else
		{
			values = new String[1];
			values[0] = null;
		}
	}
}