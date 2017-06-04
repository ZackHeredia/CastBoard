package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JButton;
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

public class SequenceDetailWindow extends Window
{
	private JPanel pnlHead;
	private JPanel pnlContent;
	private JPanel pnlRoles;
	private ArrayList<ArrayList<String>> values;
	private String id;
	private String title;

	public SequenceDetailWindow (String id, String title)
	{
		this.id = id;
		this.title = title;
		
		masterFrame = MasterFrame.getInstance();
		
		init();
	}

	protected void init ()
	{
		SequenceDetailWindow detail = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				values = CatalogsHandler.get(id, CatalogsHandler.SEQUENCE_SET);
				
				createPnlHead(title);
				createPnlContent();
				createPnlRoles();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));

				detail.add(pnlHead);
				detail.add(pnlContent);
				detail.add(pnlRoles);
				detail.add(Box.createRigidArea(new Dimension(700, 700)));

				detail.validate();
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlHead (String title)
	{
		JLabel lblTitle = new JLabel("<html><h1>" + title + "</h1></html>");
		JLabel lblNumber = new JLabel("<html><h2>Secuencia No. " + values.get(0).get(0) + "</h2></html>");
		JButton btnCapture = new JButton("Capturar");
		JPanel innerHead = new JPanel();
		
		pnlHead =new JPanel();
		pnlHead.setLayout(new BoxLayout(pnlHead, BoxLayout.Y_AXIS));
		
		lblTitle.setForeground(new Color(0, 146, 182));

		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		innerHead.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnCapture.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				capture();
			}
		});

		lblTitle.setToolTipText("Titulo");
		lblNumber.setToolTipText("Número");

		innerHead.add(lblNumber);
		innerHead.add(Box.createHorizontalStrut(32));
		innerHead.add(btnCapture);

		pnlHead.add(lblTitle);
		pnlHead.add(innerHead);
	}
	private void createPnlContent ()
	{
		JPanel pnlGenerals = new JPanel();
		JPanel pnlScript = new JPanel();
		JPanel pnlInnerGenerals = new JPanel();
		JPanel pnlInnerScript = new JPanel();
		JLabel lblFilmingDate = new JLabel("<html>Fecha de rodaje: <small>" + values.get(0).get(2) + "</small></html>");
		JLabel lblLocation = new JLabel("<html>Locación: <small>" + values.get(0).get(3) + "</small></html>");
		JLabel lblScriptPage = new JLabel("<html>Página: <small>" + values.get(0).get(6) + "</small></html>");
		JLabel lblScriptDay = new JLabel("<html>Dia: <small>" + values.get(0).get(7) + "</small></html>");
		String[] description = {"Descripción"};
		String[] script = {"Tipo de locación", "Momento del dia"};
		ArrayList<String> descriptionValue = new ArrayList<String>(values.get(0).subList(1, 2));
		ArrayList<String> scriptValues = new ArrayList<String>(values.get(0).subList(4, 6));

		pnlContent = new JPanel();

		pnlInnerGenerals.setLayout(new GridLayout(1, 2));
		pnlInnerScript.setLayout(new GridLayout(1, 2));

		pnlInnerGenerals.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlInnerScript.setAlignmentX(Component.LEFT_ALIGNMENT);

		pnlInnerGenerals.add(lblFilmingDate);
		pnlInnerGenerals.add(lblLocation);

		pnlInnerScript.add(lblScriptPage);
		pnlInnerScript.add(lblScriptDay);

		pnlScript.add(pnlInnerScript);
		prepareField(pnlScript, "Guión", script, scriptValues);

		prepareField(pnlGenerals, "Generales", description, descriptionValue);
		pnlGenerals.add(pnlInnerGenerals);

		pnlGenerals.setPreferredSize(new Dimension(644, 68));
		pnlScript.setPreferredSize(new Dimension(344, 68));

		pnlContent.add(pnlGenerals);
		pnlContent.add(pnlScript);
	}
	private void createPnlRoles ()
	{
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		ArrayList<ArrayList<String>> rolesValues = new ArrayList<ArrayList<String>>(values.subList(1, values.size()));
		ArrayList<ArrayList<String>> selection = new ArrayList<ArrayList<String>>();
		String role = (rolesValues.isEmpty()) ? null : rolesValues.get(0).get(0);
		
		pnlRoles = new JPanel();
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
		Border etched;
		JLabel label;

		pnlField.setLayout(new BoxLayout(pnlField, BoxLayout.Y_AXIS));

		if (title != null)
		{
			etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
			pnlField.setBorder(BorderFactory.createTitledBorder(etched, title));
		}

		for (int i = 0; i < fields.length; i++)
		{
			label = new JLabel("<html>" + fields[i] + ": <small>" + values.get(i) + "</small></html>");

			label.setAlignmentX(Component.LEFT_ALIGNMENT);

			pnlField.add(label);
		}
	}
	private void prepareRole (String role, ArrayList<ArrayList<String>> selection)
	{
		SetWindow pnlRoleSelection = new SetWindow(role)
		{
			protected final int THUMBNAIL_MAX = 1;
			protected final int THUMBNAIL_COL = 1;

			protected void createPnlGrid ()
			{
				pnlGrid = new JPanel();
				pnlGrid.setLayout(new GridLayout(1, THUMBNAIL_COL, 4, 4));
			}

			public void createSet ()
			{
				createPnlGrid();
				createPnlNavigation();

				this.setPreferredSize(new Dimension(164, 240));

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
				int counter = 0;

				pnlGrid.removeAll();

				try
				{
					for (int i = indexAcum; i < (indexAcum + THUMBNAIL_MAX); i++)
					{
						pnlGrid.add(thumbnails.get(i));
						counter++;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace(); 
				}
				finally
				{
					indexAcum += counter;

					pnlGrid.revalidate();
					this.repaint();
				}

				pnlGrid.setPreferredSize(pnlGrid.getPreferredSize());
			}
		};

		pnlRoleSelection.setToolTipText("Rol[Categoria]");

		pnlRoleSelection.createSet();
		if (pnlRoleSelection.initThumbnails(null))
			pnlRoleSelection.fillPnlGrid();

		pnlRoles.add(pnlRoleSelection);
	}

	private void capture ()
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
				wasSuccessful = CatalogsHandler.capture(title, values.get(0).get(0), fcsDirectory.getSelectedFile().getAbsolutePath(), masterFrame.getBounds());	
				
				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if (wasSuccessful)
					(new AccomplishmentNotificationPopUp(masterFrame)).display("Secuencia capturada, revise el directorio");
				else
					(new FailureNotificationPopUp(masterFrame)).display("Secuencia no lista, intentelo de nuevo");
			}
		};

		if (option == JFileChooser.APPROVE_OPTION)
		{
			worker.execute();

			try
			{
				Thread.sleep(2000);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			masterFrame.startWaitingLayer();
		}
		else
			(new CancellationNotificationPopUp(masterFrame)).display("Ha cancelado la captura");
	}
}