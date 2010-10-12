package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.sun.istack.internal.NotNull;

@Entity
public class Instance
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private Destination destination;
	@OneToMany
	private Collection<Taxi> taxis;
	
	@NotNull
	private String number;

	public Instance()
	{

	}

	public long getID()
	{
		return id;
	}
	
	public Destination getDestination()
	{
		return destination;
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public void add(final Taxi taxi)
	{
		taxis.add(taxi);
	}
	
	public Iterable<Taxi> getTaxis()
	{
		return taxis;
	}

	public void toJSON(final Writer writer) throws IOException
	{
		writer.append("{");

		writer.append("\"id\": ");
		writer.append(Long.toString(id));
		writer.append(",");

		writer.append("\"number\": ");
		writer.append("\"");
		writer.append(number);
		writer.append("\"");
		writer.append(",");

		writer.append("\"destination\": ");
		destination.toJSON(writer);
		writer.append(",");

		writer.append("\"taxis\": ");
		writer.append("[");
		boolean comma = false;
		for (final Taxi taxi : taxis)
		{
			if (comma)
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
