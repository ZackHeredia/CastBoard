package castboard.domain;

import static castboard.domain.Enumeration.*;

import castboard.dataaccess.InterfaceDAL;
import castboard.view.MasterFrame;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class CatalogsHandler
{
	public static final int FRONT_SET = 0;
	public static final int TALENT_SET = 1;
	public static final int PROJECT_SET = 2;
	public static final int CINEMA_SET = 3;
	public static final int SEQUENCE_SET = 4;

	public static final TreeMap<String, String> PHOTOS_PATHS = new TreeMap<String, String>();
	public static final TreeMap<String, String> VIDEOS_PATHS = new TreeMap<String, String>();

	public static boolean connect (String user, String pass)
	{
		return getInterfaceDAL().connect(user, pass);
	}
	public static void disconnect ()
	{
		getInterfaceDAL().close();
	}

	public static ArrayList<ArrayList<String>> getSet (int setType)
	{
		ArrayList<ArrayList<String>> set = null;

		switch (setType)
		{
			case FRONT_SET:
				set = getFrontSet();

				break;
			case TALENT_SET:
				set = getTalentSet();

				break;
			case PROJECT_SET:
				set = getProjectSet();

				break;
			default:
				set = null;

				break;
		}

		return set;
	}
	public static ArrayList<ArrayList<String>> search (ArrayList<String> args, int setType)
	{
		ArrayList<ArrayList<String>> set = null;

		switch (setType)
		{
			case TALENT_SET:
				set = searchTalent(args);

				break;
			case PROJECT_SET:
				set = searchProject(args);

				break;
			default:
				set = null;

				break;
		}

		return set;
	}
	public static ArrayList<ArrayList<String>> get (String id, int setType)
	{
		ArrayList<ArrayList<String>> set = null;

		switch (setType)
		{
			case TALENT_SET:
				set = getTalent(id);

				break;
			case PROJECT_SET:
				set = getProject(id);

				break;
			case CINEMA_SET:
				set = getCinemaProject(id);

				break;
			case SEQUENCE_SET:
				set = getSequence(id);

				break;
			default:
				set = null;

				break;
		}

		return set;
	}
	public static boolean enter (ArrayList<ArrayList<String>> values, int setType)
	{
		boolean wasEntered = false;

		switch (setType)
		{
			case TALENT_SET:
				wasEntered = enterTalent(values);

				break;
			case PROJECT_SET:
				wasEntered = enterProject(values);

				break;
			case SEQUENCE_SET:
				wasEntered = enterSequence(values);

				break;
			default:
				wasEntered = false;

				break;
		}

		return wasEntered;
	}
	public static boolean update (ArrayList<ArrayList<String>> values, int setType)
	{
		boolean wasUpdated = false;

		switch (setType)
		{
			case TALENT_SET:
				wasUpdated = updateTalent(values);

				break;
			case PROJECT_SET:
				wasUpdated = updateProject(values);

				break;
			default:
				wasUpdated = false;

				break;
		}

		return wasUpdated;
	}

	public static void notifyException (Exception e)
	{
		String message = e.getMessage();

		getMasterFrame().displayException(message);
	}

	private static InterfaceDAL getInterfaceDAL ()
	{
		return InterfaceDAL.getInstance();
	}
	private static MasterFrame getMasterFrame ()
	{
		return MasterFrame.getInstance();
	}

	private static ArrayList<ArrayList<String>> getFrontSet ()
	{
		ArrayList<Item> items = getInterfaceDAL().retriveSet();
		int separator = items.indexOf(null);
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;
		Project projectThumbnail;
		Talent talentThumbnail;

		if(!items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				inner = new ArrayList<String>();

				if (i < separator)
				{
					projectThumbnail = (Project) items.get(i);

					inner.add(Long.toString(projectThumbnail.getId()));
					inner.add(projectThumbnail.getTitle());
					inner.add(projectThumbnail.getType().toString());
					inner.add(projectThumbnail.getProducer());
					inner.add(projectThumbnail.getState().toString());

					set.add(inner);
				}
				else if (i == separator)
					set.add(null);
				else
				{
					talentThumbnail = (Talent) items.get(i);

					inner.add(Long.toString(talentThumbnail.getId()));
					inner.add(parse(talentThumbnail.getPhotos(), talentThumbnail.getId())[0]);
					inner.add(talentThumbnail.getName());
					inner.add(parse(talentThumbnail.getBirthdate()));
					inner.add(talentThumbnail.getProfileType().toString());

					set.add(inner);
				}
			}
		}

		return set;
	}
	private static ArrayList<ArrayList<String>> getTalentSet ()
	{
		Talent thumbnail = null;
		ArrayList<Talent> items = getInterfaceDAL().retriveSet(thumbnail);
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;

		if(!items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				inner = new ArrayList<String>();
				thumbnail = (Talent) items.get(i);

				inner.add(Long.toString(thumbnail.getId()));
				inner.add(parse(thumbnail.getPhotos(), thumbnail.getId())[0]);
				inner.add(thumbnail.getName());
				inner.add(parse(thumbnail.getBirthdate()));
				inner.add(thumbnail.getProfileType().toString());

				set.add(inner);
			}
		}

		return set;
	}
	private static ArrayList<ArrayList<String>> getProjectSet ()
	{
		Project thumbnail = null;
		ArrayList<Project> items = getInterfaceDAL().retriveSet(thumbnail);
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;

		if(!items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				inner = new ArrayList<String>();
				thumbnail = (Project) items.get(i);

				inner.add(Long.toString(thumbnail.getId()));
				inner.add(thumbnail.getTitle());
				inner.add(thumbnail.getType().toString());
				inner.add(thumbnail.getProducer());
				inner.add(thumbnail.getState().toString());

				set.add(inner);
			}
		}

		return set;
	}
	@SuppressWarnings("unchecked")
	private static ArrayList<ArrayList<String>> searchTalent (ArrayList<String> args)
	{
		Talent profile = new Talent();
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<Talent> items;
		ArrayList<String> inner;
		TreeMap<String, String> address = new TreeMap<String, String>();

		address.put("Provincia", args.get(9));

		profile.setSex(Sex.identifierOf(args.get(0)));
		profile.setPhysique(Physique.identifierOf(args.get(1)));
		profile.setHairTexture(HairTexture.identifierOf(args.get(2)));
		profile.setHairColor(args.get(3));
		profile.setSkinTone(SkinTone.identifierOf(args.get(4)));
		profile.setEyeColor(args.get(5));
		profile.setStature(parse(args.get(6), args.get(7)));
		profile.setProfileType(ProfileType.identifierOf(args.get(8)));
		profile.setAddress(address);
		profile.setBirthdate(parse(Integer.parseInt(args.get(10))));
		profile.setArtisticSkills(new ArrayList(args.subList(11, args.size())));

		items = getInterfaceDAL().retriveSet(profile);

		if(!items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				inner = new ArrayList<String>();
				profile = (Talent) items.get(i);

				inner.add(Long.toString(profile.getId()));
				inner.add(parse(profile.getPhotos(), profile.getId())[0]);
				inner.add(profile.getName());
				inner.add(parse(profile.getBirthdate()));
				inner.add(profile.getProfileType().toString());

				set.add(inner);
			}
		}

		return set;
	}
	private static ArrayList<ArrayList<String>> searchProject (ArrayList<String> args)
	{
		Project criteria = new Project();
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;
		ArrayList<Project> items;

		criteria.setTitle(args.get(0));
		criteria.setType(Type.identifierOf(args.get(1)));
		criteria.setProducer(args.get(2));
		criteria.setDirector(args.get(3));

		items = getInterfaceDAL().retriveSet(criteria);

		if(!items.isEmpty())
		{
			for (int i = 0; i < items.size(); i++)
			{
				inner = new ArrayList<String>();
				criteria = (Project) items.get(i);

				inner.add(Long.toString(criteria.getId()));
				inner.add(criteria.getTitle());
				inner.add(criteria.getType().toString());
				inner.add(criteria.getProducer());
				inner.add(criteria.getState().toString());

				set.add(inner);
			}
		}

		return set;
	}

	private static ArrayList<ArrayList<String>> getTalent (String id)
	{
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;
		Talent talent = new Talent();
		String[] photos = new String[3];
		String[] videos = new String[1];
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<String[]> photoFuture = null;
		Future<String[]> videoFuture = null;

		talent.setId(Long.parseLong(id));
		getInterfaceDAL().retrive(talent);

		if (talent.getVideos() != null)
		{
			photoFuture = executor.submit(new Callable<String[]>()
			{
				public String[] call () throws Exception
				{
					return parse(talent.getPhotos(), talent.getId());
				}
			});
			videoFuture = executor.submit(new Callable<String[]>()
			{
				public String[] call () throws Exception
				{
					return parse(talent.getVideos(), Long.toString(talent.getId()));
				}
			});
		}

		inner = new ArrayList<String>();

		inner.add(talent.getName() + " " + talent.getSurname());
		inner.add(talent.getBirthdate().toString());
		inner.add(talent.getSex().toString());

		values.add(inner);

		inner = new ArrayList<String>();

		inner.add(talent.getProfileType().toString());
		inner.add(parse(talent.getStature()));
		inner.add(talent.getPhysique().toString());
		inner.add(talent.getSkinTone().toString());
		inner.add(talent.getHairTexture().toString());
		inner.add(talent.getHairColor());
		inner.add(talent.getEyeColor());

		values.add(inner);

		inner = new ArrayList<String>();

		inner.add((talent.getShirtSize()!=null) ? talent.getShirtSize().toString() : "");
		inner.add((talent.getPantSize()!=null) ? talent.getPantSize() : "");
		inner.add((talent.getShoeSize()==0) ? "" : Float.toString(talent.getShoeSize()));

		values.add(inner);

		inner = new ArrayList<String>();

		inner.add(parse(talent.getMobilePhone()));
		inner.add((talent.getSocialNetworks().get("Facebook") == null) ? "" : talent.getSocialNetworks().get("Facebook"));
		inner.add((talent.getSocialNetworks().get("Twitter") == null) ? "" : talent.getSocialNetworks().get("Twitter"));
		inner.add(talent.getEmail());
		inner.add(parse(talent.getAddress()));
		inner.add(parse(talent.getHomePhone()));
		inner.add((talent.getSocialNetworks().get("Instagram") == null) ? "" : talent.getSocialNetworks().get("Instagram"));
		inner.add(talent.getStatus().toString());
		inner.add((talent.getAddress().get("Provincia") == null) ? "" : talent.getAddress().get("Provincia"));

		values.add(inner);

		inner = new ArrayList<String>();

		inner.add(parse(talent.getArtisticSkills()));
		inner.add(parse(talent.getDominatedLanguages()));
		inner.add((talent.getAcademicLevel() == null) ? "" : talent.getAcademicLevel());
		inner.add((talent.getArtisticExperience() == null) ? "" : talent.getArtisticExperience());
		inner.add((talent.getScheduleAvailable() == null) ? "" : talent.getScheduleAvailable());
		inner.add((talent.getHobbies() == null) ? "" : talent.getHobbies());

		values.add(inner);


		inner = new ArrayList<String>();

		if (talent.getVideos() != null)
		{
			try
			{
				while(!photoFuture.isDone() || !videoFuture.isDone())
				{
					Thread.sleep(1000);
				}

				photos = photoFuture.get().clone();
				videos = videoFuture.get().clone();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			inner.add(photos[2]);
			inner.add(photos[1]);
			inner.add(photos[0]);
			inner.add(videos[0]);
		}
		else
		{
			inner.add(PHOTOS_PATHS.get("Rostro" + talent.getId()));
			inner.add(PHOTOS_PATHS.get("Medio Cuerpo" + talent.getId()));
			inner.add(PHOTOS_PATHS.get("Cuerpo Completo" + talent.getId()));
			inner.add(VIDEOS_PATHS.get("Demo" + talent.getId()));
		}

		values.add(0, inner);

		executor.shutdown();

		return values;
	}
	private static ArrayList<ArrayList<String>> getProject (String id)
	{
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		Project project = new Project();
		ArrayList<String> inner;
		Talent talent;

		project.setId(Long.parseLong(id));
		getInterfaceDAL().retrive(project);

		inner = new ArrayList<String>();

		inner.add(project.getTitle());
		inner.add(project.getType().toString());
		inner.add(project.getProducer());
		inner.add(project.getDirector());
		inner.add(project.getState().toString());

		values.add(inner);

		for (Entry<Category, ArrayList<String>> role : project.getRoles().entrySet())
		{
			for (String name : role.getValue())
			{
				inner = new ArrayList<String>();

				inner.add(parse(new SimpleEntry<Category, String>(role.getKey(), name)));

				values.add(inner);

				if (!project.getSelecteds().containsKey(name) && project.getPreselecteds().containsKey(name))
				{
					for (Talent talen : project.getPreselecteds().get(name))
					{
						inner = new ArrayList<String>();

						inner.add(Long.toString(talen.getId()));
						inner.add(parse(talen.getPhotos(), talen.getId())[0]);
						inner.add(talen.getName());
						inner.add(parse(talen.getBirthdate()));
						inner.add(talen.getProfileType().toString());

						values.add(inner);
					}
				}
				else if (project.getSelecteds().containsKey(name))
				{
					talent = project.getSelecteds().get(name);

					inner = new ArrayList<String>();

					inner.add(Long.toString(talent.getId()));
					inner.add(parse(talent.getPhotos(), talent.getId())[0]);
					inner.add(talent.getName());
					inner.add(parse(talent.getBirthdate()));
					inner.add(talent.getProfileType().toString());

					values.add(inner);
				}
			}
		}

		return values;
	}
	private static ArrayList<ArrayList<String>> getCinemaProject (String id)
	{
		CinemaProject cinemaProject = new CinemaProject();
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;

		cinemaProject.setId(Long.parseLong(id));
		getInterfaceDAL().retrive(cinemaProject);

		inner = new ArrayList<String>();

		inner.add(cinemaProject.getTitle());

		values.add(inner);

		if(!cinemaProject.getSequences().isEmpty())
		{
			for (Sequence thumbnail : cinemaProject.getSequences())
			{
				inner = new ArrayList<String>();

				inner.add(Long.toString(thumbnail.getId()));
				inner.add(Integer.toString(thumbnail.getNumber()));
				inner.add(thumbnail.getFilmingDate().toString());
				inner.add(thumbnail.getLocation());
				inner.add(Integer.toString(thumbnail.getScriptPage()));

				values.add(inner);
			}
		}

		return values;
	}
	private static ArrayList<ArrayList<String>> getSequence (String id)
	{
		Sequence sequence = new Sequence();
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;
		Talent talent;

		sequence.setId(Long.parseLong(id));
		getInterfaceDAL().retrive(sequence);

		inner = new ArrayList<String>();

		inner.add((sequence.getNumber() == 0) ? "" : Integer.toString(sequence.getNumber()));
		inner.add((sequence.getDescription() == null) ? "" : sequence.getDescription());
		inner.add(sequence.getFilmingDate().toString());
		inner.add(sequence.getLocation());
		inner.add(sequence.getLocationType().toString());
		inner.add(sequence.getDayMoment().toString());
		inner.add(Integer.toString(sequence.getScriptPage()));
		inner.add((sequence.getScriptDay() == 0) ? "" : Integer.toString(sequence.getScriptDay()));

		values.add(inner);

		if(!sequence.getSelecteds().isEmpty())
		{
			for (Entry<String, Talent> selected : sequence.getSelecteds().entrySet())
			{
				inner = new ArrayList<String>();

				inner.add(selected.getKey());

				values.add(inner);

				inner = new ArrayList<String>();
				talent = selected.getValue();

				inner.add(Long.toString(talent.getId()));
				inner.add(parse(talent.getPhotos(), talent.getId())[0]);
				inner.add(talent.getName());
				inner.add(parse(talent.getBirthdate()));
				inner.add(talent.getProfileType().toString());

				values.add(inner);
			}
		}

		return values;
	}
	@Deprecated
	private static boolean enterTalent (ArrayList<ArrayList<String>> values)
	{
		Talent talent = new Talent();

		talent.setBirthdate(new Date());

		talent.setName(values.get(0).get(0));
		talent.setSurname(values.get(0).get(1));
		talent.getBirthdate().setDate(Integer.parseInt(values.get(0).get(2)));
		talent.getBirthdate().setMonth(Integer.parseInt(values.get(0).get(3)) - 1);
		talent.getBirthdate().setYear(Integer.parseInt(values.get(0).get(4)) - 1900);
		talent.setSex(Sex.identifierOf(values.get(0).get(5)));

		talent.setSocialNetworks(new TreeMap<String, String>());
		talent.setAddress(new TreeMap<String, String>());

		talent.setMobilePhone(values.get(1).get(0));
		talent.setHomePhone(values.get(1).get(1));
		talent.getSocialNetworks().put("Facebook", values.get(1).get(2));
		talent.getSocialNetworks().put("Instagram", values.get(1).get(3));
		talent.getSocialNetworks().put("Twitter", values.get(1).get(4));
		talent.getAddress().put("Numero", values.get(1).get(5));
		talent.getAddress().put("Calle", values.get(1).get(6));
		talent.getAddress().put("Sector", values.get(1).get(7));
		talent.getAddress().put("Provincia", values.get(1).get(8));
		talent.setEmail(values.get(1).get(9));

		talent.setPhysique(Physique.identifierOf(values.get(2).get(0)));
		talent.setSkinTone(SkinTone.identifierOf(values.get(2).get(1)));
		talent.setStature(parse(values.get(2).get(2), values.get(2).get(3)));
		talent.setHairTexture(HairTexture.identifierOf(values.get(2).get(4)));
		talent.setEyeColor(values.get(2).get(5));
		talent.setProfileType(ProfileType.identifierOf(values.get(2).get(6)));
		talent.setHairColor(values.get(2).get(7));

		talent.setShirtSize(ShirtSize.identifierOf(values.get(3).get(0)));
		talent.setPantSize(values.get(3).get(1));
		talent.setShoeSize(Float.parseFloat((values.get(3).get(2).isEmpty()) ? "0" : values.get(3).get(2)));

		talent.setAcademicLevel(values.get(4).get(0));
		talent.setHobbies(values.get(4).get(1));
		talent.setArtisticExperience(values.get(4).get(2));
		talent.setScheduleAvailable(values.get(4).get(3));

		talent.setArtisticSkills(values.get(5));
		talent.setDominatedLanguages(values.get(6));

		talent.setPhotos(new TreeMap<String, BufferedImage>());
		talent.setVideos(new TreeMap<String, InputStream>());

		addPhotoEntry(talent.getPhotos(), "Rostro", parse(Paths.get(values.get(7).get(0))));
		if (!values.get(7).get(1).equals(" ..."))
			addPhotoEntry(talent.getPhotos(), "Medio Cuerpo", parse(Paths.get(values.get(7).get(1))));
		if (!values.get(7).get(2).equals(" ..."))
			addPhotoEntry(talent.getPhotos(), "Cuerpo Completo", parse(Paths.get(values.get(7).get(2))));
		if (!values.get(7).get(3).equals(" ..."))
			talent.getVideos().put("Demo",  parse(Paths.get(values.get(7).get(3))));

		return getInterfaceDAL().enter(talent);
	}
	private static boolean enterProject (ArrayList<ArrayList<String>> values)
	{
		Project project = new Project();

		project.setTitle(values.get(0).get(0));
		project.setType(Type.identifierOf(values.get(0).get(1)));
		project.setProducer(values.get(0).get(2));
		project.setDirector(values.get(0).get(3));

		project.setRoles(new TreeMap<Category, ArrayList<String>>());
		for (ArrayList<String> roles : new ArrayList<ArrayList<String>>(values.subList(1, values.size())))
		{
			if(!project.getRoles().containsKey(Category.identifierOf(roles.get(0))))
				project.getRoles().put(Category.identifierOf(roles.get(0)), new ArrayList<String>());

			project.getRoles().get(Category.identifierOf(roles.get(0))).add(roles.get(1));
		}

		return getInterfaceDAL().enter(project);
	}
	@Deprecated
	private static boolean enterSequence (ArrayList<ArrayList<String>> values)
	{
		CinemaProject cinema = new CinemaProject();
		Sequence sequence = new Sequence();

		sequence.setFilmingDate(new Date());

		cinema.setId(Long.parseLong(values.get(0).get(0)));
		sequence.setNumber(Integer.parseInt(values.get(0).get(1)));
		sequence.getFilmingDate().setDate(Integer.parseInt(values.get(0).get(2)));
		sequence.getFilmingDate().setMonth(Integer.parseInt(values.get(0).get(3)) - 1);
		sequence.getFilmingDate().setYear(Integer.parseInt(values.get(0).get(4)) - 1900);
		sequence.setLocation(values.get(0).get(5));
		sequence.setDescription(values.get(0).get(6));

		sequence.setLocationType(LocationType.identifierOf(values.get(1).get(0)));
		sequence.setDayMoment(DayMoment.identifierOf(values.get(1).get(1)));
		sequence.setScriptPage(Integer.parseInt(values.get(1).get(2)));
		sequence.setScriptDay(Integer.parseInt(values.get(1).get(3)));

		sequence.setSelecteds(new TreeMap<String, Talent>());
		for (ArrayList<String> selecteds : new ArrayList<ArrayList<String>>(values.subList(2, values.size())))
		{
			if(!sequence.getSelecteds().containsKey(selecteds.get(0)))
				sequence.getSelecteds().put(selecteds.get(0), new Talent());

			sequence.getSelecteds().get(selecteds.get(0)).setId(Long.parseLong(selecteds.get(1)));
		}

		cinema.setSequences(new ArrayList<Sequence>());
		cinema.getSequences().add(sequence);

		return getInterfaceDAL().enter(cinema);
	}

	@Deprecated
	private static boolean updateTalent (ArrayList<ArrayList<String>> values)
	{
		Talent talent = new Talent();

		talent.setId(Long.parseLong(values.get(0).get(0)));

		talent.setBirthdate(new Date());

		talent.setName(values.get(1).get(0));
		talent.setSurname(values.get(1).get(1));
		talent.getBirthdate().setDate(Integer.parseInt(values.get(1).get(2)));
		talent.getBirthdate().setMonth(Integer.parseInt(values.get(1).get(3)) - 1);
		talent.getBirthdate().setYear(Integer.parseInt(values.get(1).get(4)) - 1900);
		talent.setSex(Sex.identifierOf(values.get(1).get(5)));

		talent.setSocialNetworks(new TreeMap<String, String>());
		talent.setAddress(new TreeMap<String, String>());

		talent.setMobilePhone(values.get(2).get(0));
		talent.setHomePhone(values.get(2).get(1));
		talent.getSocialNetworks().put("Facebook", values.get(2).get(2));
		talent.getSocialNetworks().put("Instagram", values.get(2).get(3));
		talent.getSocialNetworks().put("Twitter", values.get(2).get(4));
		talent.getAddress().put("Numero", values.get(2).get(5));
		talent.getAddress().put("Calle", values.get(2).get(6));
		talent.getAddress().put("Sector", values.get(2).get(7));
		talent.getAddress().put("Provincia", values.get(2).get(8));
		talent.setEmail(values.get(2).get(9));

		talent.setPhysique(Physique.identifierOf(values.get(3).get(0)));
		talent.setSkinTone(SkinTone.identifierOf(values.get(3).get(1)));
		talent.setStature(parse(values.get(3).get(2), values.get(3).get(3)));
		talent.setHairTexture(HairTexture.identifierOf(values.get(3).get(4)));
		talent.setEyeColor(values.get(3).get(5));
		talent.setProfileType(ProfileType.identifierOf(values.get(3).get(6)));
		talent.setHairColor(values.get(3).get(7));

		talent.setShirtSize(ShirtSize.identifierOf(values.get(4).get(0)));
		talent.setPantSize(values.get(4).get(1));
		talent.setShoeSize(Float.parseFloat((values.get(4).get(2).isEmpty()) ? "0" : values.get(4).get(2)));

		talent.setAcademicLevel(values.get(5).get(0));
		talent.setHobbies(values.get(5).get(1));
		talent.setArtisticExperience(values.get(5).get(2));
		talent.setScheduleAvailable(values.get(5).get(3));

		talent.setArtisticSkills(values.get(6));
		talent.setDominatedLanguages(values.get(7));

		talent.setPhotos(new TreeMap<String, BufferedImage>());
		talent.setVideos(new TreeMap<String, InputStream>());

		addPhotoEntry(talent.getPhotos(), "Rostro", parse(Paths.get(values.get(8).get(0))));
		addPhotoEntry(talent.getPhotos(), "Medio Cuerpo", parse(Paths.get(values.get(8).get(1))));
		addPhotoEntry(talent.getPhotos(), "Cuerpo Completo", parse(Paths.get(values.get(8).get(2))));
		talent.getVideos().put("Demo",  parse(Paths.get(values.get(8).get(3))));

		return getInterfaceDAL().update(talent);
	}
	private static boolean updateProject (ArrayList<ArrayList<String>> values)
	{
		Project project = new Project();

		project.setId(Long.parseLong(values.get(0).get(0)));

		project.setTitle(values.get(1).get(0));
		project.setType(Type.identifierOf(values.get(1).get(1)));
		project.setProducer(values.get(1).get(2));
		project.setDirector(values.get(1).get(3));

		project.setRoles(new TreeMap<Category, ArrayList<String>>());
		for (ArrayList<String> roles : new ArrayList<ArrayList<String>>(values.subList(2, values.size())))
		{
			if(!project.getRoles().containsKey(Category.identifierOf(roles.get(0))))
				project.getRoles().put(Category.identifierOf(roles.get(0)), new ArrayList<String>());

			project.getRoles().get(Category.identifierOf(roles.get(0))).add(roles.get(1));
		}

		return getInterfaceDAL().update(project);
	}

	public static boolean preselect (String talentId, String projectId, String roleName)
	{
		return getInterfaceDAL().preselect(Long.parseLong(talentId), Long.parseLong(projectId), roleName);
	}

	public static boolean present (String projectId, String dir)
	{
		Project project = new Project();
		Path photoPath;
		Path videoPath;
		Path dirProject;
		Path dirRole;
		Path dirTalent;

		project.setId(Long.parseLong(projectId));
		project = getInterfaceDAL().retrive(project);

		try
		{
			dirProject = Paths.get(dir, project.getTitle());

			if (Files.notExists(dirProject))
				dirProject = Files.createDirectory(dirProject);

			for (Entry<String, ArrayList<Talent>> preselected : project.getPreselecteds().entrySet())
			{
				dirRole = Paths.get(dirProject.toString(), preselected.getKey());

				if (Files.notExists(dirRole))
					dirRole = Files.createDirectory(dirRole);

				for (Talent talent : preselected.getValue())
				{
					talent = getInterfaceDAL().retrive(talent);

					dirTalent = Paths.get(dirRole.toString(), (talent.getName() + " " + talent.getSurname() +
													" - " + parse(talent.getBirthdate())));

					if (Files.notExists(dirTalent))
						dirTalent = Files.createDirectory(dirTalent);

					if (!PHOTOS_PATHS.containsKey("Cuerpo Completo" + talent.getId()))
					{
						for (Entry<String, BufferedImage> photo : talent.getPhotos().entrySet())
						{
							photoPath = Paths.get(dirTalent.toString(), (photo.getKey() + ".jpg"));
							Files.deleteIfExists(photoPath);
							photoPath = Files.createFile(photoPath);

							ImageIO.write(photo.getValue(), "jpg", photoPath.toFile());
						}
					}
					else 
					{
						photoPath = Paths.get(dirTalent.toString(), "Rostro.jpg");
						
						Files.copy(Paths.get(PHOTOS_PATHS.get("Rostro" + talent.getId())), photoPath, 
								   StandardCopyOption.REPLACE_EXISTING);

						photoPath = Paths.get(dirTalent.toString(), "Medio Cuerpo.jpg");
						
						Files.copy(Paths.get(PHOTOS_PATHS.get("Medio Cuerpo" + talent.getId())), 
								   photoPath, StandardCopyOption.REPLACE_EXISTING);

						photoPath = Paths.get(dirTalent.toString(), "Cuerpo Completo.jpg");
						
						Files.copy(Paths.get(PHOTOS_PATHS.get("Cuerpo Completo" + talent.getId())), 
								   photoPath, StandardCopyOption.REPLACE_EXISTING);
					}

					if (!CatalogsHandler.VIDEOS_PATHS.containsKey("Demo" + talent.getId()))
					{
						for (Entry<String, InputStream> video : talent.getVideos().entrySet())
						{
							videoPath = Paths.get(dirTalent.toString(), (video.getKey() + ".mp4"));

							Files.copy(video.getValue(), videoPath, StandardCopyOption.REPLACE_EXISTING);
						}
					}
					else
					{
						videoPath = Paths.get(dirTalent.toString(), "Demo.mp4");

						Files.copy(Paths.get(VIDEOS_PATHS.get("Demo" + talent.getId())), videoPath, 
								   StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public static boolean capture (String title, String number, String dir, Rectangle rec)
	{
		Path dirProject;
		Path capturePath;

		try 
		{
			dirProject = Paths.get(dir, title);
			capturePath = Paths.get(dirProject.toString(), ("Secuencia No " + number + ".png"));
			Files.deleteIfExists(capturePath);
			capturePath = Files.createFile(capturePath);
 			
 			Thread.sleep(1000);

    		BufferedImage capture = (new Robot()).createScreenCapture(rec);
 
        	ImageIO.write(capture, "png", capturePath.toFile());
    	} 
    	catch (Exception e) 
    	{
        	e.printStackTrace();

        	return false;
    	}

       return true;
	}

	public static String[][] getSuppressed ()
	{
		Item suppressed = null;
		ArrayList<Item> items = getInterfaceDAL().retriveSet(suppressed);
		String[][] suppresseds = new String[items.size()][4];

		for(int i = 0; i < suppresseds.length; i++)
		{
			suppressed = items.get(i);

			suppresseds[i][0] = "" + suppressed.getId();
			suppresseds[i][1] = suppressed.getCreationDate().toString();

			if (suppressed instanceof Talent)
			{
				suppresseds[i][2] = ((Talent) suppressed).getName();
				suppresseds[i][3] = "Talento";
			}
			else
			{
				suppresseds[i][2] = ((Project) suppressed).getTitle();
				suppresseds[i][3] = "Proyecto";
			}
		}

		return suppresseds;
	}

	public static boolean select (String talentId, String projectId, String roleName)
	{
		return getInterfaceDAL().select(Long.parseLong(talentId), Long.parseLong(projectId), roleName);
	}

	public static boolean remove (String id)
	{
		return getInterfaceDAL().suppress(Long.parseLong(id));
	}
	public static boolean restore (String id)
	{
		return getInterfaceDAL().unsuppress(Long.parseLong(id));
	}

	public static boolean switchStatus (String id, String currentStatus)
	{
		return getInterfaceDAL().switchStatus(Long.parseLong(id), Status.identifierOf(currentStatus));
	}

	public static boolean terminate (String id)
	{
		return getInterfaceDAL().terminate(Long.parseLong(id));
	}

	@Deprecated
	private static String parse (Date birthdate)
	{
		int age = ((new Date()).getYear() + 1900) - (birthdate.getYear() + 1900);

		return Integer.toString(age);
	}
	@Deprecated
	private static Date parse (int age)
	{
		Date current = new Date();
		int year = (current.getYear() + 1900) - age;

		return (new Date(year, current.getMonth(), current.getDay()));
	}
	private static float parse (String feet, String inches)
	{
		float integer = Float.parseFloat(feet);
		float decimal = Float.parseFloat(inches) / 12;

		return integer + decimal;
	}
	private static String parse (float stature)
	{
		if (stature == 0)
			return "";

		int feet = (int) stature;
		int inches = Math.round((stature - feet) * 12);

		return (feet + "' " + inches + "''");
	}
	public static InputStream parse (BufferedImage photo)
	{

		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();

		try
		{
			ImageIO.write(photo,"jpg", baOutputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		byte[] bytes = baOutputStream.toByteArray();

		return new ByteArrayInputStream(bytes);
	}
	public static TreeMap<String, BufferedImage> parse (String name, InputStream inputStream)
	{
		TreeMap<String, BufferedImage> photos = new TreeMap<String, BufferedImage>();

		try
		{
			photos.put(name , ImageIO.read(inputStream));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return photos;
	}
	private static String[] parse (TreeMap<String, BufferedImage> photos, long id)
	{
		String[] photosPath = new String[photos.size()];
		File photoFile;
		int i = 0;

		try
		{
			for (Entry<String, BufferedImage> photo : photos.entrySet())
			{
				if (PHOTOS_PATHS.containsKey(photo.getKey() + id) &&
					!Files.notExists(Paths.get(PHOTOS_PATHS.get(photo.getKey() + id))))
				{
					photosPath[i] = PHOTOS_PATHS.get(photo.getKey() + id);
				}
				else
				{
					photoFile = File.createTempFile(photo.getKey() + id, ".jpg");
					photoFile.deleteOnExit();

					ImageIO.write(photo.getValue(), "jpg", photoFile);

					photosPath[i] = photoFile.getAbsolutePath();
					PHOTOS_PATHS.put(photo.getKey() + id, photosPath[i]);
				}
				++i;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return photosPath;
	}
	public static InputStream parse (byte[] bytes)
	{
		return new ByteArrayInputStream(bytes);
	}
	private static InputStream parse (Path path)
	{
		try
		{
			return parse(Files.readAllBytes(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();

			return null;
		}
	}
	private static String[] parse (TreeMap<String,InputStream> videos, String id)
	{
		String[] videosPath = new String[videos.size()];
		Path videoPath;
		int i = 0;

		try
		{
			for (Entry<String, InputStream> video : videos.entrySet())
			{
				if (VIDEOS_PATHS.containsKey(video.getKey() + id) &&
					!Files.notExists(Paths.get(VIDEOS_PATHS.get(video.getKey() + id))))
				{
					videosPath[i] = VIDEOS_PATHS.get(video.getKey() + id);
				}
				else
				{
					videoPath = Files.createTempFile(video.getKey() + id, ".mp4");
					videoPath.toFile().deleteOnExit();

					Files.copy(video.getValue(), videoPath, StandardCopyOption.REPLACE_EXISTING);

					videosPath[i] = videoPath.toAbsolutePath().toString();
					VIDEOS_PATHS.put(video.getKey() + id, videosPath[i]);
				}
				++i;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return videosPath;
	}
	private static String parse (String phone)
	{
		if (phone == null)
			return "";

		String phonePattern = "(%s) %s-%s";

		return String.format(phonePattern, phone.substring(0,3), phone.substring(3,6), phone.substring(6));
	}
	private static String parse (TreeMap<String, String> address)
	{
		String addressPattern = "";

		if (address.get("Calle") != null)
			addressPattern = addressPattern.concat("C/ %s");
		if (address.get("Numero") != null)
			addressPattern = addressPattern.concat(" #%s");
		if (address.get("Sector") != null)
			addressPattern = addressPattern.concat(", %s");

		return String.format(addressPattern, address.get("Calle"), address.get("Numero"), address.get("Sector"));
	}
	private static String parse (ArrayList<String> list)
	{
		if (list.isEmpty())
			return "";

		String strList = list.get(0);

		for(int i = 1; i < list.size(); i++)
		{
			strList = strList.concat(", " + list.get(i));
		}

		return strList;
	}
	public static String parse (Entry<Category, String> role)
	{
		String strRole = role.getValue() + "[" + role.getKey().toString() + "]";

		return strRole;
	}

	public static void addPhotoEntry (TreeMap<String, BufferedImage> photos, String name, InputStream inputStream)
	{
		try
		{
			photos.put(name , ImageIO.read(inputStream));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
