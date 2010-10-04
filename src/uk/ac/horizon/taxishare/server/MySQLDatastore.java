package uk.ac.horizon.taxishare.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class MySQLDatastore implements Datastore
{
	private Connection connection = null;
	
	@Override
	public <T> T get(final Class<T> clazz, final long id)
	{
		try
		{
			final Connection connection = getConnection();
			if (connection != null)
			{
				final Statement statement = connection.createStatement();
				try
				{
					final String query = "SELECT * FROM " + clazz.getSimpleName() + " WHERE id=" + id + ";";						
					final ResultSet result = statement.executeQuery(query);
					while (result.next())
					{

					}
				}
				finally
				{
					statement.close();
				}
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> Iterable<T> list(final Class<T> clazz, final Map<String, String> conditions)
	{
		try
		{
			final Connection connection = getConnection();
			if (connection != null)
			{
				final Statement statement = connection.createStatement();
				try
				{
					String query;	
					if(conditions.isEmpty())
					{
						query = "SELECT * FROM " + clazz.getSimpleName() + ";";
					}
					else
					{
						query = "SELECT * FROM " + clazz.getSimpleName() + " WHERE ";
						for(String key: conditions.keySet())
						{
							query += key + "=" + conditions.get(key); 
						}
						query += ";";
					}
					final ResultSet result = statement.executeQuery(query);
					while (result.next())
					{

					}
				}
				finally
				{
					statement.close();
				}
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private Connection getConnection() throws Exception
	{
		if(connection == null)
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql:///taxishare", "ec2-user", "c4rsh4re");
	
			if (!connection.isClosed())
			{
				System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
				return connection;
			}
		}
		return connection;
	}

	@Override
	public <T> T add(T object, long id)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T delete(Class<T> clazz, long id)
	{
		// TODO Auto-generated method stub
		return null;
	}
}