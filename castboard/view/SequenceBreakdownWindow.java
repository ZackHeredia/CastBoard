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
import java.util.TreeMap;

public class SequenceBreakdownWindow extends Window
{
	private JPanel pnlTitle;
	private JPanel pnlActions;
	private SetWindow pnlSequences;
	private ArrayList<ArrayList<String>> values;
	private String id;
	private TreeMap<String, String> roles;

	public SequenceBreakdownWindow (String id, TreeMap<String, String> roles)
	{
		masterFrame = MasterFrame.getInstance();
		this.id = id;
		this.roles = roles;
		
		init();
	}

	protected void init ()
	{
		SequenceBreakdownWindow breakdown = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				values = CatalogsHandler.get(id, CatalogsHandler.CINEMA_SET);
				
				createPnlTitle();
				createPnlActions(id, roles);
				createPnlSequences(values.get(0).get(0));

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				breakdown.setLayout(new BoxLayout(breakdown, BoxLayout.Y_AXIS));

				breakdown.add(pnlTitle);
				breakdown.add(pnlActions);
				breakdown.add(pnlSequences);

				breakdown.validate();
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
	private void createPnlActions (String id, TreeMap<String, String> roles)
	{
		JButton btnCreateSequence = new JButton("Crear secuencia");

		pnlActions = new JPanel();
		pnlActions.setMaximumSize(new Dimension(1024, 32));

		btnCreateSequence.setToolTipText("Crear nueva secuencia");

		btnCreateSequence.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				masterFrame.displaySequenceEntry(id, roles);
			}
		});

		pnlActions.add(btnCreateSequence);
	}
	private void createPnlSequences (String title)
	{
		ArrayList<ArrayList<String>> sequences = new ArrayList<ArrayList<String>>(values.subList(1, values.size()));

		pnlSequences = new SetWindow("Secuencias")
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
				if (sequences.isEmpty())
					return false;

				thumbnails = masterFrame.makeSequenceThumbnails(sequences, title);
				indexAcum = 0;

				return true;
			}

			public void fillPnlGrid ()
			{
				super.fillPnlGrid();
			}
		};

		pnlSequences.createSet();
		if (pnlSequences.initThumbnails(null))
			pnlSequences.fillPnlGrid();
	}
}