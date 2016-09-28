package castboard.dataaccess;

import castboard.domain.CatalogsHandler;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class InterfaceDAL
{
	private Connection connection ;
	private static InterfaceDAL instance = null;
	
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

		if (connection == null)				 
		{
			try
			{
				DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
				connection = DriverManager.getConnection(url);
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
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			//TODO: Notify controller
			CatalogsHandler.notifyException(e);
			e.printStackTrace();
		}

		instance = null;
	}
}