package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.SwingWorker;
import java.util.ArrayList;

public class ProjectSetWindow extends SetWindow
{
	public ProjectSetWindow ()
	{
		super("Proyectos");
		
		init();
	}

	protected void init ()
	{
		indexAcum = 0;
		ProjectSetWindow projectSet = this;

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

				projectSet.add(pnlGrid);
				projectSet.add(pnlNavigation);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	protected void createPnlGrid ()
	{
		ArrayList<ArrayList<String>> projects = CatalogsHandler.getSet(CatalogsHandler.PROJECT_SET);
		thumbnails = masterFrame.makeProjectThumbnails(projects);

		super.createPnlGrid();		
	}
}