package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person
{
	private long id;
	private String name;
	private String number;
	
	public Person(final long id, final String name, final String number)
	{
		this.id = id;
		this.name = name;
		this.number = number;
	}
	
	public Person(final ResultSet resultSet) throws SQLException
	{
		id = resultSet.getLong("id");
		name = resultSet.getString("name");
		number = resultSet.getString("number");
	}
	
	public void toJSON(final Writer writer) throws IOException
	{
		writer.append("{");
		
		writer.append("\"id\": ");
		writer.append(Long.toString(id));
		writer.append(",");
		
		writer.append("\"name\": ");
		writer.append("\"");
		writer.append(name);
		writer.append("\"");		
		writer.append(",");
		
		writer.append("\"number\": ");
		writer.append("\"");		
		writer.append(number);
		writer.append("\"");		
		
		writer.append("}");
	}
}
