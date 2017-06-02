package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Component;
import javax.swing.SwingWorker;
import java.util.ArrayList;

public class ProjectUpdateWindow extends ProjectEntryWindow
{
	private String id;
	private ArrayList<ArrayList<String>> values;

	public ProjectUpdateWindow (String id, ArrayList<ArrayList<String>> values)
	{
		super("actualizar");

		this.id = id;
		this.values = values;
	}

	protected void reform ()
	{
		prepareBtnUpdate();
		fillInputs();
	}

	private void prepareBtnUpdate ()
	{
		super.btnEnter.setText("Actualizar");
    			btnEnter.setToolTipText("Actualizar el proyecto");
	}

	private void fillInputs ()
	{
		int roleCounter = 0;
		String name;
		String category;
		ArrayList<ArrayList<String>> roleValues = new ArrayList<ArrayList<String>>(values.subList(1, values.size()));
		
		for (int i = 0; i < generalInputs.size(); i++)
		{
			if (generalInputs.get(i) instanceof JTextField)
				((JTextField) generalInputs.get(i)).setText(values.get(0).get(i));
			else if (generalInputs.get(i) instanceof JComboBox)
				((JComboBox) generalInputs.get(i)).setSelectedItem(values.get(0).get(i));
		}

		prepareRolesFields(roleValues.size());

		for (int i = 0; i < roleValues.size(); i++)
		{
			if (roleValues.get(i).size() == 1 && roleValues.get(i).get(0).contains("["))
			{
				name = roleValues.get(i).get(0).substring(0, roleValues.get(i).get(0).indexOf("["));
				category = roleValues.get(i).get(0).substring((roleValues.get(i).get(0).indexOf("[") + 1), roleValues.get(i).get(0).indexOf("]"));
				roleCounter++;

				for (Component input : roleInputs)
				{
					if (input instanceof JComboBox && ((JComboBox) input).getName() == null)
					{
						((JComboBox) input).setSelectedItem(category);
						((JComboBox) input).setName("changed");
						continue;
					}
					else if (input instanceof JTextField && ((JTextField) input).getText().equals(""))
					{
						((JTextField) input).setText(name);
						break;
					}
				}
			}
		}

		prepareRolesFields(roleCounter);
	}

	protected void enter ()
	{
		ProjectUpdateWindow entry = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean wasEntered;

			protected Void doInBackground ()
			{
				ArrayList<String> inner = new ArrayList<String>();
				ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

				inner.add(id);
				values.add(inner);

				inner = new ArrayList<String>();

				for (Component input : generalInputs)
				{
					if (input instanceof JTextField)
						inner.add(((JTextField) input).getText());
					else if (input instanceof JComboBox)
						inner.add((String) ((JComboBox) input).getSelectedItem());
				}
				values.add(inner);

				for (Component input : roleInputs)
				{
					if (input instanceof JComboBox)
					{
						inner = new ArrayList<String>();
						inner.add((String) ((JComboBox) input).getSelectedItem());
					}
					else if (input instanceof JTextField)
					{
						inner.add(((JTextField) input).getText());
						values.add(inner);
					}
				}

				wasEntered = CatalogsHandler.update(values, CatalogsHandler.PROJECT_SET);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(wasEntered)
				{
					(new SuccessNotificationPopUp(masterFrame)).display("¡El proyecto ha sido actualizado!");
					masterFrame.previousWindow(entry);
				}
				else
					(new FailureNotificationPopUp(masterFrame)).display("El proyecto no ha sido actualizado");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

}