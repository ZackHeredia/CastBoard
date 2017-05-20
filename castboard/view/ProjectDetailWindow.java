package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.TreeMap;

public class ProjectDetailWindow extends Window
{
	private JPanel pnlTitle;
	private JPanel pnlActions;
	private JPanel pnlGenerals;
	private JPanel pnlRoles;
	private ArrayList<ArrayList<String>> values;
	private String id;

	public ProjectDetailWindow (String id)
	{
		this.id = id;
		masterFrame = MasterFrame.getInstance();
		
		init();
	}

	protected void init ()
	{
		ProjectDetailWindow detail = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{try
				{
				values = CatalogsHandler.get(id, CatalogsHandler.PROJECT_SET);
				TreeMap<String, String> roles = new TreeMap<String, String>();
				
				createPnlTitle();
				createPnlActions(roles);
				createPnlGenerals();
				createPnlRoles(roles);}catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));

				detail.add(pnlTitle);
				detail.add(pnlActions);
				detail.add(pnlGenerals);
				detail.add(pnlRoles);

				detail.validate();
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlTitle ()
	{
		JLabel lblTitle = new JLabel("<html><h1>" + values.get(0).get(0) + "</h1></html>");
		
		pnlTitle =new JPanel();
		pnlTitle.setMaximumSize(new Dimension(1024, 32));
		
		lblTitle.setForeground(new Color(0, 146, 182));

		lblTitle.setToolTipText("Titulo");

		pnlTitle.add(lblTitle);
	}
	private void createPnlActions (TreeMap<String, String> roles)
	{
		JButton btnUpdate = new JButton("Actualizar");
		JButton btnDelete = new JButton("Eliminar");
		JButton btnPresent = new JButton("Presentar");
		JButton btnBreakdown = new JButton("Desglose");
		JButton btnTerminate = new JButton("Terminar");

		pnlActions = new JPanel();
		pnlActions.setMaximumSize(new Dimension(1024, 32));

		btnUpdate.setToolTipText("Actualizar este proyecto");
		btnDelete.setToolTipText("Eliminar este proyecto");
		btnPresent.setToolTipText("Presentar talentos preseleccionados");
		btnBreakdown.setToolTipText("Ver desglose de secuencias del proyecto");
		btnTerminate.setToolTipText("Terminar este proyecto");

		if (!(values.size()>2 && values.get(2).size()>1))
			btnPresent.setEnabled(false);

		btnUpdate.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				masterFrame.displayProjectUpdate(id, values);
			}
		});
		btnDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				delete();
			}
		});
		btnPresent.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				present();
			}
		});
		btnBreakdown.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				masterFrame.displaySequenceBreakdown(id, roles);
			}
		});
		btnTerminate.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				terminate();
			}
		});

		pnlActions.add(btnUpdate);
		pnlActions.add(btnDelete);
		pnlActions.add(btnPresent);
		if (values.get(0).get(1).equals("Cinematografico") && values.get(0).get(4).equals("Desarrollo"))
			pnlActions.add(btnBreakdown);
		pnlActions.add(btnTerminate);
	}
	private void createPnlGenerals ()
	{
		String[] generals = {"Tipo", "Productor", "Director", "Estado"};
		ArrayList<String> generalsValues = new ArrayList<String>(values.get(0).subList(1, values.get(0).size()));

		pnlGenerals = new JPanel();
		pnlGenerals.setMaximumSize(new Dimension(1024, 32));

		prepareField(pnlGenerals, "Generales", generals, generalsValues);
	}
	private void createPnlRoles (TreeMap<String, String> roles)
	{
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		ArrayList<ArrayList<String>> rolesValues = new ArrayList<ArrayList<String>>(values.subList(1,
																					values.size()));
		ArrayList<ArrayList<String>> selection = new ArrayList<ArrayList<String>>();
		ArrayList<String> dummyRole = new ArrayList<String>();
		String role = (rolesValues.isEmpty()) ? null : rolesValues.get(0).get(0);

		dummyRole.add("");
		rolesValues.add(dummyRole);

		pnlRoles = new JPanel();
		pnlRoles.setLayout(new BoxLayout(pnlRoles, BoxLayout.Y_AXIS));
		pnlRoles.setBorder(BorderFactory.createTitledBorder(etched, "Roles"));

		if (!rolesValues.isEmpty() && role!=null)
		{
			for (ArrayList<String> roleValues : rolesValues) 
			{
				if ((roleValues.size() == 1 && !roleValues.get(0).equals(role)) || roleValues.equals(rolesValues.get(rolesValues.size() - 1)))
				{
					if (roleValues.size() > 1 && roleValues.equals(rolesValues.get(rolesValues.size() - 1)))
						selection.add(roleValues);

					prepareRole(role, selection);
					roles.put(role, ((selection.isEmpty()==true) ? "" : selection.get(0).get(0)));

					role = roleValues.get(0);
				
					selection = new ArrayList<ArrayList<String>>();
				}
				if (roleValues.size() > 1)
					selection.add(roleValues);
			}
		}
		else
		{
			pnlRoles.add(new JPanel());
		}
	}

	private void prepareField (JPanel pnlField, String title, String[] fields, ArrayList<String> values)
	{
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		JLabel label;

		pnlField.setBorder(BorderFactory.createTitledBorder(etched, title));
		
		for (int i = 0; i < fields.length; i++)
		{
			label = new JLabel("<html>" + fields[i] + ": <small>" + values.get(i) + "</small></html>");

			pnlField.add(Box.createHorizontalGlue());
			pnlField.add(Box.createHorizontalGlue());
			pnlField.add(label);
			pnlField.add(Box.createHorizontalGlue());
			pnlField.add(Box.createHorizontalGlue());
		}
	}
	private void prepareRole (String role, ArrayList<ArrayList<String>> selection)
	{
		JPanel pnlRoleWrapper = new JPanel();
		JPanel pnlRoleActions = new JPanel();
		JButton btnPreselection = new JButton("Preselección");
		JButton btnSelection = new JButton("Selección");
		SetWindow pnlRoleSelection = new SetWindow(role)
		{
			protected final int THUMBNAIL_MAX = 4;
			protected final int THUMBNAIL_COL = 4;

			protected void createPnlGrid ()
			{
				pnlGrid = new JPanel();
				pnlGrid.setLayout(new GridLayout(1, THUMBNAIL_COL, 4, 4));
			}

			public void createSet ()
			{
				createPnlGrid();
				createPnlNavigation();

				this.add(pnlGrid);
				this.add(pnlNavigation);

				this.setPreferredSize(new Dimension(720, 240));
			}

			public boolean initThumbnails (ArrayList<String> args)
			{
				if (selection.isEmpty())
					return false;

				thumbnails = masterFrame.makeTalentThumbnails(selection);
				indexAcum = 0;

				return true;
			}

			public void fillPnlGrid ()
			{
				super.fillPnlGrid();

				pnlGrid.setPreferredSize(pnlGrid.getPreferredSize());
			}
		};

		pnlRoleActions.setLayout(new BoxLayout(pnlRoleActions, BoxLayout.Y_AXIS));

		pnlRoleSelection.setToolTipText("Rol[Categoria]");

		btnPreselection.setToolTipText("Abrir preselección de talento");
		btnSelection.setToolTipText("Abrir selección de talento");

		if (selection.isEmpty())
			btnSelection.setEnabled(false);

		btnPreselection.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				masterFrame.displayTalentSearch(id, role.substring(0, role.indexOf("[")));
			}
		});
		btnSelection.addActionListener(new ActionListener()
		{
			private final String ROLE = role.substring(0, role.indexOf("["));
			private final ArrayList<ArrayList<String>> SELECTION = selection;

			public void actionPerformed (ActionEvent e)
			{
				select(ROLE, SELECTION);
			}
		});

		pnlRoleSelection.createSet();
		if (pnlRoleSelection.initThumbnails(null))
			pnlRoleSelection.fillPnlGrid();

		pnlRoleActions.add(btnPreselection);
		pnlRoleActions.add(btnSelection);

		pnlRoleWrapper.add(pnlRoleSelection);
		pnlRoleWrapper.add(pnlRoleActions);

		pnlRoles.add(pnlRoleWrapper);

		pnlRoles.setPreferredSize(pnlRoles.getPreferredSize());
	}

	private void delete ()
	{
		if ((new ConfirmationPopUp(masterFrame)).display("El proyecto será eliminado"))
		{
			if (CatalogsHandler.remove(id))
			{
				(new SuccessNotificationPopUp(masterFrame)).display("El proyecto se ha eliminado");
				masterFrame.previousWindow(this);
			}
			else
				(new FailureNotificationPopUp(masterFrame)).display("El proyecto no se ha eliminado");
		}
	}

	private void terminate ()
	{
		if ((new ConfirmationPopUp(masterFrame)).display("El proyecto será terminado"))
		{
			if (CatalogsHandler.terminate(id))
				(new SuccessNotificationPopUp(masterFrame)).display("El proyecto se ha terminado");
			else
				(new FailureNotificationPopUp(masterFrame)).display("El proyecto no se ha terminado");
		}
	}

	@SuppressWarnings("unchecked")
	private void select (String role, ArrayList<ArrayList<String>> selection)
	{
		String[] talents = new String[selection.size()];
		String[] ids = new String[selection.size()];
		String[] selected = new String[1];
		JPanel pnlTalent = new JPanel();
		JPanel pnlInnerTalent = new JPanel();
		JLabel lblTalent = new JLabel("Talento");
		JComboBox cbbTalent;

		for (int i = 0; i < selection.size(); i++)
		{
			ids[i] = selection.get(i).get(0);
			talents[i] = selection.get(i).get(2);
		}

		cbbTalent = new JComboBox(talents);

		pnlInnerTalent.add(lblTalent);
		pnlInnerTalent.add(cbbTalent);

		pnlTalent.add(pnlInnerTalent);

		selected = (new PetitionPopUp(masterFrame)).display(pnlTalent);

		if (selected[0]!=null)
		{
			if (CatalogsHandler.select(ids[cbbTalent.getSelectedIndex()], id, role))
				(new AccomplishmentNotificationPopUp(masterFrame)).display("¡El talento ha sido seleccionado!");
			else
				(new FailureNotificationPopUp(masterFrame)).display("La seleción ha fallado");	
		}
		else
			(new CancellationNotificationPopUp(masterFrame)).display("El talento no ha sido seleccionado");
	}

	private void present ()
	{
		JFileChooser fcsDirectory = new JFileChooser();
		int option;

		fcsDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		option = fcsDirectory.showDialog(masterFrame, "Elegir");

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean wasSuccessful;

			protected Void doInBackground ()
			{	
				wasSuccessful = CatalogsHandler.present(id, fcsDirectory.getSelectedFile().getAbsolutePath());	
				
				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if (wasSuccessful)
					(new AccomplishmentNotificationPopUp(masterFrame)).display("Listos para presentar, revise el directorio");
				else
					(new FailureNotificationPopUp(masterFrame)).display("Datos no listos, intentelo de nuevo");
			}
		};

		if (option == JFileChooser.APPROVE_OPTION)
		{
			worker.execute();

			masterFrame.startWaitingLayer();
		}
		else
			(new CancellationNotificationPopUp(masterFrame)).display("Ha cancelado la presentación");
	}
}