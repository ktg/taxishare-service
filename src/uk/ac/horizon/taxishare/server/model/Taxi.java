package uk.ac.horizon.taxishare.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Taxi
{
	public enum Status
	{
		unconfirmed, confirmed, arriving, arrived, left, cancelled
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;

	// @NotNull
	private Location destination;

	@OneToMany(mappedBy = "taxi")
	private Collection<Person> people = new ArrayList<Person>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date pickupTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date requestTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date arrivalTime;

	private int totalSpace;

	private int usedSpace = 0;
	
	private TaxiCompany company;

	@ManyToOne
	private Instance instance;

	private float predictedCost;

	private Status status = Status.unconfirmed;

	public Taxi()
	{

	}

	void add(final Person person)
	{	
		if(people.add(person))
		{
			usedSpace += person.getSpaces();			
		}
	}

	public Date getArrivalTime()
	{
		return arrivalTime;
	}

	public int getAvailableSpace()
	{
		return totalSpace - usedSpace;
	}

	public TaxiCompany getCompany()
	{
		return company;
	}

	public Location getDestination()
	{
		return destination;
	}

	public int getId()
	{
		return id;
	}

	public Instance getInstance()
	{
		return instance;
	}

	public Iterable<Person> getPeople()
	{
		return people;
	}

	public int getUsedSpace()
	{
		return usedSpace;
	}

	public Date getPickupTime()
	{
		return pickupTime;
	}

	public float getPredictedCost()
	{
		return predictedCost;
	}

	public Date getRequestTime()
	{
		return requestTime;
	}

	public Status getStatus()
	{
		return status;
	}

	public int getTotalSpace()
	{
		return totalSpace;
	}

	void remove(final Person person)
	{
		if(people.remove(person))
		{
			refreshSpaces();	
		}
	}

	public void setArrivalTime(final Date arrivalTime)
	{
		this.arrivalTime = arrivalTime;
	}

	public void setCompany(final TaxiCompany company)
	{
		this.company = company;
	}

	public void setDestination(final Location destination)
	{
		this.destination = destination;
	}

	public void setInstance(final Instance instance)
	{
		this.instance = instance;
	}

	public void setPickupTime(final Date pickupTime)
	{
		this.pickupTime = pickupTime;
	}

	public void setPredictedCost(final float predictedCost)
	{
		this.predictedCost = predictedCost;
	}

	public void setRequestTime(final Date requestTime)
	{
		this.requestTime = requestTime;
	}

	public void setStatus(final Status status)
	{
		this.status = status;
	}

	public void setTotalSpace(final int totalSpace)
	{
		this.totalSpace = totalSpace;
	}

	public void refreshSpaces()
	{
		usedSpace = 0;
		for(Person remainingPerson: people)
		{
			usedSpace += remainingPerson.getSpaces();
		}	
	}
}