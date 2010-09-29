package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

public class Instance
{
	private long id;
	private Destination destination;
	private Collection<Taxi> taxis;
	private String phone;
	
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
