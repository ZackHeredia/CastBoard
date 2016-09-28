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
			  (new ImageIcon("castboard/res/icons/bln_64_cb.png")));
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
		Component[] components = pnlForm.getComponents();
		
		for (int i = 0; i < components.length; i++)
		{
			if (components[i].getClass().equals(JComboBox.class))
				values[i] = (String) ((JComboBox) components[i]).getSelectedItem();
			else
				values[i] = ((JTextField) components[i]).getText();
		}
	}
}