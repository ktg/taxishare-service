package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;

public class Company
{
	private long id;
	private String name;
	private String phone;
	
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

		writer.append("\"phone\": ");
		writer.append("\"");
		writer.append(phone);
		writer.append("\"");
		writer.append(",");
		
		writer.append("}");
	}
}
