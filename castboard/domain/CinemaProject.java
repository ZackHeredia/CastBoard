package castboard.domain;

import java.util.ArrayList;

public class CinemaProject extends Project
{
	private ArrayList<Sequence> sequences;

	public CinemaProject () {}

	public void setSequences (ArrayList<Sequence> sequences)
	{
		this.sequences = sequences;
	}

	public ArrayList<Sequence> getSequences ()
	{
		return sequences;
	}
}