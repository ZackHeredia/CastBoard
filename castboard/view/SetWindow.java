package castboard.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class SetWindow extends JPanel
{
	protected MasterFrame masterFrame;
	protected JPanel pnlGrid;
	protected JPanel pnlNavigation;
	protected ArrayList<JButton> thumbnails;

	protected int indexAcum;
	protected final int THUMBNAIL_MAX = 20;

	public SetWindow (String title)
	{
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder(etched, title));

		masterFrame = MasterFrame.getInstance();
		indexAcum = 0;
	}

	protected void createPnlGrid ()
	{
		pnlGrid = new JPanel();
		pnlGrid.setLayout(new GridLayout(0, 5, 4, 4));

		fillPnlGrid();
	}
	protected void createPnlNavigation ()
	{
		BasicArrowButton btnBack = new BasicArrowButton(BasicArrowButton.WEST, (new Color(0, 146, 182)),
																			   (new Color(0, 125, 156)),
																			   (new Color(0, 104, 130)),
																			   (new Color(0, 166, 207)));
		BasicArrowButton btnForward = new BasicArrowButton(BasicArrowButton.EAST, (new Color(0, 146, 182)),
																				  (new Color(0, 125, 156)),
																				  (new Color(0, 104, 130)),
																				  (new Color(0, 166, 207)));
		JPanel pnlButton = new JPanel();

		pnlNavigation = new JPanel();
		pnlNavigation.setLayout(new BorderLayout());

		btnBack.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				goBack();
			}
		});
		btnForward.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				goForward();
			}
		});

		btnBack.setToolTipText("Ver anteriores");
		btnForward.setToolTipText("Ver siguientes");

		pnlButton.add(btnBack);
		pnlButton.add(btnForward);

		pnlNavigation.add(pnlButton);
	}

	protected void fillPnlGrid ()
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

			if ((counter >= 1) && (counter % 5) != 0)
			{
				for (int i = counter; (i % 5) != 0; i++)
				{
					pnlGrid.add(Box.createRigidArea(new Dimension(132, 196)));
				}
			}
		}
		finally
		{
			indexAcum += counter;

			pnlGrid.revalidate();
			this.repaint();
		}
	}

	protected void goBack ()
	{
		if (indexAcum > THUMBNAIL_MAX)
		{
			if ((indexAcum % THUMBNAIL_MAX) > 0)
				indexAcum -= ((indexAcum % THUMBNAIL_MAX) + THUMBNAIL_MAX);
			else
				indexAcum -= THUMBNAIL_MAX;

			fillPnlGrid();
			masterFrame.scrollTop();
		}
		else
			(new FailureNotificationPopUp(masterFrame)).display("Ha llegado al principio del catálogo");
	}
	protected void goForward ()
	{
		if (indexAcum < thumbnails.size())
		{
			fillPnlGrid();
			masterFrame.scrollTop();
		}
		else
			(new FailureNotificationPopUp(masterFrame)).display("Ha llegado al final del catálogo");
	}

	public void createSet () {}
	public boolean initThumbnails (ArrayList<String> args) {return false;}
}