package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
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

public class ProjectDetailWindow extends JPanel
{
	private MasterFrame masterFrame;
	private JPanel pnlTitle;
	private JPanel pnlActions;
	private JPanel pnlGenerals;
	private JPanel pnlRoles;
	private ArrayList<ArrayList<String>> values;
	private JPanel pnlLargerMedia;
	private JPanel pnlMedia;

	public ProjectDetailWindow (String id)
	{
		masterFrame = MasterFrame.getInstance();
		ProjectDetailWindow detail = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				values = CatalogsHandler.get(id, CatalogsHandler.PROJECT_SET);
				
				createPnlTitle();
				createPnlActions();
				createPnlGenerals();
				createPnlRoles();

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
	private void createPnlActions ()
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

		btnUpdate.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//masterFrame.displayTalentUpdate(values);
			}
		});
		btnDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//delete();
			}
		});
		btnPresent.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//present();
			}
		});
		btnBreakdown.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//switchStatus();
			}
		});
		btnTerminate.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//switchStatus();
			}
		});

		pnlActions.add(btnUpdate);
		pnlActions.add(btnDelete);
		pnlActions.add(btnPresent);
		if (values.get(0).get(1).equals("Cinematografico"))
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
	private void createPnlRoles ()
	{
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		ArrayList<ArrayList<String>> rolesValues = new ArrayList<ArrayList<String>>(values.subList(1, values.size()));
		ArrayList<ArrayList<String>> selection = new ArrayList<ArrayList<String>>();
		String role = (rolesValues.isEmpty()) ? null : rolesValues.get(0).get(0);
		
		pnlRoles = new JPanel();
		pnlRoles.setLayout(new BoxLayout(pnlRoles, BoxLayout.Y_AXIS));
		pnlRoles.setBorder(BorderFactory.createTitledBorder(etched, "Roles"));

		if (!rolesValues.isEmpty())
		{
			for (ArrayList<String> roleValues : rolesValues) 
			{
				if ((roleValues.size() == 1 && !roleValues.get(0).equals(role)) || roleValues.equals(rolesValues.get(rolesValues.size() - 1)))
				{
					if (roleValues.size() > 1 && roleValues.equals(rolesValues.get(rolesValues.size() - 1)))
						selection.add(roleValues);

					prepareRole(role, selection);

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
			protected final int THUMBNAIL_MAX = 15;
			protected final int THUMBNAIL_COL = 4;

			protected void createPnlGrid ()
			{
				pnlGrid = new JPanel();
				pnlGrid.setLayout(new GridLayout(0, THUMBNAIL_COL, 4, 4));
			}

			public void createSet ()
			{
				createPnlGrid();
				createPnlNavigation();

				this.setPreferredSize(new Dimension(132, 196));

				this.add(pnlGrid);
				this.add(pnlNavigation);
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
			}
		};

		pnlRoleActions.setLayout(new BoxLayout(pnlRoleActions, BoxLayout.Y_AXIS));

		pnlRoleSelection.setToolTipText("Rol[Categoria]");

		btnPreselection.setToolTipText("Abrir preselección de talento");
		btnSelection.setToolTipText("Abrir selección de talento");

		btnPreselection.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//preselection();
			}
		});
		btnSelection.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				//selection(selection);
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
	}
}