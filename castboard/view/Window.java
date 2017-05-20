package castboard.view;

import javax.swing.JPanel;

public abstract class Window extends JPanel
{
	protected MasterFrame masterFrame;

	protected abstract void init ();

	public void reload ()
	{
		this.removeAll();
		this.revalidate();

		init();
		this.repaint();
	}
	public void close () {}
}