package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sun.istack.internal.NotNull;

@Entity
public class Taxi
{
	public enum Status
	{
		unconfirmed, confirmed, arriving, arrived, left
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@NotNull
	private Destination destination;

	private Collection<Person> people;
	@Temporal(TemporalType.TIMESTAMP)
	private Date pickupTime;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date requestTime;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date arrivalTime;
	private int totalSpace;
	@ManyToOne
	private Instance instance;
	private float predictedCost;
	private Status status;

	public Taxi()
	{

	}

	public void add(Person person)
	{
		people.add(person);
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	public int getTotalSpace()
	{
		return totalSpace;
	}
	
	public Date getArrivalTime()
	{
		return arrivalTime;
	}
	
	public Date getPickupTime()
	{
		return pickupTime;
	}
	
	public Iterable<Person> getPeople()
	{
		return people;
	}
	
	public float getPredictedCost()
	{
		return predictedCost;
	}
	
	public Date getRequestTime()
	{
		return requestTime;
	}
	
	public Destination getDestination()
	{
		return destination;
	}
	
	public void toJSON(final Writer writer) throws IOException
	{
		writer.append("{");

		writer.append("\"id\": ");
		writer.append(Long.toString(id));
		writer.append(",");

		writer.append("\"destination\": ");
		destination.toJSON(writer);
		writer.append(",");

		writer.append("\"people\": ");
		writer.append("[");
		boolean comma = false;
		for (final Person person : people)
		{
			if (comma)
			{
				writer.append(",");
			}
			else
			{
				comma = true;
			}
			person.toJSON(writer);
		}
		writer.append("]");
		writer.append(",");

		writer.append("\"requestTime\": ");
		writer.append(Long.toString(requestTime.getTime()));
		writer.append(",");

		writer.append("\"arrivalTime\": ");
		writer.append(Long.toString(arrivalTime.getTime()));
		writer.append(",");

		writer.append("\"pickupTime\": ");
		writer.append(Long.toString(pickupTime.getTime()));
		writer.append(",");

		writer.append("\"totalSpace\": ");
		writer.append(Integer.toString(totalSpace));
		writer.append(",");

		writer.append("\"spaceLeft\": ");
		writer.append(Integer.toString(totalSpace - people.size()));
		writer.append(",");

		writer.append("\"predictedCost\": ");
		writer.append(Float.toString(predictedCost));
		writer.append(",");

		writer.append("\"status\": ");
		writer.append("\"");
		writer.append(status.toString());
		writer.append("\"");

		writer.append("}");
	}

	public long getId()
	{
		return id;
	}

	public Instance getInstance()
	{
		return instance;		
	}
	
	public void setInstance(Instance instance)
	{
		this.instance = instance;		
	}
}