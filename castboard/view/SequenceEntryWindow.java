package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.plaf.basic.BasicSpinnerUI;
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
import javax.swing.JScrollPane;
import java.util.ArrayList;
import java.util.TreeMap;

public class SequenceEntryWindow extends Window
{
	private JPanel pnlGenerals;
	private JPanel pnlScript;
	private JPanel pnlOuterRoles;
	private JPanel pnlRoles;
	private JPanel pnlBottom;
	private JButton btnEnter;
	private ArrayList<Component> generalInputs;
	private ArrayList<Component> scriptInputs;
	private ArrayList<Component> roleInputs;
	private String requireMark;
	private int filledCounter;
	private String projectdId;
	private TreeMap<String, String> roles;

	public SequenceEntryWindow (String projectdId, TreeMap<String, String> roles)
	{
		masterFrame = MasterFrame.getInstance();
		generalInputs = new ArrayList<Component>();
		scriptInputs = new ArrayList<Component>();
		roleInputs = new ArrayList<Component>();
		requireMark = "<font color='red'>*</font>";
		filledCounter = 0;
		this.projectdId = projectdId;
		this.roles = roles;
		
		init();
	}

	protected void init ()
	{
		SequenceEntryWindow entry = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				createPnlGenerals(projectdId);
				createPnlScript();
				createPnlRoles(roles);
				createPnlBottom(roles);
				
				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				entry.setLayout(new BoxLayout(entry, BoxLayout.Y_AXIS));

				entry.add(pnlGenerals);
				entry.add(pnlScript);
				entry.add(pnlOuterRoles);
				entry.add(pnlBottom);
				entry.add(Box.createRigidArea(new Dimension(700, 700)));
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlGenerals (String projectdId)
	{
		JLabel lblNumber = new JLabel("<html>" + requireMark + "Número<html>");
		JLabel lblFilmingDate = new JLabel("<html>" + requireMark + "Fecha de filmación: <html>");
		JLabel lblDay = new JLabel("Dia");
		JLabel lblMonth = new JLabel("Mes");
		JLabel lblYear = new JLabel("Año");
		JLabel lblLocation = new JLabel("<html>" + requireMark + "Locación<html>");
		JLabel lblDescription = new JLabel("Descripción");
		SpinnerNumberModel numberModel = new SpinnerNumberModel(1, 1, 2000, 1);
		SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
		SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
		SpinnerNumberModel yearModel = new SpinnerNumberModel(2000, 1900, 2100, 1);
		JSpinner spnNumber = new JSpinner(numberModel);
		JSpinner spnDay = new JSpinner(dayModel);
		JSpinner spnMonth = new JSpinner(monthModel);
		JSpinner spnYear = new JSpinner(yearModel);
		JTextField txtLocation = new JTextField(16);
		JTextArea txaDescription = new JTextArea(16, 24);
		JScrollPane sclDescription = new JScrollPane(txaDescription);
		JPanel pnlNumber = new JPanel();
		JPanel pnlFilmingDate = new JPanel();
		JPanel pnlInnerFilmingDate = new JPanel();
		JPanel pnlDay = new JPanel();
		JPanel pnlMonth = new JPanel();
		JPanel pnlYear = new JPanel();
		JPanel pnlLocation = new JPanel();
		JPanel pnlDescription = new JPanel();
		JPanel pnlFirstColumn = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Generales";

		pnlGenerals = new JPanel();
		pnlGenerals.setBorder(BorderFactory.createTitledBorder(etched, title));
		
		pnlInnerFilmingDate.setLayout(new BoxLayout(pnlInnerFilmingDate, BoxLayout.Y_AXIS));
		pnlFirstColumn.setLayout(new BoxLayout(pnlFirstColumn, BoxLayout.Y_AXIS));

		pnlDay.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pnlMonth.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pnlYear.setAlignmentX(Component.RIGHT_ALIGNMENT);

		spnNumber.setName("" + 1);
		spnDay.setName("" + 1);
		spnMonth.setName("" + 1);
		spnYear.setName("" + 2000);

		hideArrow(spnNumber);
		spnNumber.setEditor(new JSpinner.NumberEditor(spnNumber, "####"));

		spnYear.setEditor(new JSpinner.NumberEditor(spnYear, "####"));

		txtLocation.getDocument().putProperty("parent", txtLocation);
		txtLocation.getDocument().addDocumentListener(new RequiredListener());

		pnlNumber.add(lblNumber);
		pnlNumber.add(spnNumber);

		pnlDay.add(lblDay);
		pnlDay.add(spnDay);

		pnlMonth.add(lblMonth);
		pnlMonth.add(spnMonth);

		pnlYear.add(lblYear);
		pnlYear.add(spnYear);

		pnlInnerFilmingDate.add(pnlDay);
		pnlInnerFilmingDate.add(pnlMonth);
		pnlInnerFilmingDate.add(pnlYear);

		pnlFilmingDate.add(lblFilmingDate);
		pnlFilmingDate.add(pnlInnerFilmingDate);

		pnlLocation.add(lblLocation);
		pnlLocation.add(txtLocation);

		pnlDescription.add(lblDescription);
		pnlDescription.add(sclDescription);

		pnlNumber.setToolTipText("Escriba el número de la secuencia a crear");
		pnlFilmingDate.setToolTipText("Seleccione la fecha de filmación de la secuencia a crear");
		pnlLocation.setToolTipText("Escriba la locación de la secuencia a crear");
		pnlDescription.setToolTipText("Escriba la descripción de la secuencia a crear");

		pnlFirstColumn.add(pnlNumber);
		pnlFirstColumn.add(pnlDescription);

		pnlGenerals.add(pnlFirstColumn);
		pnlGenerals.add(pnlFilmingDate);
		pnlGenerals.add(pnlLocation);

		generalInputs.add(new JTextField(projectdId));
		generalInputs.add(spnNumber);
		generalInputs.add(spnDay);
		generalInputs.add(spnMonth);
		generalInputs.add(spnYear);
		generalInputs.add(txtLocation);
		generalInputs.add(txaDescription);
	}
	@SuppressWarnings("unchecked")
	private void createPnlScript ()
	{
		String[] types = {"INT", "EXT"};
		String[] moments = {"Mañana", "Tarde", "Noche"};
		JLabel lblType = new JLabel("<html>" + requireMark + "Tipo de locación</html>");
		JLabel lblMoment = new JLabel("<html>" + requireMark + "Momento del dia</html>");
		JLabel lblPage = new JLabel("<html>" + requireMark + "Página</html>");
		JLabel lblDay = new JLabel("Dia");
		JComboBox cbbType = new JComboBox(types);
		JComboBox cbbMoment = new JComboBox(moments);
		SpinnerNumberModel pageModel = new SpinnerNumberModel(1, 1, 400, 1);
		SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 36500, 1);
		JSpinner spnPage = new JSpinner(pageModel);
		JSpinner spnDay = new JSpinner(dayModel);
		JPanel pnlType = new JPanel();
		JPanel pnlMoment = new JPanel();
		JPanel pnlPage = new JPanel();
		JPanel pnlDay = new JPanel();
		JPanel pnlThirdColumn = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Guión";

		pnlScript = new JPanel();
		pnlScript.setBorder(BorderFactory.createTitledBorder(etched, title));
		
		pnlThirdColumn.setLayout(new BoxLayout(pnlThirdColumn, BoxLayout.Y_AXIS));

		spnPage.setName("" + 1);
		spnDay.setName("" + 1);

		pnlType.add(lblType);
		pnlType.add(cbbType);

		pnlMoment.add(lblMoment);
		pnlMoment.add(cbbMoment);

		pnlPage.add(lblPage);
		pnlPage.add(spnPage);

		pnlDay.add(lblDay);
		pnlDay.add(spnDay);

		pnlType.setToolTipText("Seleccione el tipo de locación de la secuencia a crear");
		pnlMoment.setToolTipText("Seleccione el momento del dia de la secuencia a crear");
		pnlPage.setToolTipText("Seleccione la pagina en el guión de la secuencia a crear");
		pnlDay.setToolTipText("Seleccione el dia en el guión de la secuencia a crear");

		pnlThirdColumn.add(pnlPage);
		pnlThirdColumn.add(pnlDay);

		pnlScript.add(pnlType);
		pnlScript.add(pnlMoment);
		pnlScript.add(pnlThirdColumn);

		scriptInputs.add(cbbType);
		scriptInputs.add(cbbMoment);
		scriptInputs.add(spnPage);
		scriptInputs.add(spnDay);
	}
	private void createPnlRoles (TreeMap<String, String>  roles)
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
		spnRoles.setToolTipText("Seleccione la cantidad de roles de la secuencia a crear");

