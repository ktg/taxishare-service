package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.internal.NotNull;

@Entity
public class Destination
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	@NotNull
	@Column(length = 10)
	private String postcode;

	public Destination()
	{
		
	}
	
	public Destination(final String name, final String postcode)
	{
		this.name = name;
		this.postcode = postcode;
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getPostcode()
	{
		return postcode;
	}
	
	public void setPostcode(final String postcode)
	{
		this.postcode = postcode;
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
