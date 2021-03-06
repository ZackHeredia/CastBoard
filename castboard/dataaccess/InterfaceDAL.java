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
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	public ArrayList<Item> retriveSet (Item suppressed)
	{
		ArrayList<Item> items = new ArrayList<Item>();
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet result = null;
		String queryItem = null;
		Talent talent;
		Project project;

		if (suppressed == null)
		{
			queryItem = "SELECT i.id, i.creationDate, (t.name || ' ' || t.surname), p.title " +
								  "FROM Items AS i LEFT JOIN Talents AS t ON i.id=t.id LEFT JOIN " +
								  	   "Projects AS p ON i.id=p.id " +
								  "WHERE i.isSuppressed=true " +
								  "ORDER BY i.creationDate DESC";

			try
			{
				statement = connection.createStatement();
				result = statement.executeQuery(queryItem);

				while(result.next())
				{
					if (result.getString(3)!=null)
					{
						talent = new Talent();

						talent.setId(result.getLong(1));
						talent.setCreationDate(result.getTimestamp(2).toLocalDateTime());
						talent.setName(result.getString(3));

						items.add(talent);
					}
					else
					{
						project = new Project();

						project.setId(result.getLong(1));
						project.setCreationDate(result.getTimestamp(2).toLocalDateTime());
						project.setTitle(result.getString(4));

						items.add(project);
					}
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
			queryItem = "SELECT i.id, i.creationDate, (t.name || ' ' || t.surname), p.title " +
								  "FROM Items AS i LEFT JOIN Talents AS t ON i.id=t.id LEFT JOIN " +
								  	   "Projects AS p ON i.id=p.id " +
								  "WHERE i.id=?, i.isSuppressed=true " +
								  "ORDER BY i.creationDate DESC";

			try
			{
				pStatement = connection.prepareStatement(queryItem);

				pStatement.setLong(1, suppressed.getId());

				result = pStatement.executeQuery();

				result.next();

				if (result.getString(3)!=null)
				{
					talent = new Talent();

					talent.setId(result.getLong(1));
					talent.setCreationDate(result.getTimestamp(2).toLocalDateTime());
					talent.setName(result.getString(3));

					items.add(talent);
				}
				else
				{
					project = new Project();

					project.setId(result.getLong(1));
					project.setCreationDate(result.getTimestamp(2).toLocalDateTime());
					project.setTitle(result.getString(4));

					items.add(project);
				}

				pStatement.close();
				result.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}	

		return items;
	}

	public Talent retrive (Talent thumbnail)
	{
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<InputStream> futureVideo = null;
		PreparedStatement statement;
		ResultSet result;
		byte[] bytesVideo;
		String videoName = null;

		String queryVideo = "SELECT * " +
							 "FROM TalentsVideos AS tv " +
							 "WHERE tv.talentId=?";
		String queryTalent = "SELECT * " +
							 "FROM Talents AS t LEFT JOIN TalentsAddresses AS ta ON t.id=ta.talentId " +
							 "WHERE t.id=?";
		String queryNetwork = "SELECT * " +
							  "FROM TalentsSocialNetworks AS tsn " +
							  "WHERE tsn.talentId=?";
		String queryPhone = "SELECT * " +
							"FROM TalentsPhones AS tp " +
							"WHERE tp.talentId=?";
		String querySkills = "SELECT * " +
							 "FROM TalentsArtisticSkills AS tas " +
							 "WHERE tas.talentId=?";
		String queryLanguages = "SELECT * " +
								"FROM TalentsDominatedLanguages AS tdl " +
								"WHERE tdl.talentId=?";
		String queryPhotos = "SELECT * " +
							 "FROM TalentsPhotos AS tpt " +
							 "WHERE tpt.talentId=?";

		try
		{
			if (!CatalogsHandler.VIDEOS_PATHS.containsKey("Demo" + thumbnail.getId()))
			{			
				statement = connection.prepareStatement(queryVideo);
				statement.setLong(1, thumbnail.getId());

				result = statement.executeQuery();
				result.next();

				bytesVideo = result.getBytes("video");

				futureVideo = executor.submit(new Callable<InputStream>()
				{
					public InputStream call () throws Exception
					{
						return CatalogsHandler.parse(bytesVideo);
					}
				});
				videoName = result.getString("name");
			}

			statement = connection.prepareStatement(queryTalent);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();
			result.next();

			thumbnail.setName(result.getString(2));
			thumbnail.setSurname(result.getString("surname"));
			thumbnail.setBirthdate(result.getDate("birthdate"));
			thumbnail.setSex(Sex.identifierOf(result.getString("sex")));
			thumbnail.setAddress(new TreeMap<String, String>());
			thumbnail.getAddress().put("Numero", result.getString("num"));
			thumbnail.getAddress().put("Calle", result.getString("street"));
			thumbnail.getAddress().put("Sector", result.getString("neighborhood"));
			thumbnail.getAddress().put("Provincia", result.getString("city"));
			thumbnail.setEmail(result.getString("email"));
			thumbnail.setStature(result.getFloat("stature"));
			thumbnail.setEmail(result.getString("email"));
			thumbnail.setPhysique(Physique.identifierOf(result.getString("physique")));
			thumbnail.setSkinTone(SkinTone.identifierOf(result.getString("skinTone")));
			thumbnail.setHairTexture(HairTexture.identifierOf(result.getString("hairTexture")));
			thumbnail.setHairColor(result.getString("hairColor"));
			thumbnail.setEyeColor(result.getString("eyeColor"));
			thumbnail.setProfileType(ProfileType.identifierOf(result.getString("profileType")));
			thumbnail.setShirtSize(ShirtSize.identifierOf(result.getString("shirtSize")));
			thumbnail.setPantSize(result.getString("pantsSize"));
			thumbnail.setShoeSize(result.getFloat("shoeSize"));
			thumbnail.setAcademicLevel(result.getString("academicLevel"));
			thumbnail.setHobbies(result.getString("hobbies"));//result.getClob("hobbies").getSubString(1, (int) result.getClob("hobbies").length()));
			thumbnail.setScheduleAvailable(result.getString("scheduleAvailable"));//result.getClob("scheduleAvailable").getSubString(1, 
																//(int) result.getClob("scheduleAvailable").length()));
			thumbnail.setArtisticExperience(result.getString("artisticExperience"));//result.getClob("artisticExperience").getSubString(1, 
																//(int) result.getClob("artisticExperience").length()));
			thumbnail.setStatus(Status.identifierOf(result.getString("status")));

			statement = connection.prepareStatement(queryNetwork);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();
		
			thumbnail.setSocialNetworks(new TreeMap<String, String>());
			while (result.next())
			{
				thumbnail.getSocialNetworks().put(result.getString("network"), result.getString("account"));
			}

			statement = connection.prepareStatement(queryPhone);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();

			while (result.next())
			{
				if (result.getString("type").equals("M"))
					thumbnail.setMobilePhone(result.getString("num"));
				else
					thumbnail.setHomePhone(result.getString("num"));
			}

			statement = connection.prepareStatement(querySkills);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();
			
			thumbnail.setArtisticSkills(new ArrayList<String>());
			while (result.next())
			{
				thumbnail.getArtisticSkills().add(result.getString("skill"));
			}

			statement = connection.prepareStatement(queryLanguages);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();
			
			thumbnail.setDominatedLanguages(new ArrayList<String>());
			while (result.next())
			{
				thumbnail.getDominatedLanguages().add(result.getString("language"));
			}

			if (!CatalogsHandler.PHOTOS_PATHS.containsKey("Cuerpo Completo" + thumbnail.getId()))
			{
				statement = connection.prepareStatement(queryPhotos);
				statement.setLong(1, thumbnail.getId());

				result = statement.executeQuery();
				
				thumbnail.setPhotos(new TreeMap<String, java.awt.image.BufferedImage>());
				while (result.next())
				{
					CatalogsHandler.addPhotoEntry(thumbnail.getPhotos(), result.getString("name"), 
												  result.getBlob("photo").getBinaryStream());
				}
			}

			if (!CatalogsHandler.VIDEOS_PATHS.containsKey("Demo" + thumbnail.getId()))
			{
				thumbnail.setVideos(new TreeMap<String, InputStream>());
				while (!futureVideo.isDone())
				{
					Thread.sleep(1000);
				}

				thumbnail.getVideos().put(videoName, futureVideo.get());
			}

			statement.close();
			result.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return thumbnail;
	}
	public Project retrive (Project thumbnail)
	{
		PreparedStatement statement;
		ResultSet result;
		Talent talent;

		String queryProject = "SELECT * " +
							  "FROM Projects AS p " +
							  "WHERE p.id=?";
		String queryRoles = "SELECT * " +
							"FROM ProjectsRoles AS pr " +
							"WHERE pr.projectId=? " +
							"ORDER BY pr.category";
		String queryTalents = "SELECT pr.name, t.id, tp.photo, t.name, t.birthdate, t.profileType " +
							  "FROM ProposedTalents AS pt, ProjectsRoles AS pr, Talents AS t, " +
							  "TalentsPhotos AS tp " +
							  "WHERE pr.projectId=? AND pt.roleId=pr.id AND pt.talentId=t.id AND " +
							  "(t.id=tp.talentId AND tp.name='Rostro') " +
							  "ORDER BY pr.name";
		String queryTalentSelected = "SELECT pr.name, t.id, tp.photo, t.name, t.birthdate, t.profileType " +
									 "FROM ProposedTalents AS pt, ProjectsRoles AS pr, Talents AS t, " +
									 "TalentsPhotos AS tp " +
									 "WHERE pr.projectId=? AND pt.roleId=pr.id AND pt.talentId=t.id AND " +
									 "(t.id=tp.talentId AND tp.name='Rostro') AND pt.isSelected=true";

		try
		{
			statement = connection.prepareStatement(queryProject);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();
			result.next();

			thumbnail.setTitle(result.getString("title"));
			thumbnail.setType(Type.identifierOf(result.getString("type")));
			thumbnail.setProducer(result.getString("producer"));
			thumbnail.setDirector(result.getString("director"));
			thumbnail.setState(State.identifierOf(result.getString("state")));

			statement = connection.prepareStatement(queryRoles);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();

			thumbnail.setRoles(new TreeMap<Category, ArrayList<String>>());
			while (result.next())
			{
				if (!thumbnail.getRoles().containsKey(Category.identifierOf(result.getString("category"))))
					thumbnail.getRoles().put(Category.identifierOf(result.getString("category")), 
																   new ArrayList<String>());

				thumbnail.getRoles().get(Category.identifierOf(result.getString("category"))).add(result.getString("name"));
			}

			statement = connection.prepareStatement(queryTalents);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();

			thumbnail.setPreselecteds(new TreeMap<String, ArrayList<Talent>>());
			while (result.next())
			{
				if (thumbnail.getPreselecteds().containsKey(result.getString(1)))
				{
					talent = new Talent();
					talent.setId(result.getLong(2));
					talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(3).getBinaryStream()));
					talent.setName(result.getString(4));
					talent.setBirthdate(new java.util.Date(result.getDate(5).getTime()));
					talent.setProfileType(ProfileType.identifierOf(result.getString(6)));

					thumbnail.getPreselecteds().get(result.getString(1)).add(talent);
				}
				else
				{
					thumbnail.getPreselecteds().put(result.getString(1), new ArrayList<Talent>());

					talent = new Talent();
					talent.setId(result.getLong(2));
					talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(3).getBinaryStream()));
					talent.setName(result.getString(4));
					talent.setBirthdate(new java.util.Date(result.getDate(5).getTime()));
					talent.setProfileType(ProfileType.identifierOf(result.getString(6)));

					thumbnail.getPreselecteds().get(result.getString(1)).add(talent);
				}
			}

			statement = connection.prepareStatement(queryTalentSelected);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();

			thumbnail.setSelecteds(new TreeMap<String, Talent>());
			while (result.next())
			{
				talent = new Talent();
				talent.setId(result.getLong(2));
				talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(3).getBinaryStream()));
				talent.setName(result.getString(4));
				talent.setBirthdate(new java.util.Date(result.getDate(5).getTime()));
				talent.setProfileType(ProfileType.identifierOf(result.getString(6)));

				thumbnail.getSelecteds().put(result.getString(1), talent);
			}

			statement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return thumbnail;
	}
	public CinemaProject retrive (CinemaProject thumbnail)
	{
		Statement statement;
		ResultSet result;
		Sequence sequence;
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();

		String queryTitle = "SELECT title " +
							"FROM Projects " +
							"WHERE id=" + thumbnail.getId();
		String querySequences = "SELECT id, num, filmingDate, location, scriptPage " +
								"FROM Sequences " +
								"WHERE projectId=" + thumbnail.getId();

		try
		{
			statement = connection.createStatement();

			result = statement.executeQuery(queryTitle);

			result.next();
			thumbnail.setTitle(result.getString(1));

			result = statement.executeQuery(querySequences);

			while(result.next())
			{
				sequence = new Sequence();

				sequence.setId(result.getLong(1));
				sequence.setNumber(result.getInt(2));
				sequence.setFilmingDate(result.getDate(3));
				sequence.setLocation(result.getString(4));
				sequence.setScriptPage(result.getInt(5));

				sequences.add(sequence);
			}

			statement.close();
			result.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		thumbnail.setSequences(sequences);

		return thumbnail;
	}
	public Sequence retrive (Sequence thumbnail)
	{
		Statement statement;
		ResultSet result;
		Entry<Category, String> role;
		Talent talent;

		String querySequence = "SELECT * " +
							   "FROM Sequences " +
							   "WHERE id=" + thumbnail.getId();
		String querySelecteds = "SELECT pr.category, pr.name, t.id, tp.photo, t.name, t.birthdate, " +
									   "t.profileType " +
								"FROM SequencesTalents AS st LEFT JOIN ProjectsRoles AS pr ON st.roleId=pr.id" +
									" LEFT JOIN Talents AS t ON st.talentId=t.id, TalentsPhotos AS tp " +
								"WHERE (t.id=tp.talentId AND tp.name='Rostro') AND sequenceId=" + thumbnail.getId();

		try
		{
			statement = connection.createStatement();

			result = statement.executeQuery(querySequence);

			result.next();

			thumbnail.setNumber(result.getInt("num"));
			thumbnail.setDescription(result.getString("description"));
			thumbnail.setFilmingDate(result.getDate("filmingDate"));
			thumbnail.setLocationType(LocationType.identifierOf(result.getString("locationType")));
			thumbnail.setLocation(result.getString("location"));
			thumbnail.setDayMoment(DayMoment.identifierOf(result.getString("dayMoment")));
			thumbnail.setScriptPage(result.getInt("scriptPage"));
			thumbnail.setScriptDay(result.getInt("scriptDay"));

			result = statement.executeQuery(querySelecteds);

			thumbnail.setSelecteds(new TreeMap<String, Talent>());
			while(result.next())
			{
				role = new SimpleEntry<Category, String>(Category.identifierOf(result.getString(1)), 
														 result.getString(2));
				talent = new Talent();

				talent.setId(result.getLong(3));
				talent.setPhotos(CatalogsHandler.parse("Rostro", result.getBlob(4).getBinaryStream()));
				talent.setName(result.getString(5));
				talent.setBirthdate(new java.util.Date(result.getDate(6).getTime()));
				talent.setProfileType(ProfileType.identifierOf(result.getString(7)));

				thumbnail.getSelecteds().put(CatalogsHandler.parse(role), talent);
			}

			statement.close();
			result.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return thumbnail;
	}

	public boolean enter (Talent talent)
	{
		Statement statement;
		PreparedStatement pStatement;
		ResultSet result;
		boolean wasEntered = false;

		String insertItem = "INSERT INTO Items(creationDate, isSuppressed) " +
							"VALUES (DEFAULT, DEFAULT)";
		String queryId = "SELECT MAX(id) FROM Items";
		String insertTalent = "INSERT INTO Talents(id, name, surname, birthdate, sex, email, " + 
												   "physique, stature, skinTone, hairTexture, " +
												   "hairColor, eyeColor, profileType, shirtSize, " +
												   "pantsSize, shoeSize, academicLevel, hobbies, " +
												   "scheduleAvailable, artisticExperience, status) " +
							   "VALUES ((" + queryId + "), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
							   		   "?, ?, ?, ?, ?, ?, DEFAULT)";
		String insertAddress = "INSERT INTO TalentsAddresses(talentId, num, street, neighborhood, city) " +
							   "VALUES ((" + queryId + "), ?, ?, ?, ?)";
		String insertNetworks = "INSERT INTO TalentsSocialNetworks(talentId, network, account) " +
							    "VALUES ((" + queryId + "), ?, ?)";
		String insertPhones = "INSERT INTO TalentsPhones(talentId, num, type) " +
							   "VALUES ((" + queryId + "), ?, ?)";
		String insertSkills = "INSERT INTO TalentsArtisticSkills(talentId, skill) " +
							  "VALUES ((" + queryId + "), ?)";
		String insertLanguages = "INSERT INTO TalentsDominatedLanguages(talentId, language) " +
							    "VALUES ((" + queryId + "), ?)";
		String insertPhotos = "INSERT INTO TalentsPhotos(talentId, name, photo) " +
							    "VALUES ((" + queryId + "), ?, ?)";
		String insertVideos = "INSERT INTO TalentsVideos(talentId, name, video) " +
							    "VALUES ((" + queryId + "), ?, ?)";
		String query = "SELECT MAX(id) FROM Talents WHERE name=? AND birthdate=?";

		try
		{
			statement = connection.createStatement();
			pStatement = connection.prepareStatement(insertTalent);

			pStatement.setString(1, talent.getName());
			pStatement.setString(2, talent.getSurname());
			pStatement.setDate(3, (new Date(talent.getBirthdate().getTime())));
			pStatement.setString(4, talent.getSex().toString());
			pStatement.setString(5, talent.getEmail());
			pStatement.setString(6, talent.getPhysique().toString());
			pStatement.setFloat(7, talent.getStature());
			pStatement.setString(8, talent.getSkinTone().toString());
			pStatement.setString(9, talent.getHairTexture().toString());
			pStatement.setString(10, talent.getHairColor());
			pStatement.setString(11, talent.getEyeColor());
			pStatement.setString(12, talent.getProfileType().toString());
			pStatement.setString(13, talent.getShirtSize().toString());
			pStatement.setString(14, talent.getPantSize());
			pStatement.setFloat(15, talent.getShoeSize());
			pStatement.setString(16, talent.getAcademicLevel());
			pStatement.setString(17, talent.getHobbies());
			pStatement.setString(18, talent.getScheduleAvailable());
			pStatement.setString(19, talent.getArtisticExperience());

			connection.setAutoCommit(false);

			statement.executeUpdate(insertItem);
			pStatement.executeUpdate();

			pStatement = connection.prepareStatement(insertAddress);

			pStatement.setString(1, talent.getAddress().get("Numero"));
			pStatement.setString(2, talent.getAddress().get("Calle"));
			pStatement.setString(3, talent.getAddress().get("Sector"));
			pStatement.setString(4, talent.getAddress().get("Provincia"));

			pStatement.executeUpdate();

			pStatement = connection.prepareStatement(insertNetworks);

			for (Entry<String, String> network : talent.getSocialNetworks().entrySet())
			{
				pStatement.setString(1, network.getKey());
				pStatement.setString(2, network.getValue());

				pStatement.executeUpdate();
			}

			pStatement = connection.prepareStatement(insertPhones);

			pStatement.setString(1, talent.getMobilePhone());
			pStatement.setString(2, "M");

			pStatement.executeUpdate();

			pStatement.setString(1, talent.getHomePhone());
			pStatement.setString(2, "F");

			pStatement.executeUpdate();

			pStatement = connection.prepareStatement(insertSkills);

			for (String skill : talent.getArtisticSkills())
			{
				pStatement.setString(1, skill);

				pStatement.executeUpdate();
			}

			pStatement = connection.prepareStatement(insertLanguages);

			for (String language : talent.getDominatedLanguages())
			{
				pStatement.setString(1, language);

				pStatement.executeUpdate();
			}

			pStatement = connection.prepareStatement(insertPhotos);

			for (Entry<String, BufferedImage> photo : talent.getPhotos().entrySet())
			{
				pStatement.setString(1, photo.getKey());
				pStatement.setBlob(2, CatalogsHandler.parse(photo.getValue()));

				pStatement.executeUpdate();
			}

			pStatement = connection.prepareStatement(insertVideos);

			for (Entry<String, InputStream> video : talent.getVideos().entrySet())
			{
				pStatement.setString(1, video.getKey());
				pStatement.setBlob(2, video.getValue());

				pStatement.executeUpdate();
			}

			connection.commit();
			connection.setAutoCommit(true);

			pStatement = connection.prepareStatement(query);

			pStatement.setString(1, talent.getName());
			pStatement.setDate(2, (new Date(talent.getBirthdate().getTime())));

			result = pStatement.executeQuery();

			wasEntered = result.next();

			statement.close();
			pStatement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			try
			{
				connection.rollback();
				connection.setAutoCommit(true);
			}
			catch (SQLException ex) 
			{
				ex.printStackTrace();
				return false;
			}

			return false;
		}

		return wasEntered;
	}
	public boolean enter (Project project)
	{
		Statement statement;
		PreparedStatement pStatement;
		ResultSet result;
		boolean wasEntered = false;

		String insertItem = "INSERT INTO Items(creationDate, isSuppressed) " +
							"VALUES (DEFAULT, DEFAULT)";
		String queryId = "SELECT MAX(id) FROM Items";
		String insertProject = "INSERT INTO Projects(id, title, type, producer, director, state) " +
							   "VALUES ((" + queryId + "), ?, ?, ?, ?, DEFAULT)";
		String insertRoles = "INSERT INTO ProjectsRoles(projectId, category, name) " +
							   "VALUES ((" + queryId + "), ?, ?)";
		String query = "SELECT MAX(id) FROM Projects WHERE title=? AND type=?";

		try
		{
			statement = connection.createStatement();
			pStatement = connection.prepareStatement(insertProject);

			pStatement.setString(1, project.getTitle());
			pStatement.setString(2, project.getType().toString());
			pStatement.setString(3, project.getProducer());
			pStatement.setString(4, project.getDirector());

			connection.setAutoCommit(false);

			statement.executeUpdate(insertItem);
			pStatement.executeUpdate();

			pStatement = connection.prepareStatement(insertRoles);
			for (Entry<Category, ArrayList<String>> role : project.getRoles().entrySet())
			{
				for (String name : role.getValue())
				{
					pStatement.setString(1, role.getKey().toString());
					pStatement.setString(2, name);

					pStatement.executeUpdate();
				}
			}

			connection.commit();
			connection.setAutoCommit(true);

			pStatement = connection.prepareStatement(query);

			pStatement.setString(1, project.getTitle());
			pStatement.setString(2, project.getType().toString());

			result = pStatement.executeQuery();

			wasEntered = result.next();

			statement.close();
			pStatement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			try
			{
				connection.rollback();
				connection.setAutoCommit(true);
			}
			catch (SQLException ex) 
			{
				ex.printStackTrace();
				return false;
			}

			return false;
		}

		return wasEntered;
	}
	public boolean enter (CinemaProject cinema)
	{
		Statement statement;
		PreparedStatement pStatement;
		ResultSet result;
		boolean wasEntered = false;
		Sequence sequence = cinema.getSequences().get(0);

		String insertItem = "INSERT INTO Items(creationDate, isSuppressed) " +
							"VALUES (DEFAULT, DEFAULT)";
		String queryId = "SELECT MAX(id) FROM Items";
		String insertSequence = "INSERT INTO Sequences(id, projectId, num, description, filmingDate, " +
								"locationType, location, dayMoment, scriptPage, scriptDay) " +
							   "VALUES ((" + queryId + "), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String queryRole = "SELECT id FROM ProjectsRoles WHERE projectId=? AND name=?";
		String insertSelecteds = "INSERT INTO SequencesTalents(sequenceId, roleId, talentId) " +
							   "VALUES ((" + queryId + "), (" + queryRole + "), ?)";
		String query = "SELECT MAX(id) FROM Sequences WHERE num=? AND projectId=?";

		try
		{
			statement = connection.createStatement();
			pStatement = connection.prepareStatement(insertSequence);

			pStatement.setLong(1, cinema.getId());
			pStatement.setInt(2, sequence.getNumber());
			pStatement.setString(3, sequence.getDescription());
			pStatement.setDate(4, (new Date(sequence.getFilmingDate().getTime())));
			pStatement.setString(5, sequence.getLocationType().toString());
			pStatement.setString(6, sequence.getLocation());
			pStatement.setString(7, sequence.getDayMoment().toString());
			pStatement.setInt(8, sequence.getScriptPage());
			pStatement.setInt(9, sequence.getScriptDay());

			connection.setAutoCommit(false);

			statement.executeUpdate(insertItem);
			pStatement.executeUpdate();

			pStatement = connection.prepareStatement(insertSelecteds);
			for (Entry<String, Talent> selected : sequence.getSelecteds().entrySet())
			{	
				pStatement.setLong(1, cinema.getId());
				pStatement.setString(2, selected.getKey());
				pStatement.setLong(3, selected.getValue().getId());

				pStatement.executeUpdate();
			}

			connection.commit();
			connection.setAutoCommit(true);

			pStatement = connection.prepareStatement(query);

			pStatement.setLong(1, sequence.getNumber());
			pStatement.setLong(2, cinema.getId());

			result = pStatement.executeQuery();

			wasEntered = result.next();

			statement.close();
			pStatement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			try
			{
				connection.rollback();
				connection.setAutoCommit(true);
			}
			catch (SQLException ex) 
			{
				ex.printStackTrace();
				return false;
			}

			return false;
		}

		return wasEntered;
	}

	public boolean update (Talent talent)
	{
		Statement statement;
		PreparedStatement pStatement;
		CallableStatement cStatement = null;
		ResultSet resultNetworks;
		ResultSet resultSkills;
		ResultSet resultLanguages;
		int minimunAffecteds = 7;
		int affectedRows = 0;
		boolean isInserted = false;

		String queryNetwork = "SELECT network " +
							  "FROM TalentsSocialNetworks AS tsn " +
							  "WHERE tsn.talentId=?";
		String querySkills = "SELECT skill " +
							 "FROM TalentsArtisticSkills AS tas " +
							 "WHERE tas.talentId=?";
		String queryLanguages = "SELECT language " +
								"FROM TalentsDominatedLanguages AS tdl " +
								"WHERE tdl.talentId=?";
		String insertNetworks = "INSERT INTO TalentsSocialNetworks(talentId, network, account) " +
							    "VALUES (?, ?, ?)";
		String insertSkills = "INSERT INTO TalentsArtisticSkills(talentId, skill) " +
							  "VALUES (?, ?)";
		String insertLanguages = "INSERT INTO TalentsDominatedLanguages(talentId, language) " +
							    "VALUES (?, ?)";
		String updateTalent = "UPDATE Talents SET name=?, surname=?, birthdate=?, sex=?, email=?, " + 
												 "physique=?, stature=?, skinTone=?, hairTexture=?, " +
												 "hairColor=?, eyeColor=?, profileType=?, shirtSize=?, " +
												 "pantsSize=?, shoeSize=?, academicLevel=?, hobbies=?, " +
												 "scheduleAvailable=?, artisticExperience=? " +
							   "WHERE id=?";
		String updateAddress = "UPDATE TalentsAddresses SET num=?, street=?, neighborhood=?, " +
														   "city=? " +
							   "WHERE talentId=?";
		String updateNetworks = "UPDATE TalentsSocialNetworks SET account=? " +
							    "WHERE network=? AND talentId=?";
		String updatePhones = "UPDATE TalentsPhones SET num=? " +
							  "WHERE type=? AND talentId=?";
		String updateSkills = "UPDATE TalentsArtisticSkills SET skill=? " +
							  "WHERE skill=? AND talentId=?";
		String updateLanguages = "UPDATE TalentsDominatedLanguages SET language=? " +
							     "WHERE talentId=?";
		String updatePhotos = "UPDATE TalentsPhotos SET photo=? " +
							  "WHERE name=? AND talentId=?";
		String updateVideos = "UPDATE TalentsVideos SET video=? " +
							  "WHERE name=? AND talentId=?";

		try
		{
			pStatement = connection.prepareStatement(queryNetwork, ResultSet.TYPE_SCROLL_INSENSITIVE, 
																   ResultSet.CONCUR_READ_ONLY);
			pStatement.setLong(1, talent.getId());

			resultNetworks = pStatement.executeQuery();

			pStatement = connection.prepareStatement(querySkills, ResultSet.TYPE_SCROLL_INSENSITIVE, 
																  ResultSet.CONCUR_READ_ONLY);
			pStatement.setLong(1, talent.getId());

			resultSkills = pStatement.executeQuery();

			pStatement = connection.prepareStatement(queryLanguages, ResultSet.TYPE_SCROLL_INSENSITIVE, 
																	 ResultSet.CONCUR_READ_ONLY);
			pStatement.setLong(1, talent.getId());

			resultLanguages = pStatement.executeQuery();

			pStatement = connection.prepareStatement(updateTalent);

			pStatement.setString(1, talent.getName());
			pStatement.setString(2, talent.getSurname());
			pStatement.setDate(3, (new Date(talent.getBirthdate().getTime())));
			pStatement.setString(4, talent.getSex().toString());
			pStatement.setString(5, talent.getEmail());
			pStatement.setString(6, talent.getPhysique().toString());
			pStatement.setFloat(7, talent.getStature());
			pStatement.setString(8, talent.getSkinTone().toString());
			pStatement.setString(9, talent.getHairTexture().toString());
			pStatement.setString(10, talent.getHairColor());
			pStatement.setString(11, talent.getEyeColor());
			pStatement.setString(12, talent.getProfileType().toString());
			pStatement.setString(13, talent.getShirtSize().toString());
			pStatement.setString(14, talent.getPantSize());
			pStatement.setFloat(15, talent.getShoeSize());
			pStatement.setString(16, talent.getAcademicLevel());
			pStatement.setString(17, talent.getHobbies());
			pStatement.setString(18, talent.getScheduleAvailable());
			pStatement.setString(19, talent.getArtisticExperience());
			pStatement.setLong(20, talent.getId());

			connection.setAutoCommit(false);

			pStatement.executeUpdate();
			++affectedRows;

			pStatement = connection.prepareStatement(updateAddress);

			pStatement.setString(1, talent.getAddress().get("Numero"));
			pStatement.setString(2, talent.getAddress().get("Calle"));
			pStatement.setString(3, talent.getAddress().get("Sector"));
			pStatement.setString(4, talent.getAddress().get("Provincia"));
			pStatement.setLong(5, talent.getId());

			pStatement.executeUpdate();
			++affectedRows;
		
			for (Entry<String, String> network : talent.getSocialNetworks().entrySet())
			{
				while (resultNetworks.next())
				{
					isInserted = (resultNetworks.getString(1).equals(network.getKey()));

					if (isInserted)
						break;
				}

				if (isInserted)
				{
					pStatement = connection.prepareStatement(updateNetworks);

					pStatement.setString(1, network.getValue());
					pStatement.setString(2, network.getKey());
					pStatement.setLong(3, talent.getId());

					pStatement.executeUpdate();
					++affectedRows;
				}
				else
				{
					pStatement = connection.prepareStatement(insertNetworks);

					pStatement.setLong(1, talent.getId());
					pStatement.setString(2, network.getKey());
					pStatement.setString(3, network.getValue());

					pStatement.executeUpdate();
					++affectedRows;
				}

				resultNetworks.beforeFirst();
			}
			
			pStatement = connection.prepareStatement(updatePhones);

			pStatement.setString(1, talent.getMobilePhone());
			pStatement.setString(2, "M");
			pStatement.setLong(3, talent.getId());

			pStatement.executeUpdate();
			++affectedRows;

			pStatement.setString(1, talent.getHomePhone());
			pStatement.setString(2, "F");
			pStatement.setLong(3, talent.getId());

			pStatement.executeUpdate();
			++affectedRows;

			while (resultSkills.next())
			{
				for (String skill : talent.getArtisticSkills())
				{
					isInserted = (skill.equals(resultSkills.getString(1)));

					if (isInserted)
						break;
				}

				if (isInserted == false)
				{
					cStatement = connection.prepareCall("{CALL dba.drop_record_sp(?, ?, ?, ?, ?)}");
					
					cStatement.setLong(1, talent.getId());
					cStatement.setString(2, "TalentsArtisticSkills");
					cStatement.setString(3, "skill");
					cStatement.setString(4, resultSkills.getString(1));
					cStatement.setString(5, "talentId");

					cStatement.execute();
					++affectedRows;

					cStatement.close();
				}
			}

			resultSkills.beforeFirst();

			for (String skill : talent.getArtisticSkills())
			{
				while (resultSkills.next())
				{
					isInserted = (resultSkills.getString(1).equals(skill));

					if (isInserted)
						break;
				}

				if (isInserted == false)
				{
					pStatement = connection.prepareStatement(insertSkills);
					
					pStatement.setLong(1, talent.getId());
					pStatement.setString(2, skill);

					pStatement.executeUpdate();
					++affectedRows;
				}

				resultSkills.beforeFirst();
			}

			while (resultLanguages.next())
			{
				for (String language : talent.getDominatedLanguages())
				{
					isInserted = (language.equals(resultLanguages.getString(1)));

					if (isInserted)
						break;
				}

				if (isInserted == false)
				{
					cStatement = connection.prepareCall("{CALL dba.drop_record_sp(?, ?, ?, ?, ?)}");
					
					cStatement.setLong(1, talent.getId());
					cStatement.setString(2, "TalentsDominatedLanguages");
					cStatement.setString(3, "language");
					cStatement.setString(4, resultLanguages.getString(1));
					cStatement.setString(5, "talentId");

					cStatement.execute();
					++affectedRows;

					cStatement.close();
				}
			}

			resultLanguages.beforeFirst();

			for (String language : talent.getDominatedLanguages())
			{
				while (resultLanguages.next())
				{
					isInserted = (resultLanguages.getString(1).equals(language));

					if (isInserted)
						break;
				}

				if (isInserted == false)
				{
					pStatement = connection.prepareStatement(insertLanguages);		

					pStatement.setLong(1, talent.getId());
					pStatement.setString(2, language);

					pStatement.executeUpdate();
					++affectedRows;
				}

				resultLanguages.beforeFirst();
			}

			pStatement = connection.prepareStatement(updatePhotos);

			for (Entry<String, BufferedImage> photo : talent.getPhotos().entrySet())
			{
				pStatement.setBlob(1, CatalogsHandler.parse(photo.getValue()));
				pStatement.setString(2, photo.getKey());
				pStatement.setLong(3, talent.getId());

				pStatement.executeUpdate();
				++affectedRows;
			}

			pStatement = connection.prepareStatement(updateVideos);

			for (Entry<String, InputStream> video : talent.getVideos().entrySet())
			{
				pStatement.setBlob(1, video.getValue());
				pStatement.setString(2, video.getKey());
				pStatement.setLong(3, talent.getId());

				pStatement.executeUpdate();
				++affectedRows;
			}

			connection.commit();
			connection.setAutoCommit(true);

			pStatement.close();
			resultNetworks.close();
			resultSkills.close();
			resultLanguages.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			try
			{
				connection.rollback();
				connection.setAutoCommit(true);
			}
			catch (SQLException ex) 
			{
				ex.printStackTrace();

				return false;
			}

			return false;
		}

		return (minimunAffecteds <= affectedRows);
	}
	public boolean update (Project project)
	{
		PreparedStatement pStatement;
		CallableStatement cStatement = null;
		ResultSet result;
		boolean isInserted = false;
		int affectedRows = 0;

		String queryRoles = "SELECT name " +
							"FROM ProjectsRoles AS pr " +
							"WHERE pr.projectId=? " +
							"ORDER BY pr.category";
		String insertRoles = "INSERT INTO ProjectsRoles(projectId, category, name) " +
							 "VALUES (?, ?, ?)";
		String updateProject = "UPDATE Projects SET title=?, type=?, producer=?, director=? " +
							   "WHERE id=?";

		try
		{
			pStatement = connection.prepareStatement(queryRoles, ResultSet.TYPE_SCROLL_INSENSITIVE, 
													 ResultSet.CONCUR_READ_ONLY);
			pStatement.setLong(1, project.getId());

			result = pStatement.executeQuery();

			pStatement = connection.prepareStatement(updateProject);

			pStatement.setString(1, project.getTitle());
			pStatement.setString(2, project.getType().toString());
			pStatement.setString(3, project.getProducer());
			pStatement.setString(4, project.getDirector());
			pStatement.setLong(5, project.getId());

			connection.setAutoCommit(false);

			pStatement.executeUpdate();
			++affectedRows;

			while (result.next())
			{
				for (Entry<Category, ArrayList<String>> role : project.getRoles().entrySet())
				{
					for (String name : role.getValue())
					{
						isInserted = (name.equals(result.getString(1)));

						if (isInserted)
							break;
					}

					if (isInserted)
						break;
				}

				if (isInserted == false)
				{
					cStatement = connection.prepareCall("{CALL dba.drop_record_sp(?, ?, ?, ?, ?)}");
					
					cStatement.setLong(1, project.getId());
					cStatement.setString(2, "ProjectsRoles");
					cStatement.setString(3, "name");
					cStatement.setString(4, "'" + result.getString(1) + "'");
					cStatement.setString(5, "projectId");

					pStatement.execute();
					++affectedRows;

					cStatement.close();
				}
			}

			for (Entry<Category, ArrayList<String>> role : project.getRoles().entrySet())
			{
				for (String name : role.getValue())
				{
					while (result.next())
					{
						isInserted = (result.getString(1).equals(name));

						if (isInserted)
							break;
					}

					if (isInserted == false)
					{
						pStatement = connection.prepareStatement(insertRoles);
				
						pStatement.setLong(1, project.getId());
						pStatement.setString(2, role.getKey().toString());
						pStatement.setString(3, name);

						pStatement.executeUpdate();
						++affectedRows;
					}

					result.beforeFirst();
				}
			}

			connection.commit();
			connection.setAutoCommit(true);

			pStatement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			try
			{
				connection.rollback();
				connection.setAutoCommit(true);
			}
			catch (SQLException ex) 
			{
				ex.printStackTrace();
				return false;
			}

			return false;
		}

		return (affectedRows >= 1);
	}

	public boolean suppress (long id)
	{
		PreparedStatement statement;
		int affectedRows = 0;
		
		String update = "UPDATE Items SET isSuppressed=true WHERE id=?";
		
		try
		{
			statement = connection.prepareStatement(update);
			statement.setLong(1, id);
				
			affectedRows = statement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return (affectedRows >= 1);
	}
	public boolean unsuppress (long id)
	{
		PreparedStatement statement;
		int affectedRows = 0;
		
		String update = "UPDATE Items SET isSuppressed=false WHERE id=?";
		
		try
		{
			statement = connection.prepareStatement(update);
			statement.setLong(1, id);
				
			affectedRows = statement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return (affectedRows >= 1);
	}

	public boolean switchStatus (long id, Status currentStatus)
	{
		PreparedStatement statement;
		int affectedRows = 0;;
		
		String update = "UPDATE Talents SET status=? WHERE id=?";
		
	try
	{
		statement = connection.prepareStatement(update);
			
		statement.setString(1, ((currentStatus.equals("Talento") ? "Estrella" : "Talento")));
		statement.setLong(2, id);
			
		affectedRows = statement.executeUpdate();
	}
	catch (SQLException e)
	{
		e.printStackTrace();
	}
		
		return (affectedRows >= 1);
	}

	public boolean terminate (long id)
	{
		return changeState(id, "Termino");
	}

	public boolean preselect (long talentId, long projectId, String roleName)
	{
		PreparedStatement pStatement;
		long roleId;
		ResultSet result;
		boolean isPreselected = false;
		int affectedRows = 0;

		String queryRole = "SELECT id " +
							"FROM ProjectsRoles " +
							"WHERE projectId=? AND name=?";
		String insertProposal = "INSERT INTO ProposedTalents(roleId, talentId, isSelected) " +
							 "VALUES (?, ?, DEFAULT)";

		try
		{
			pStatement = connection.prepareStatement(queryRole);

			pStatement.setLong(1, projectId);
			pStatement.setString(2, roleName);

			result = pStatement.executeQuery();

			result.next();
			roleId = result.getLong(1);

			pStatement = connection.prepareStatement(insertProposal);

			pStatement.setLong(1, roleId);
			pStatement.setLong(2, talentId);

			pStatement.executeUpdate();
			affectedRows++;

			pStatement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			return false;
		}

		return (affectedRows>=1);
	}
	public boolean select (long talentId, long projectId, String roleName)
	{
		PreparedStatement pStatement;
		long roleId;
		ResultSet result;
		int affectedRows = 0;

		String queryRole = "SELECT id " +
							"FROM ProjectsRoles " +
							"WHERE projectId=? AND name=?";
		String updateProposal = "UPDATE ProposedTalents SET isSelected=true " +
								"WHERE roleId=? AND talentId=?";

		try
		{
			pStatement = connection.prepareStatement(queryRole);

			pStatement.setLong(1, projectId);
			pStatement.setString(2, roleName);

			result = pStatement.executeQuery();

			result.next();
			roleId = result.getLong(1);

			pStatement = connection.prepareStatement(updateProposal);

			pStatement.setLong(1, roleId);
			pStatement.setLong(2, talentId);

			pStatement.executeUpdate();
			affectedRows++;

			pStatement.close();
			result.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			return false;
		}

		changeState(projectId, "Desarrollo");

		return (affectedRows>=1);
	}

	private boolean changeState (long id, String state)
	{
		PreparedStatement statement;
		int affectedRows = 0;;
		
		String update = "UPDATE Projects SET state=? WHERE id=?";
		
		try
		{
			statement = connection.prepareStatement(update);
				
			statement.setString(1, state);
			statement.setLong(2, id);
				
			affectedRows = statement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return (affectedRows >= 1);
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