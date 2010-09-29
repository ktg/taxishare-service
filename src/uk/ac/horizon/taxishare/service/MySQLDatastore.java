package uk.ac.horizon.taxishare.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

public class MySQLDatastore implements Datastore
{
	@Override
	public <T> T getItem(final long id, final Class<T> clazz)
	{
		try
		{
			final Connection connection = getConnection();
			if (connection != null)
			{
				try
				{
					final Statement statement = connection.createStatement();
					try
					{
						final String query = "SELECT * FROM " + clazz.getSimpleName() + " WHERE id=" + id;						
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
				finally
				{
					connection.close();
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
	public <T> Collection<T> list(final Class<T> clazz)
	{
		try
		{
			final Connection connection = getConnection();
			if (connection != null)
			{
				try
				{
					final Statement statement = connection.createStatement();
					try
					{
						final String query = "SELECT * FROM " + clazz.getSimpleName();						
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
				finally
				{
					connection.close();
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
		Connection connection = null;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql:///taxishare", "ec2-user", "c4rsh4re");

		if (!connection.isClosed())
		{
			System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
			return connection;
		}

		return null;
	}
}
