package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public class ProjectEntryWindow extends Window
{
	private JPanel pnlGenerals;
	private JPanel pnlOuterRoles;
	private JPanel pnlRoles;
	private JPanel pnlBottom;
	protected JButton btnEnter;
	protected ArrayList<Component> generalInputs;
	protected ArrayList<Component> roleInputs;
	protected String requireMark;
	private int filledCounter;

	protected final String ACTION;

	public ProjectEntryWindow ()
	{
		this("ingresar");
	}
	public ProjectEntryWindow (String action)
	{
		masterFrame = MasterFrame.getInstance();
		generalInputs = new ArrayList<Component>();
		roleInputs = new ArrayList<Component>();
		requireMark = "<font color='red'>*</font>";
		filledCounter = 0;
		ACTION = action;
		
		init();
	}

	protected void init ()
	{
		ProjectEntryWindow entry = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				createPnlGenerals();
				createPnlRoles();
				createPnlBottom();
				reform();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				entry.setLayout(new BoxLayout(entry, BoxLayout.Y_AXIS));

				entry.add(pnlGenerals);
				entry.add(pnlOuterRoles);
				entry.add(pnlBottom);
				entry.add(Box.createRigidArea(new Dimension(700, 700)));
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	@SuppressWarnings("unchecked")
	private void createPnlGenerals ()
	{
		String[] types = {"Comercial", "Fotografico", "Cinematografico"};
		JLabel lblTitle = new JLabel("<html>" + requireMark + "Titulo</html>");
		JLabel lblType = new JLabel("<html>" + requireMark + "Tipo</html>");
		JLabel lblProducer = new JLabel("<html>" + requireMark + "Productor</html>");
		JLabel lblDirector = new JLabel("<html>" + requireMark + "Director</html>");
		JTextField txtTitle = new JTextField(16);
		JComboBox cbbType = new JComboBox(types);
		JTextField txtProducer = new JTextField(16);
		JTextField txtDirector = new JTextField(16);
		JPanel pnlTitle = new JPanel();
		JPanel pnlType = new JPanel();
		JPanel pnlProducer = new JPanel();
		JPanel pnlDirector = new JPanel();
		JPanel pnlThirdColumn = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Generales";

		pnlGenerals = new JPanel();
		pnlGenerals.setBorder(BorderFactory.createTitledBorder(etched, title));
		
		pnlThirdColumn.setLayout(new BoxLayout(pnlThirdColumn, BoxLayout.Y_AXIS));

		txtTitle.getDocument().putProperty("parent", txtTitle);
		txtProducer.getDocument().putProperty("parent", txtProducer);
		txtDirector.getDocument().putProperty("parent", txtDirector);

		txtTitle.getDocument().addDocumentListener(new RequiredListener());
		txtProducer.getDocument().addDocumentListener(new RequiredListener());
		txtDirector.getDocument().addDocumentListener(new RequiredListener());

		pnlTitle.add(lblTitle);
		pnlTitle.add(txtTitle);

		pnlType.add(lblType);
		pnlType.add(cbbType);

		pnlProducer.add(lblProducer);
		pnlProducer.add(txtProducer);

		pnlDirector.add(lblDirector);
		pnlDirector.add(txtDirector);

		pnlTitle.setToolTipText("Escriba el titulo del proyecto a " + ACTION + "");
		pnlType.setToolTipText("Seleccione el tipo del proyecto a " + ACTION + "");
		pnlProducer.setToolTipText("Escriba el productor del proyecto a " + ACTION + "");
		pnlDirector.setToolTipText("Escriba el director del proyecto a " + ACTION + "");

		pnlThirdColumn.add(pnlProducer);
		pnlThirdColumn.add(pnlDirector);

		pnlGenerals.add(pnlTitle);
		pnlGenerals.add(pnlType);
		pnlGenerals.add(pnlThirdColumn);

		generalInputs.add(txtTitle);
		generalInputs.add(cbbType);
		generalInputs.add(txtProducer);
		generalInputs.add(txtDirector);
	}
	private void createPnlRoles ()
	{
		SpinnerNumberModel roleModel = new SpinnerNumberModel(0, 0, 100, 1);
		JLabel lblRoles = new JLabel("Cantidad de roles");
		JPanel pnlInnerRoles = new JPanel();
		JSpinner spnRoles = new JSpinner(roleModel);
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Roles";

		pnlRoles = new JPanel();
		pnlRoles.setLayout(new BoxLayout(pnlRoles, BoxLayout.Y_AXIS));

		pnlOuterRoles = new JPanel();
		pnlOuterRoles.setBorder(BorderFactory.createTitledBorder(etched, title));
		pnlOuterRoles.setLayout(new BorderLayout());

		spnRoles.setName("0");
		spnRoles.setAlignmentX(Component.LEFT_ALIGNMENT);
		spnRoles.setToolTipText("Seleccione la cantidad de roles del proyecto a " + ACTION + "");

		spnRoles.addChangeListener(new ChangeListener()
		{
			public void stateChanged (ChangeEvent e)
			{
				JSpinner spnRoles = (JSpinner) e.getSource();
				int qtyRoles = ((Number) spnRoles.getValue()).intValue();

				prepareRolesFields(qtyRoles);
			}
		});

		pnlInnerRoles.add(spnRoles);

		pnlOuterRoles.add(pnlInnerRoles, BorderLayout.PAGE_START);
		pnlOuterRoles.add(pnlRoles, BorderLayout.PAGE_END);
	}

	private void createPnlBottom ()
	{
		JLabel lblNote = new JLabel("<html>(" + requireMark + ") Campo obligatorio</html>");
		
		pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));

		btnEnter = new JButton("Ingresar");
		btnEnter.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnEnter.setEnabled(false);
		btnEnter.setToolTipText("Llene los campos obligatorios para " + ACTION + " el proyecto");

		lblNote.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnEnter.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				enter();
			}
		});

		pnlBottom.add(lblNote);
		pnlBottom.add(btnEnter);
	}

	@SuppressWarnings("unchecked")
	protected void prepareRolesFields (int qtyRoles)
	{
		int currentRoles = pnlRoles.getComponentCount();
		JLabel lblName;
		JLabel lblCategory;
		JTextField txtName;
		JComboBox cbbCategory;
		JPanel pnlRol;
		JPanel pnlName;
		JPanel pnlCategory;
		int qtyRequired;
		String[] categories = {"Principal", "Secundario", "Reparto", "Figurante", "Extra"};

		if (currentRoles < qtyRoles)
		{
			for (int i = 0; i < (qtyRoles - currentRoles); i++)
			{
				pnlRol = new JPanel();
				pnlRol.setAlignmentX(Component.LEFT_ALIGNMENT);

				pnlName = new JPanel();
				pnlCategory = new JPanel();

				lblName = new JLabel("<html>" + requireMark + "Nombre R" + (i+1+currentRoles) + "</html>");
				lblCategory = new JLabel("<html>" + requireMark + "Categoria R" + (i+1+currentRoles) + "</html>");

				txtName = new JTextField(16);
				cbbCategory = new JComboBox(categories);

				txtName.getDocument().putProperty("parent", txtName);
				txtName.getDocument().addDocumentListener(new RequiredListener());

				pnlName.add(lblName);
				pnlName.add(txtName);

				pnlCategory.add(lblCategory);
				pnlCategory.add(cbbCategory);

				pnlRol.add(pnlName);
				pnlRol.add(pnlCategory);

				pnlRoles.add(pnlRol);

				roleInputs.add(cbbCategory);
				roleInputs.add(txtName);
			}

			if (btnEnter.isEnabled())
			{	
				btnEnter.setEnabled(false);
				btnEnter.setToolTipText("Llene los campos obligatorios para " + ACTION + " el proyecto");
			}

		}
		else if (currentRoles > qtyRoles)
		{
			for (int i = 0; i < (currentRoles - qtyRoles); i++)
			{
				pnlRoles.remove(pnlRoles.getComponentCount() - 1);
				roleInputs.remove(roleInputs.size() - 1);
				roleInputs.remove(roleInputs.size() - 1);
			}

			qtyRequired = (generalInputs.size() - 1) + (roleInputs.size() / 2);
			if (filledCounter>=qtyRequired)
			{	
				btnEnter.setEnabled(true);
    			btnEnter.setToolTipText("Ingresar el proyecto");
			}
		}

		pnlRoles.revalidate();
		this.repaint();
	}

	protected void enter ()
	{
		ProjectEntryWindow entry = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean wasEntered;

			protected Void doInBackground ()
			{
				ArrayList<String> inner = new ArrayList<String>();
				ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

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

				wasEntered = CatalogsHandler.enter(values, CatalogsHandler.PROJECT_SET);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(wasEntered)
				{
					(new SuccessNotificationPopUp(masterFrame)).display("¡El proyecto ha sido ingresado!");
					clearInputs(generalInputs);
					clearInputs(roleInputs);
				}
				else
					(new FailureNotificationPopUp(masterFrame)).display("El proyecto no ha sido ingresado");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void clearInputs (ArrayList<Component> inputs)
	{
		for (Component input : inputs)
		{
			if (input instanceof JTextField)
				((JTextField) input).setText("");
			else if (input instanceof JComboBox)
				((JComboBox) input).setSelectedItem(0);
			else if (input instanceof JSpinner)
				((JSpinner) input).setValue(Integer.parseInt(((JSpinner) input).getName()));
		}
	}

	protected void reform () {}

	private class RequiredListener implements DocumentListener
	{
		private boolean wasFilled;

		public RequiredListener ()
		{
			wasFilled = false;
		}

		public void insertUpdate (DocumentEvent e) 
		{
    	    valid((JTextField) e.getDocument().getProperty("parent"));
    	}
    	public void removeUpdate (DocumentEvent e) 
    	{
    	    valid((JTextField) e.getDocument().getProperty("parent"));
    	}
    	public void changedUpdate (DocumentEvent e) {}

    	private void valid (JTextField input)
    	{
    		int qtyRequired = (generalInputs.size() - 1) + (roleInputs.size() / 2);

    		filledCounter += (!input.getText().equals("") && !wasFilled) ? 1 : (input.getText().equals("") && wasFilled) ? -1 : 0;
    		wasFilled = (!input.getText().equals(""));

    		if (filledCounter < qtyRequired)
    		{
    			btnEnter.setEnabled(false);
    			btnEnter.setToolTipText("Llene los campos obligatorios para " + ACTION + " el proyecto");
    		}
    		else
    		{
    			btnEnter.setEnabled(true);
    			btnEnter.setToolTipText(btnEnter.getText() + " el proyecto");
    		}
    	}
	}
}