		spnRoles.addChangeListener(new ChangeListener()
		{
			public void stateChanged (ChangeEvent e)
			{
				JSpinner spnRoles = (JSpinner) e.getSource();
				int qtyRoles = ((Number) spnRoles.getValue()).intValue();

				prepareRolesFields(qtyRoles, roles.keySet().toArray(new String[0]));
			}
		});

		pnlInnerRoles.add(spnRoles);

		pnlOuterRoles.add(pnlInnerRoles, BorderLayout.PAGE_START);
		pnlOuterRoles.add(pnlRoles, BorderLayout.PAGE_END);
	}

	public void createPnlBottom (TreeMap<String, String> roles)
	{
		JLabel lblNote = new JLabel("<html>(" + requireMark + ") Campo obligatorio</html>");
		
		pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));

		btnEnter = new JButton("Ingresar");
		btnEnter.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnEnter.setEnabled(false);
		btnEnter.setToolTipText("Llene los campos obligatorios para crear la secuencia");

		lblNote.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnEnter.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				enter(roles);
			}
		});

		pnlBottom.add(lblNote);
		pnlBottom.add(btnEnter);
	}

	@SuppressWarnings("unchecked")
	public void prepareRolesFields (int qtyRoles, String[] roles)
	{
		int currentRoles = pnlRoles.getComponentCount();
		JLabel lblName;
		JComboBox cbbName;
		JPanel pnlName;

		if (currentRoles < qtyRoles)
		{
			for (int i = 0; i < (qtyRoles - currentRoles); i++)
			{
				pnlName = new JPanel();
				lblName = new JLabel("<html>" + requireMark + "Rol " + (i+currentRoles+1) + "</html>");
				cbbName = new JComboBox(roles);

				pnlName.add(lblName);
				pnlName.add(cbbName);

				pnlRoles.add(pnlName);

				roleInputs.add(cbbName);
			}
		}
		else if (currentRoles > qtyRoles)
		{
			for (int i = 0; i < (currentRoles - qtyRoles); i++)
			{
				pnlRoles.remove((currentRoles - 1) - i);
				roleInputs.remove((currentRoles - 1) - i);
			}
		}

		pnlRoles.revalidate();
		this.repaint();
	}

	private void enter (TreeMap<String, String> roles)
	{
		SequenceEntryWindow entry = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean wasEntered;

			protected Void doInBackground ()
			{
				ArrayList<String> inner = new ArrayList<String>();
				ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
				String key;
				String role;

				values.add(fillArray(generalInputs));
				values.add(fillArray(scriptInputs));

				for (Component input : roleInputs)
				{
					if (input instanceof JComboBox)
					{
						inner = new ArrayList<String>();
						key = ((String) (((JComboBox) input).getSelectedItem()));
						role = key.substring(0, ((String) (((JComboBox) input).getSelectedItem())).indexOf("["));
					
						inner.add(role);
						inner.add((roles.get(key) == null) ? "0" : roles.get(key));

						values.add(inner);
					}
				}

				wasEntered = CatalogsHandler.enter(values, CatalogsHandler.SEQUENCE_SET);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(wasEntered)
				{
					(new SuccessNotificationPopUp(masterFrame)).display("¡La secuencia ha sido creada!");
					clearInputs(generalInputs);
					clearInputs(scriptInputs);
					clearInputs(roleInputs);
				}
				else
					(new FailureNotificationPopUp(masterFrame)).display("La secuencia no ha sido creada");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private ArrayList<String> fillArray (ArrayList<Component> inputs)
	{
		ArrayList<String> inner = new ArrayList<String>();

		for (Component input : inputs)
		{
			if (input instanceof JTextField)
				inner.add(((JTextField) input).getText());
			else if (input instanceof JComboBox)
				inner.add((String) ((JComboBox) input).getSelectedItem());
			else if (input instanceof JTextArea)
				inner.add(((JTextArea) input).getText());
			else if (input instanceof JSpinner)
				inner.add(((Integer) ((JSpinner) input).getValue()).toString());
		}

		return inner;
	}

	private void clearInputs (ArrayList<Component> inputs)
	{
		for (Component input : inputs)
		{
			if (input instanceof JTextField)
				((JTextField) input).setText("");
			else if (input instanceof JComboBox)
				((JComboBox) input).setSelectedItem(0);
			else if (input instanceof JTextArea)
				((JTextArea) input).setText("");
			else if (input instanceof JSpinner)
				((JSpinner) input).setValue(Integer.parseInt(((JSpinner) input).getName()));
		}
	}

	private void hideArrow(JSpinner spinner) 
	{
        Dimension d = spinner.getPreferredSize();
        d.width = 35;

        spinner.setUI(new BasicSpinnerUI() 
        {
            protected Component createNextButton() 
            {
                return null;
            }
            protected Component createPreviousButton() 
            {
                return null;
            }
        });

        spinner.setPreferredSize(d);
    }

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
    		int qtyRequired = 1;

    		filledCounter += (!input.getText().equals("") && !wasFilled) ? 1 : (filledCounter==0 || !input.getText().equals("")) ? 0 : -1;
    		wasFilled = (!input.getText().equals(""));

    		if (filledCounter < qtyRequired)
    		{
    			btnEnter.setEnabled(false);
    			btnEnter.setToolTipText("Llene los campos obligatorios para crear la secuencia");
    		}
    		else
    		{
    			btnEnter.setEnabled(true);
    			btnEnter.setToolTipText("Crear la secuencia");
    		}
    	}
	}
}