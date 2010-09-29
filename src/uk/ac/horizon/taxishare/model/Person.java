package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;

public class Person
{
	private long id;
	private String name;
	private String number;
	
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
