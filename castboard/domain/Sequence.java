package castboard.domain;

import static castboard.domain.Enumeration.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.TreeMap;

public class Sequence extends Item
{
	private int number;
	private String description;
	private Date filmingDate;
	private LocationType locationType;
	private String location;
	private DayMoment dayMoment;
	private int scriptPage;
	private int scriptDay;
	private TreeMap<String, Talent> selecteds;

	public Sequence () {}

	public void setNumber (int number)
	{
		this.number = number;
	}
	public void setDescription (String description)
	{
		this.description = description;
	}
	public void setFilmingDate (Date filmingDate)
	{
		this.filmingDate = filmingDate;
	}
	public void setLocationType (LocationType locationType)
	{
		this.locationType = locationType;
	}
	public void setLocation (String location)
	{
		this.location = location;
	}
	public void setDayMoment (DayMoment dayMoment)
	{
		this.dayMoment = dayMoment;
	}
	public void setScriptPage (int scriptPage)
	{
		this.scriptPage = scriptPage;
	}
	public void setScriptDay (int scriptDay)
	{
		this.scriptDay = scriptDay;
	}
	public void setSelecteds (TreeMap<String, Talent> selecteds)
	{
		this.selecteds = selecteds;
	}

	public int getNumber ()
	{
		return number;
	}
	public String getDescription ()
	{
		return description;
	}
	public Date getFilmingDate ()
	{
		return filmingDate;
	}
	public LocationType getLocationType ()
	{
		return locationType;
	}
	public String getLocation ()
	{
		return location;
	}
	public DayMoment getDayMoment ()
	{
		return dayMoment;
	}
	public int getScriptPage ()
	{
		return scriptPage;
	}
	public int getScriptDay ()
	{
		return scriptDay;
	}
	public TreeMap<String, Talent> getSelecteds ()
	{
		return selecteds;
	}
}