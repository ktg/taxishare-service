package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Destination
{
	private long id;
	private String name;
	private String postcode;
	
	public Destination(final long id, final String name, final String postcode)
	{
		this.id = id;
		this.name = name;
		this.postcode = postcode;
	}
	
	public Destination(final ResultSet resultSet) throws SQLException
	{
		id = resultSet.getLong("id");
		name = resultSet.getString("name");
		postcode = resultSet.getString("postcode");
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
		
		writer.append("\"postcode\": ");
		writer.append("\"");		
		writer.append(postcode);
		writer.append("\"");		
		
		writer.append("}");
	}
}
