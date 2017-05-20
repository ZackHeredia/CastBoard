package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import javax.swing.SwingWorker;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.util.ArrayList;

public class FrontWindow extends Window
{
	JPanel pnlProject;
	JPanel pnlTalent;

	public FrontWindow ()
	{
		masterFrame = MasterFrame.getInstance();
		
		init();
	}

	protected void init ()
	{
		FrontWindow front = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{

			protected Void doInBackground ()
			{
				createPnlProject();
				createPnlTalent();

				return null;
			}
			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				front.setLayout(new BoxLayout(front, BoxLayout.Y_AXIS));

				front.add(pnlProject);
				front.add(Box.createVerticalGlue());
				front.add(pnlTalent);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlProject ()
	{
		JPanel pnlGrid = new JPanel();
		JLabel lblLink = new JLabel("\u25B8Catálogo Completo");
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Proyectos";
		ArrayList<ArrayList<String>> items = CatalogsHandler.getSet(CatalogsHandler.FRONT_SET);
		int toIndex = items.indexOf(null);
		ArrayList<ArrayList<String>> projects;
		ArrayList<JButton> thumbnails;
		int counter = 0;

		pnlProject = new JPanel();

		lblLink.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				masterFrame.displayProjectSet();
			}

			public void mouseEntered (MouseEvent e)
			{
				JLabel lblLink = (JLabel) e.getSource();

				lblLink.setForeground(new Color(210, 210, 210));
			}

			public void mouseExited (MouseEvent e)
			{
				JLabel lblLink = (JLabel) e.getSource();

				lblLink.setForeground(new Color(0, 146, 182));
			}
		});
		masterFrame.styleLink(lblLink);

		pnlProject.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlGrid.setLayout(new GridLayout(1, 5, 4, 4));
		pnlProject.setLayout(new BorderLayout());
		
		projects = new ArrayList<ArrayList<String>>(items.subList(0, toIndex));

		thumbnails = masterFrame.makeProjectThumbnails(projects);
		for (JButton thumbnail : thumbnails)
		{
			pnlGrid.add(thumbnail);
			counter++;
		}

		if (counter < 5) 
		{
			for (int i = counter; i < 5; i++)
			{
				pnlGrid.add(Box.createRigidArea(new Dimension(132, 196)));
			}
		}

		lblLink.setToolTipText("Ver catálogo de proyectos");

		pnlProject.add(pnlGrid, BorderLayout.NORTH);
		pnlProject.add(lblLink, BorderLayout.SOUTH);
	}
	private void createPnlTalent ()
	{
		JPanel pnlGrid = new JPanel();
		JLabel lblLink = new JLabel("\u25B8Catálogo Completo");
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Talentos";
		ArrayList<ArrayList<String>> items = CatalogsHandler.getSet(CatalogsHandler.FRONT_SET);
		int fromIndex = items.indexOf(null) + 1;
		int toIndex = items.size();
		ArrayList<ArrayList<String>> talents;
		ArrayList<JButton> thumbnails;
		int counter = 0;

		pnlTalent = new JPanel();

		lblLink.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				masterFrame.displayTalentSet();
			}

			public void mouseEntered (MouseEvent e)
			{
				JLabel lblLink = (JLabel) e.getSource();

				lblLink.setForeground(new Color(210, 210, 210));
			}

			public void mouseExited (MouseEvent e)
			{
				JLabel lblLink = (JLabel) e.getSource();

				lblLink.setForeground(new Color(0, 146, 182));
			}
		});
		masterFrame.styleLink(lblLink);

		pnlTalent.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlGrid.setLayout(new GridLayout(3, 5, 4, 4));
		pnlTalent.setLayout(new BorderLayout());
		
		talents = new ArrayList<ArrayList<String>>(items.subList(fromIndex, toIndex));

		thumbnails = masterFrame.makeTalentThumbnails(talents);
		for (JButton thumbnail : thumbnails)
		{
			pnlGrid.add(thumbnail);
			counter++;
		}

		lblLink.setToolTipText("Ver catálogo de talentos");

		if (counter < 15) 
		{
			for (int i = counter; i < 15; i++)
			{
				pnlGrid.add(Box.createRigidArea(new Dimension(132, 196)));
			}
		}

		pnlTalent.add(pnlGrid, BorderLayout.NORTH);
		pnlTalent.add(lblLink, BorderLayout.SOUTH);
	}
}