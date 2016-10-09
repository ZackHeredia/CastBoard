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

		return set;
	}
	private static ArrayList<ArrayList<String>> getTalentSet ()
	{
		Talent thumbnail = null;
		ArrayList<Talent> items = getInterfaceDAL().retriveSet(thumbnail);
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;

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

		return set;
	}
	private static ArrayList<ArrayList<String>> getProjectSet ()
	{
		Project thumbnail = null;
		ArrayList<Project> items = getInterfaceDAL().retriveSet(thumbnail);
		ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
		ArrayList<String> inner;

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

		return set;
	}

	@Deprecated
	private static String parse (Date birthdate)
	{
		int age = ((new Date()).getYear() + 1900) - (birthdate.getYear() + 1900);

		return Integer.toString(age);
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
	public static TreeMap<String, BufferedImage> parse (InputStream inputStream)
	{
		TreeMap<String, BufferedImage> photos = new TreeMap<String, BufferedImage>();
		
		try
		{
			photos.put("Rostro", ImageIO.read(inputStream));
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
				photoFile = File.createTempFile(photo.getKey() + id, ".jpg");
				photoFile.deleteOnExit();

				ImageIO.write(photo.getValue(), "jpg", photoFile);

				photosPath[i] = photoFile.getAbsolutePath();
				i++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return photosPath;
	}
}