package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.SwingWorker;
import java.util.ArrayList;

public class TalentSetWindow extends SetWindow
{
	public TalentSetWindow ()
	{
		super("Talentos");
		TalentSetWindow talentSet = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				createPnlGrid();
				createPnlNavigation();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				talentSet.add(pnlGrid);
				talentSet.add(pnlNavigation);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	public void createPnlGrid ()
	{
		ArrayList<ArrayList<String>> talents = CatalogsHandler.getSet(CatalogsHandler.TALENT_SET);
		thumbnails = masterFrame.makeTalentThumbnails(talents);

		super.createPnlGrid();		
	}
}