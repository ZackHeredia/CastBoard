package castboard.domain;

import static castboard.domain.Enumeration.*;

import castboard.dataaccess.InterfaceDAL;
import castboard.view.MasterFrame;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

public abstract class CatalogsHandler
{
	public static final int FRONT_SET = 0;
	public static final int TALENT_SET = 1;
	public static final int PROJECT_SET = 2;

	private static final TreeMap<String, String> PHOTOS_PATHS = new TreeMap<String, String>();

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
		ArrayList<Project> items;
		ArrayList<String> inner;

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
	public static String[] parse (TreeMap<String, BufferedImage> photos, long id)
	{
		String[] photosPath = new String[photos.size()];
		File photoFile;
		int i = 0;

		try
		{
			for (Entry<String, BufferedImage> photo : photos.entrySet())
			{
				if (PHOTOS_PATHS.containsKey(photo.getKey() + id))
					photosPath[i] = PHOTOS_PATHS.get(photo.getKey() + id);
				else
				{
					photoFile = File.createTempFile(photo.getKey() + id, ".jpg");
					photoFile.deleteOnExit();

					ImageIO.write(photo.getValue(), "jpg", photoFile);

					photosPath[i] = photoFile.getAbsolutePath();
					PHOTOS_PATHS.put(photo.getKey() + id, photoFile.getAbsolutePath());
					i++;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return photosPath;
	}
}