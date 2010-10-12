package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.internal.NotNull;

@Entity
public class Person
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull
	private String name;
	@NotNull
	private String number;

	public Person()
	{
		
	}
	
	public Person(final String name, final String number)
	{
		this.name = name;
		this.number = number;
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
	
	public long getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getNumber()
	{
		return number;
	}
}
