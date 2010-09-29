package uk.ac.horizon.taxishare.model;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;

public class Taxi
{
	public enum Status
	{
		unconfirmed,
		confirmed,
		arriving,
		arrived,
		left
	}
	
	private long id;
	private Destination destination;
	private Collection<Person> people;
	private Date pickupTime;
	private Date requestTime;	
	private Date arrivalTime;
	private int totalSpace;
	private float predictedCost;
	private Status status;
	
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
		for(final Person person: people)
		{
			if(comma)
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
}
