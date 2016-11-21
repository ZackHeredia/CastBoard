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

	public Talent retrive (Talent thumbnail)
	{
		PreparedStatement statement;
		ResultSet result;

		String queryTalent = "SELECT * " +
							 "FROM Talents AS t LEFT JOIN TalentsAddresses AS ta ON t.id=ta.talentId " +
								  "LEFT JOIN TalentsVideos AS tv ON t.id=tv.talentId " +
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
			thumbnail.setVideos(new TreeMap<String, InputStream>());
			thumbnail.getVideos().put(result.getString(30), CatalogsHandler.parse(result.getBytes("video")));

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
				if (result.getString("type") == "M")
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

			statement = connection.prepareStatement(queryPhotos);
			statement.setLong(1, thumbnail.getId());

			result = statement.executeQuery();
			
			thumbnail.setPhotos(new TreeMap<String, java.awt.image.BufferedImage>());
			while (result.next())
			{
				CatalogsHandler.addPhotoEntry(thumbnail.getPhotos(), result.getString("name"), 
											  result.getBlob("photo").getBinaryStream());
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
							"WHERE pr.projectId=?";
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

			thumbnail.setRoles(new TreeMap<Category, String>());
			while (result.next())
			{
				thumbnail.getRoles().put(Category.identifierOf(result.getString("category")), result.getString("name"));
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
					thumbnail.getPreselecteds().put(result.getString(1), new ArrayList<Talent>());
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