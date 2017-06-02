package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.SwingWorker;
import java.util.ArrayList;

public class TalentUpdateWindow extends TalentEntryWindow
{
	private String id;
	private ArrayList<ArrayList<String>> values;

	public TalentUpdateWindow (String id, ArrayList<ArrayList<String>> values)
	{
		super("actualizar");

		this.id = id;
		this.values = values;
	}

	protected void reform ()
	{
		prepareBtnUpdate();
		try
		{fillInputs();}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareBtnUpdate ()
	{
		btnEnter.setText("Actualizar");
		btnEnter.setToolTipText("Actualizar el talento");
	}

	private void fillInputs ()
	{
		String name = values.get(1).get(0).substring(0, values.get(1).get(0).indexOf(" "));
		String surname = values.get(1).get(0).substring(values.get(1).get(0).indexOf(" ") + 1, 
														values.get(1).get(0).length());
		int day = Integer.parseInt(values.get(1).get(1).substring(values.get(1).get(1).lastIndexOf("-") + 1,
																  values.get(1).get(1).length()));
		int month = Integer.parseInt(values.get(1).get(1).substring(values.get(1).get(1).indexOf("-") + 1,
																  	values.get(1).get(1).lastIndexOf("-")));
		int year = Integer.parseInt(values.get(1).get(1).substring(0, values.get(1).get(1).indexOf("-")));
		boolean isSelected = ((ButtonGroup) generalInputs.get(5)).getSelection().getActionCommand().equals(values.get(1).get(2));	
		String prefixMobile = values.get(4).get(0).substring(1, 4);
		String mobile1 = values.get(4).get(0).substring(6, 9);
		String mobile2 = values.get(4).get(0).substring(10, values.get(4).get(0).length());
		String prefixHome = values.get(4).get(5).substring(1, 4);
		String home1 = values.get(4).get(5).substring(6, 9);
		String home2 = values.get(4).get(5).substring(10, values.get(4).get(5).length());
		String number = values.get(4).get(4).substring(values.get(4).get(4).indexOf("#") + 1,
													   values.get(4).get(4).indexOf(","));
		String street = values.get(4).get(4).substring(values.get(4).get(4).indexOf("/") + 2,
													   values.get(4).get(4).indexOf("#") - 1);
		String neighborhood = values.get(4).get(4).substring(values.get(4).get(4).indexOf(",") + 2,
													   		 values.get(4).get(4).length());

		String feet;
		String inches;

		String skills = values.get(5).get(0);
		String skill = values.get(5).get(0).replace("Actuación", "").replace("Baile", "").
											replace("Música", "").replace("Canto", "").replace(", ", "");
		String languages = values.get(5).get(1);
		String language = values.get(5).get(1).replace("Inglés", "").replace("Francés", "").replace(", ", "");

		
		((JTextField) generalInputs.get(0)).setText(name);
		((JTextField) generalInputs.get(1)).setText(surname);
		((JSpinner) generalInputs.get(2)).setValue(day);
		((JSpinner) generalInputs.get(3)).setValue(month);
		((JSpinner) generalInputs.get(4)).setValue(year);
		((ButtonGroup) generalInputs.get(5)).setSelected(((ButtonGroup) generalInputs.get(5)).getSelection(),
														  isSelected);
		
		((JComboBox) ((JPanel) contactInputs.get(0)).getComponents()[1]).setSelectedItem(prefixMobile);
		((JSpinner.NumberEditor) ((JSpinner) ((JPanel) contactInputs.get(0)).getComponents()[2]).
			getEditor()).getTextField().setText(mobile1);
		((JSpinner.NumberEditor) ((JSpinner) ((JPanel) contactInputs.get(0)).getComponents()[3]).
			getEditor()).getTextField().setText(mobile2);		
		((JComboBox) ((JPanel) contactInputs.get(1)).getComponents()[1]).setSelectedItem(prefixHome);
		((JSpinner.NumberEditor) ((JSpinner) ((JPanel) contactInputs.get(1)).getComponents()[2]).
			getEditor()).getTextField().setText(home1);
		((JSpinner.NumberEditor) ((JSpinner) ((JPanel) contactInputs.get(1)).getComponents()[3]).
			getEditor()).getTextField().setText(home2);
		((JTextField) contactInputs.get(2)).setText(values.get(4).get(1));
		((JTextField) contactInputs.get(3)).setText(values.get(4).get(6));
		((JTextField) contactInputs.get(4)).setText(values.get(4).get(2));
		((JTextField) contactInputs.get(5)).setText(number);
		((JTextField) contactInputs.get(6)).setText(street);
		((JTextField) contactInputs.get(7)).setText(neighborhood);
		((JComboBox) contactInputs.get(8)).setSelectedItem(values.get(4).get(8));
		((JTextField) contactInputs.get(9)).setText(values.get(4).get(3));

		((JComboBox) profileInputs.get(0)).setSelectedItem(values.get(2).get(2));
		((JComboBox) profileInputs.get(1)).setSelectedItem(values.get(2).get(3));

		if (!values.get(2).get(1).equals(""))
		{
			feet = values.get(2).get(1).substring(0, values.get(2).get(1).indexOf("'"));
			inches = values.get(2).get(1).substring(values.get(2).get(1).indexOf(" ") + 1, 
														   values.get(2).get(1).indexOf("''"));

			((JSpinner) profileInputs.get(2)).setValue(Integer.parseInt(feet));
			((JSpinner) profileInputs.get(3)).setValue(Integer.parseInt(inches));
		}

		((JComboBox) profileInputs.get(4)).setSelectedItem(values.get(2).get(4));
		((JTextField) profileInputs.get(5)).setText(values.get(2).get(6));
		((JComboBox) profileInputs.get(6)).setSelectedItem(values.get(2).get(0));
		((JTextField) profileInputs.get(7)).setText(values.get(2).get(5));

		for (int i = 0; i < measuresInputs.size(); i++)
		{
			if (measuresInputs.get(i) instanceof JTextField)
				((JTextField) measuresInputs.get(i)).setText(values.get(3).get(i));
			else if (measuresInputs.get(i) instanceof JComboBox)
				((JComboBox) measuresInputs.get(i)).setSelectedItem(values.get(3).get(i));
		}

		((JTextField) aptitudesInputs.get(0)).setText(values.get(5).get(2));
		((JTextArea) aptitudesInputs.get(1)).setText(values.get(5).get(5));
		((JTextArea) aptitudesInputs.get(2)).setText(values.get(5).get(3));
		((JTextArea) aptitudesInputs.get(3)).setText(values.get(5).get(4));

		((JCheckBox) skillInputs.get(0)).setSelected(skills.contains("Actuación"));
		((JCheckBox) skillInputs.get(1)).setSelected(skills.contains("Baile"));
		((JCheckBox) skillInputs.get(2)).setSelected(skills.contains("Música"));
		((JCheckBox) skillInputs.get(3)).setSelected(skills.contains("Canto"));
		((JTextField) skillInputs.get(4)).setText(skill);

		((JCheckBox) languageInputs.get(0)).setSelected(languages.contains("Inglés"));
		((JCheckBox) languageInputs.get(1)).setSelected(languages.contains("Francés"));
		((JTextField) languageInputs.get(2)).setText(language);

		for (int i = 0; i < mediaInputs.size(); i++)
		{
			((JLabel) mediaInputs.get(i)).setText(values.get(0).get(i));
		}
	}

	protected void enter ()
	{
		TalentUpdateWindow entry = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean wasEntered;

			protected Void doInBackground ()
			{
				ArrayList<String> inner = new ArrayList<String>();
				ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

				inner.add(id);
				values.add(inner);

				values.add(fillArray(generalInputs));
				values.add(fillArray(contactInputs));
				values.add(fillArray(profileInputs));
				values.add(fillArray(measuresInputs));
				values.add(fillArray(aptitudesInputs));
				values.add(fillArray(skillInputs));
				values.add(fillArray(languageInputs));
				values.add(fillArray(mediaInputs));

				wasEntered = CatalogsHandler.update(values, CatalogsHandler.TALENT_SET);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(wasEntered)
				{
					(new SuccessNotificationPopUp(masterFrame)).display("¡El talento ha sido actualizado!");
					masterFrame.previousWindow(entry);
				}
				else
					(new FailureNotificationPopUp(masterFrame)).display("El talento no ha sido actualizado");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}
}