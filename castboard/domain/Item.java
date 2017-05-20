package castboard.domain;

import static castboard.domain.Enumeration.*;

import java.time.LocalDateTime;

public abstract class Item
{
	private long id;
	private LocalDateTime creationDate;
	
	public Item () {}
	
	public void setId (long id)
	{
		this.id = id;
	}
	public void setCreationDate (LocalDateTime creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public long getId ()
	{
		return id;
	}
	public LocalDateTime getCreationDate ()
	{
		return creationDate;
	}
}