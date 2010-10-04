package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class Instance
{
	private long id;
	private Destination destination;
	private Collection<Taxi> taxis;
	private String phone;
	
	public Instance()
	{
		
	}
	
	public Instance(final ResultSet resultSet) throws SQLException
	{
		id = resultSet.getLong("id");
		long destinationID = resultSet.getLong("destinationID");
		phone = resultSet.getString("phone");
	}
	
	public void add(Taxi taxi)
	{
		taxis.add(taxi);
	}
	
	public void toJSON(final Writer writer) throws IOException
	{
		writer.append("{");
		
		writer.append("\"id\": ");
		writer.append(Long.toString(id));
		writer.append(",");

		writer.append("\"phone\": ");
		writer.append("\"");
		writer.append(phone);
		writer.append("\"");
		writer.append(",");
		
		writer.append("\"destination\": ");
		destination.toJSON(writer);
		writer.append(",");
		
		writer.append("\"taxis\": ");
		writer.append("[");
		boolean comma = false;
		for(Taxi taxi: taxis)
		{
			if(comma)
			{
				writer.append(",");
			}
			else 
			{
				comma = true;
			}
			taxi.toJSON(writer);
		}
		writer.append("]");
		
		writer.append("}");
	}	
}
