package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProjectSearchWindow extends Window
{
	private JPanel pnlSearchForm;
	private SetWindow pnlMatches;
	private JTextField txtTitle;
	private JComboBox cbbType;
	private JTextField txtProducer;
	private JTextField txtDirector;

	public ProjectSearchWindow ()
	{
		masterFrame = MasterFrame.getInstance();
		
		init();
	}

	protected void init ()
	{
		ProjectSearchWindow search = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				createPnlSearchForm();
				createPnlMatches();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				search.setLayout(new BoxLayout(search, BoxLayout.Y_AXIS));

				search.add(pnlSearchForm);
				search.add(Box.createVerticalGlue());
				search.add(pnlMatches);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}
	
	@SuppressWarnings("unchecked")
	private void createPnlSearchForm ()
	{
		JButton btnSearch = new JButton("Buscar");
		JLabel lblTitle = new JLabel("Titulo");
		JLabel lblType = new JLabel("Tipo");
		JLabel lblProducer = new JLabel("Productor");
		JLabel lblDirector = new JLabel("Director");
		JPanel pnlForm = new JPanel();
		JPanel pnlTitle = new JPanel();
		JPanel pnlType = new JPanel();
		JPanel pnlProducer = new JPanel();
		JPanel pnlDirector = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Criterio buscado";
		String[] types = {"Comercial", "Fotografico", "Cinematografico"};

		txtTitle = new JTextField(16);
		cbbType = new JComboBox(types);
		txtProducer = new JTextField(16);
		txtDirector = new JTextField(16);

		pnlSearchForm = new JPanel();
		pnlSearchForm.setBorder(BorderFactory.createTitledBorder(etched, title));
		pnlSearchForm.setLayout(new BoxLayout(pnlSearchForm, BoxLayout.Y_AXIS));

		btnSearch.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				search();
			}
		});

		pnlTitle.add(lblTitle);
		pnlTitle.add(txtTitle);

		pnlType.add(lblType);
		pnlType.add(cbbType);

		pnlProducer.add(lblProducer);
		pnlProducer.add(txtProducer);

		pnlDirector.add(lblDirector);
		pnlDirector.add(txtDirector);

		pnlTitle.setToolTipText("Escriba el titulo del proyecto buscado");
		pnlType.setToolTipText("Seleccione el tipo del proyecto buscado");
		pnlProducer.setToolTipText("Escriba el productor del proyecto buscado");
		pnlDirector.setToolTipText("Escriba el director del proyecto buscado");

		pnlForm.add(pnlTitle);
		pnlForm.add(pnlType);
		pnlForm.add(pnlProducer);
		pnlForm.add(pnlDirector);

		pnlSearchForm.add(pnlForm);
		pnlSearchForm.add(btnSearch);
	}
	private void createPnlMatches ()
	{
		pnlMatches = new SetWindow("Proyectos")
		{
			protected final int THUMBNAIL_MAX = 15;

			protected void createPnlGrid ()
			{
				pnlGrid = new JPanel();
				pnlGrid.setLayout(new GridLayout(0, 5, 4, 4));
			}

			public void createSet ()
			{
				createPnlGrid();
				createPnlNavigation();

				this.add(pnlGrid);
				this.add(pnlNavigation);
			}

			public boolean initThumbnails (ArrayList<String> args)
			{
				ArrayList<ArrayList<String>> projects = CatalogsHandler.search(args, CatalogsHandler.PROJECT_SET);

				if (projects.isEmpty())
					return false;

				thumbnails = masterFrame.makeProjectThumbnails(projects);
				indexAcum = 0;

				return true;
			}

			public void fillPnlGrid ()
			{
				super.fillPnlGrid();
			}
		};

		pnlMatches.createSet();
	}

	private void search ()
	{
		ArrayList<String> args = new ArrayList<String>();
		ProjectSearchWindow search = this;

		args.add(txtTitle.getText());
		args.add((String) cbbType.getSelectedItem());
		args.add(txtProducer.getText());
		args.add(txtDirector.getText());

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean matchesFound;

			protected Void doInBackground ()
			{
				matchesFound = pnlMatches.initThumbnails(args);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(matchesFound)
					pnlMatches.fillPnlGrid();
				else
					(new NoMatchesNotificationPopUp(masterFrame)).display("No hay proyectos coincidentes");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}
}