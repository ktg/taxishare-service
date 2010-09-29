package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;

public class Destination
{
	private long id;
	private String name;
	private String postcode;
	
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
