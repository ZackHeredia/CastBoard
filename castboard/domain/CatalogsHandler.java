package castboard.domain;

import static castboard.domain.Enumeration.*;

import castboard.dataaccess.InterfaceDAL;
import castboard.view.MasterFrame;
import java.util.ArrayList;
import java.util.Date;

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
		ArrayList<ArrayList<String>> set;

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

		for (int i = 0; i < items.size(); i++)
		{
			inner = new ArrayList<String>();

			if (i < separator)
			{
				inner.add(Long.toString(((Project) items.get(i)).getId()));
				inner.add(((Project) items.get(i)).getTitle());
				inner.add(((Project) items.get(i)).getType().toString());
				inner.add(((Project) items.get(i)).getProducer());
				inner.add(((Project) items.get(i)).getState().toString());

				set.add(inner);
			}
			else if (i == separator)
				set.add(null);
			else
			{
				inner.add(Long.toString(((Talent) items.get(i)).getId()));
				//inner.add(parse(((Talent) items.get(i)).getPhotos()));
				inner.add(((Talent) items.get(i)).getName());
				inner.add(parse(((Talent) items.get(i)).getBirthdate()));
				inner.add(((Talent) items.get(i)).getProfileType().toString());

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

			inner.add(Long.toString(((Talent) items.get(i)).getId()));
			//inner.add(parse(((Talent) items.get(i)).getPhotos()));
			inner.add(((Talent) items.get(i)).getName());
			inner.add(parse(((Talent) items.get(i)).getBirthdate()));
			inner.add(((Talent) items.get(i)).getProfileType().toString());

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

			inner.add(Long.toString(((Project) items.get(i)).getId()));
			inner.add(((Project) items.get(i)).getTitle());
			inner.add(((Project) items.get(i)).getType().toString());
			inner.add(((Project) items.get(i)).getProducer());
			inner.add(((Project) items.get(i)).getState().toString());

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
}