package castboard.dataaccess;

import castboard.domain.*;
import static castboard.domain.Enumeration.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

public class InterfaceDAL
{
	private Connection connection ;
	private static InterfaceDAL instance = null;
	private long accessId;
	
	private static final String DB_NAME = "CastBoardDB";
	
	private InterfaceDAL () 
	{
		connection = null;
	}
	
	public static InterfaceDAL getInstance ()
	{
		if (instance == null)
			instance = new InterfaceDAL();
			
		return instance;
	}
	
	public boolean connect (String user, String pass)
	{
		String url = "jdbc:derby://localhost:1527/.databases/" +
						 DB_NAME + ";user=" + user + ";password=" + 
						 pass + ";bootPassword=Un1v3rs3K3y;ssl=basic";
		Statement stm= null;
		CallableStatement statement = null;

		if (connection == null)				 
		{
			try
			{
				DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
				connection = DriverManager.getConnection(url);

				stm = connection.createStatement();
				stm.execute("SET ROLE agent");
				stm.execute("SET SCHEMA agent");

				statement = connection.prepareCall("{CALL dba.set_initaccesstime_sp(?, ?)}");
				statement.setString(1, user);
				statement.registerOutParameter(2, Types.BIGINT);
				statement.execute();

				accessId = statement.getLong(2);

				statement.close();
				stm.close();
				
				return true;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				connection = null;
				return false;
			}
		}
		else
			return true;
	}

