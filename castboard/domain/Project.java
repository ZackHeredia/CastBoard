package castboard.domain;

import static castboard.domain.Enumeration.*;

import java.util.TreeMap;
import java.util.ArrayList;

public class Project extends Item
{
	private String title;
	private Type type;
	private String producer;
	private String director;
	private TreeMap<Category, String> roles;
	private TreeMap<String, ArrayList<Talent>> preselecteds;
	private TreeMap<String, Talent> selecteds;
	private State state;

	public Project () {}

	public void setTitle (String title)
	{
		this.title = title;
	}
	public void setType (Type type)
	{
		this.type = type;
	}
	public void setProducer (String producer)
	{
		this.producer = producer;
	}
	public void setDirector (String director)
	{
		this.director = director;
	}
	public void setRoles (TreeMap<Category, String> roles)
	{
		this.roles = roles;
	}
	public void setPreselecteds (TreeMap<String, ArrayList<Talent>> preselecteds)
	{
		this.preselecteds = preselecteds;
	}
	public void setSelecteds (TreeMap<String, Talent> selecteds)
	{
		this.selecteds = selecteds;
	}
	public void setState (State state)
	{
		this.state = state;
	}

	public String getTitle ()
	{
		return title;
	}
	public Type getType ()
	{
		return type;
	}
	public String getProducer ()
	{
		return producer;
	}
	public String getDirector ()
	{
		return director;
	}
	public TreeMap<Category, String> getRoles ()
	{
		return roles;
	}
	public TreeMap<String, ArrayList<Talent>> getPreselecteds ()
	{
		return preselecteds;
	}
	public TreeMap<String, Talent> getSelecteds ()
	{
		return selecteds;
	}
	public State getState ()
	{
		return state;
	}
}