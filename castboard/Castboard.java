package castboard;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import javax.swing.JFrame;
import castboard.view.MasterFrame;

public class Castboard
{
	public static void main(String[] args)
	{
		try
		{
			System.setProperty("file.encoding", "UTF8");
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		JFrame masterFrame = MasterFrame.getInstance();
		masterFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