	public ArrayList<Item> retriveSet ()
	{
		ArrayList<Item> items = new ArrayList<Item>();
		Statement statement = null;
		ResultSet result = null;

		Project project;
		Talent talent;

		String queryProject = "SELECT p.id, p.title, p.type, p.producer, p.state " +
							  "FROM Projects AS p, Items AS i " +
							  "WHERE p.id=i.id AND i.isSuppressed=false " +
							  "ORDER BY i.creationDate DESC";
		String queryStar = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
							  "FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
							  "WHERE t.id=i.id AND t.status='Estrella' AND (t.id=tp.talentId AND " +
							  "tp.name='Rostro') AND i.isSuppressed=false ORDER BY i.creationDate DESC";
		 String queryTalent = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
							  "FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
							  "WHERE t.id=i.id AND t.status='Talento' AND (t.id=tp.talentId AND " +
							  "tp.name='Rostro') AND i.isSuppressed=false ORDER BY i.creationDate DESC";
		/*String queryStar = "SELECT t.id, t.name, t.birthdate, t.profileType " +
							  "FROM Talents AS t, Items AS i " +
							  "WHERE t.id=i.id AND t.status='Estrella' AND i.isSuppressed=false " +
							  "ORDER BY i.creationDate DESC";
		String queryTalent = "SELECT t.id, t.name, t.birthdate, t.profileType " +
							  "FROM Talents AS t, Items AS i " +
							  "WHERE t.id=i.id AND t.status='Talento' AND i.isSuppressed=false " +
							  "ORDER BY i.creationDate DESC";*/

		try
		{
			statement = connection.createStatement();
			statement.setMaxRows(5);
			result = statement.executeQuery(queryProject);

			while(result.next())
			{
				project = new Project();

				project.setId(result.getLong(1));
				project.setTitle(result.getString(2));
				project.setType(Type.identifierOf(result.getString(3)));
				project.setProducer(result.getString(4));
				project.setState(State.identifierOf(result.getString(5)));

				items.add(project);
			}
			items.add(null);

			result = statement.executeQuery(queryStar);

			while(result.next())
			{
				talent = new Talent();

				talent.setId(result.getLong(1));
				talent.setPhotos(CatalogsHandler.parse(result.getBlob(2).getBinaryStream()));
				talent.setName(result.getString(3));
				talent.setBirthdate(new java.util.Date(result.getDate(4).getTime()));
				talent.setProfileType(ProfileType.identifierOf(result.getString(5)));

				items.add(talent);
			}

			statement.setMaxRows(10);
			result = statement.executeQuery(queryTalent);

			while(result.next())
			{
				talent = new Talent();

				talent.setId(result.getLong(1));
				talent.setPhotos(CatalogsHandler.parse(result.getBlob(2).getBinaryStream()));
				talent.setName(result.getString(3));
				talent.setBirthdate(new java.util.Date(result.getDate(4).getTime()));
				talent.setProfileType(ProfileType.identifierOf(result.getString(5)));

				items.add(talent);
			}

			statement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return items;
	}
	public ArrayList<Talent> retriveSet (Talent profile)
	{
		ArrayList<Talent> talents = new ArrayList<Talent>();
		Statement statement = null;
		ResultSet result = null;

		Talent talent;

		if (profile == null)
		{
			String queryStar = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
								  "FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
								  "WHERE t.id=i.id AND t.status='Estrella' AND (t.id=tp.talentId AND" +
								  " tp.name='Rostro') AND i.isSuppressed=false ORDER BY i.creationDate DESC";
			String queryTalent = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
								  "FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
								  "WHERE t.id=i.id AND t.status='Talento' AND (t.id=tp.talentId AND " +
								  "tp.name='Rostro') AND i.isSuppressed=false ORDER BY i.creationDate DESC";
		/*	String queryStar = "SELECT t.id, t.name, t.birthdate, t.profileType " +
								  "FROM Talents AS t, Items AS i " +
								  "WHERE t.id=i.id AND t.status='Estrella' AND i.isSuppressed=false " +
								  "ORDER BY i.creationDate DESC";
			String queryTalent = "SELECT t.id, t.name, t.birthdate, t.profileType " +
								  "FROM Talents AS t, Items AS i " +
								  "WHERE t.id=i.id AND t.status='Talento' AND i.isSuppressed=false " +
								  "ORDER BY i.creationDate DESC";*/

			try
			{
				statement = connection.createStatement();
				result = statement.executeQuery(queryStar);

				while(result.next())
				{
					talent = new Talent();

					talent.setId(result.getLong(1));
					talent.setPhotos(CatalogsHandler.parse(result.getBlob(2).getBinaryStream()));
					talent.setName(result.getString(3));
					talent.setBirthdate(new java.util.Date(result.getDate(4).getTime()));
					talent.setProfileType(ProfileType.identifierOf(result.getString(5)));

					talents.add(talent);
				}

				result = statement.executeQuery(queryTalent);

				while(result.next())
				{
					talent = new Talent();

					talent.setId(result.getLong(1));
					talent.setPhotos(CatalogsHandler.parse(result.getBlob(2).getBinaryStream()));
					talent.setName(result.getString(3));
					talent.setBirthdate(new java.util.Date(result.getDate(4).getTime()));
					talent.setProfileType(ProfileType.identifierOf(result.getString(5)));

					talents.add(talent);
				}

				statement.close();
				result.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return talents;		
	}
	public ArrayList<Project> retriveSet (Project criteria)
	{
		ArrayList<Project> projects = new ArrayList<Project>();
		Statement statement = null;
		ResultSet result = null;

		Project project;

		if (criteria == null)
		{
			String queryProject = "SELECT p.id, p.title, p.type, p.producer, p.state " +
								  "FROM Projects AS p, Items AS i " +
								  "WHERE p.id=i.id AND i.isSuppressed=false " +
								  "ORDER BY i.creationDate DESC";

			try
			{
				statement = connection.createStatement();
				result = statement.executeQuery(queryProject);

				while(result.next())
				{
					project = new Project();

					project.setId(result.getLong(1));
					project.setTitle(result.getString(2));
					project.setType(Type.identifierOf(result.getString(3)));
					project.setProducer(result.getString(4));
					project.setState(State.identifierOf(result.getString(5)));

					projects.add(project);
				}

				statement.close();
				result.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return projects;
	}

	private boolean isServerStarted ()
	{
		try
		{
			(new org.apache.derby.drda.NetworkServerControl()).ping();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public void close ()
	{
		CallableStatement statement = null;

		try
		{
			statement = connection.prepareCall("{CALL dba.set_finalaccesstime_sp(?)}");
			statement.setLong(1, accessId);
			statement.execute();

			statement.close();
			connection.close();
		}
		catch (SQLException e)
		{
			CatalogsHandler.notifyException(e);
			e.printStackTrace();
		}

		instance = null;
	}
}