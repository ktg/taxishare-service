package uk.ac.horizon.taxishare.model;

import java.util.ArrayList;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Destination destination;
	@OneToMany(mappedBy = "instance")
	private final Collection<Taxi> taxis = new ArrayList<Taxi>();
	private boolean enabled = true;

	@NotNull
	private String number;

	public Instance()
	{

	}

	public void add(final Taxi taxi)
	{
		taxis.add(taxi);
	}

	public Destination getDestination()
	{
		return destination;
	}
	
	public boolean getEnabled()
	{
		return enabled;
	}

	public int getId()
	{
		return id;
	}

	public String getNumber()
	{
		return number;
	}

	public Iterable<Taxi> getTaxis()
	{
		return taxis;
	}

	public void setDestination(final Destination destination)
	{
		this.destination = destination;
	}

	public void setNumber(final String number)
	{
		this.number = number;
	}
}
