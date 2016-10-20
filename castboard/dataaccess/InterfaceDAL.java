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
						   		 "tp.name='Rostro') AND i.isSuppressed=false " +
						   "ORDER BY i.creationDate DESC";
		 String queryTalent = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
							  "FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
							  "WHERE t.id=i.id AND t.status='Talento' AND (t.id=tp.talentId AND " +
							  		"tp.name='Rostro') AND i.isSuppressed=false " +
							  "ORDER BY i.creationDate DESC";

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
				talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(2).getBinaryStream()));
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
				talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(2).getBinaryStream()));
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
		PreparedStatement pStatement = null;
		ResultSet result = null;
		String subquery = null;
		String queryStar = null;
		String queryTalent = null;

		Talent talent;

		if (profile == null)
		{
			queryStar = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
						"FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
						"WHERE t.id=i.id AND t.status='Estrella' AND (t.id=tp.talentId AND" +
							 " tp.name='Rostro') AND i.isSuppressed=false " +
						"ORDER BY i.creationDate DESC";
			queryTalent = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
						  "FROM Talents AS t, TalentsPhotos AS tp, Items AS i " +
						  "WHERE t.id=i.id AND t.status='Talento' AND (t.id=tp.talentId AND" +
							   " tp.name='Rostro') AND i.isSuppressed=false " +
						  "ORDER BY i.creationDate DESC";

			try
			{
				statement = connection.createStatement();
				result = statement.executeQuery(queryStar);

				while(result.next())
				{
					talent = new Talent();

					talent.setId(result.getLong(1));
					talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(2).getBinaryStream()));
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
					talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(2).getBinaryStream()));
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
		else
		{
			subquery = "SELECT tas.skill " +
					   "FROM TalentsArtisticSkills AS tas " +
					   "WHERE tas.talentId=t.id AND tas.skill IN (?, ?, ?, ?, ?)";
			queryStar = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
					    "FROM Talents AS t LEFT JOIN Items AS i ON t.id=i.id " + 
					    	 "LEFT JOIN TalentsPhotos AS tp ON t.id=tp.talentId " + 
					    	 "LEFT JOIN TalentsAddresses AS ta ON t.id=ta.talentId " + 
					    "WHERE t.status='Estrella' AND i.isSuppressed=false AND tp.name='Rostro' AND" +
					    	 " t.sex=? AND (t.physique=? OR t.hairTexture=? OR t.hairColor=? OR " + 
					    	  "t.skinTone=? OR t.eyeColor=? OR t.stature=? OR t.profileType=? OR " +
					 	 	  "ta.city=? OR (CAST(t.birthdate AS TIMESTAMP) BETWEEN " +
					 	 	  "{fn TIMESTAMPADD(SQL_TSI_YEAR, -5, CAST(? AS TIMESTAMP))} AND " +
					 	 	  "{fn TIMESTAMPADD(SQL_TSI_YEAR, 5, CAST(? AS TIMESTAMP))}) OR " +
							  "EXISTS(" + subquery + ")) " +
					 	"ORDER BY i.creationDate DESC";
			queryTalent = "SELECT t.id, tp.photo, t.name, t.birthdate, t.profileType " +
					    "FROM Talents AS t LEFT JOIN Items AS i ON t.id=i.id " + 
					    	 "LEFT JOIN TalentsPhotos AS tp ON t.id=tp.talentId " + 
					    	 "LEFT JOIN TalentsAddresses AS ta ON t.id=ta.talentId " + 
					    "WHERE t.status='Talento' AND i.isSuppressed=false AND tp.name='Rostro' AND" +
					    	 " t.sex=? AND (t.physique=? OR t.hairTexture=? OR t.hairColor=? OR " + 
					    	  "t.skinTone=? OR t.eyeColor=? OR t.stature=? OR t.profileType=? OR " +
					 	 	  "ta.city=? OR (CAST(t.birthdate AS TIMESTAMP) BETWEEN " +
					 	 	  "{fn TIMESTAMPADD(SQL_TSI_YEAR, -5, CAST(? AS TIMESTAMP))} AND " +
					 	 	  "{fn TIMESTAMPADD(SQL_TSI_YEAR, 5, CAST(? AS TIMESTAMP))}) OR " +
							  "EXISTS(" + subquery + ")) " +
					 	"ORDER BY i.creationDate DESC";

			try
			{
				pStatement = connection.prepareStatement(queryStar);

				pStatement.setString(1, profile.getSex().toString());
				pStatement.setString(2, profile.getPhysique().toString());
				pStatement.setString(3, profile.getHairTexture().toString());
				pStatement.setString(4, profile.getHairColor());
				pStatement.setString(5, profile.getSkinTone().toString());
				pStatement.setString(6, profile.getEyeColor());
				pStatement.setFloat(7, profile.getStature());
				pStatement.setString(8, profile.getProfileType().toString());
				pStatement.setString(9, profile.getAddress().get("Provincia"));
				pStatement.setDate(10, (new Date(profile.getBirthdate().getTime())));
				pStatement.setDate(11, (new Date(profile.getBirthdate().getTime())));

				for (int i = 0; i < profile.getArtisticSkills().size(); i++)
				{
					pStatement.setString((12 + i), profile.getArtisticSkills().get(i));
				}

				result = pStatement.executeQuery();

				while(result.next())
				{
					talent = new Talent();

					talent.setId(result.getLong(1));
					talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(2).getBinaryStream()));
					talent.setName(result.getString(3));
					talent.setBirthdate(new java.util.Date(result.getDate(4).getTime()));
					talent.setProfileType(ProfileType.identifierOf(result.getString(5)));

					talents.add(talent);
				}

				pStatement = connection.prepareStatement(queryTalent);

				pStatement.setString(1, profile.getSex().toString());
				pStatement.setString(2, profile.getPhysique().toString());
				pStatement.setString(3, profile.getHairTexture().toString());
				pStatement.setString(4, profile.getHairColor());
				pStatement.setString(5, profile.getSkinTone().toString());
				pStatement.setString(6, profile.getEyeColor());
				pStatement.setFloat(7, profile.getStature());
				pStatement.setString(8, profile.getProfileType().toString());
				pStatement.setString(9, profile.getAddress().get("Provincia"));
				pStatement.setDate(10, (new Date(profile.getBirthdate().getTime())));
				pStatement.setDate(11, (new Date(profile.getBirthdate().getTime())));

				for (int i = 0; i < profile.getArtisticSkills().size(); i++)
				{
					pStatement.setString((12 + i), profile.getArtisticSkills().get(i));
				}

				result = pStatement.executeQuery();

				while(result.next())
				{
					talent = new Talent();

					talent.setId(result.getLong(1));
					talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(2).getBinaryStream()));
					talent.setName(result.getString(3));
					talent.setBirthdate(new java.util.Date(result.getDate(4).getTime()));
					talent.setProfileType(ProfileType.identifierOf(result.getString(5)));

					talents.add(talent);
				}

				pStatement.close();
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
		PreparedStatement pStatement = null;
		ResultSet result = null;
		String queryProject = null;

		Project project;

		if (criteria == null)
		{
			queryProject = "SELECT p.id, p.title, p.type, p.producer, p.state " +
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
		else
		{
			queryProject = "SELECT p.id, p.title, p.type, p.producer, p.state " +
						   "FROM Projects AS p, Items AS i " +
						   "WHERE p.id=i.id AND i.isSuppressed=false AND (p.title=? OR p.type=? OR" + 
						   		" p.producer=? OR p.director=?) " +
						   "ORDER BY i.creationDate DESC";

			try
			{
				pStatement = connection.prepareStatement(queryProject);

				pStatement.setString(1, criteria.getTitle());
				pStatement.setString(2, criteria.getType().toString());
				pStatement.setString(3, criteria.getProducer());
				pStatement.setString(4, criteria.getDirector());

				result = pStatement.executeQuery();

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

				pStatement.close();
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