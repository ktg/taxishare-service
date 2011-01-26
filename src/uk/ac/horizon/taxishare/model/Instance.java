package uk.ac.horizon.taxishare.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.sun.istack.internal.NotNull;

/**
 * Represents a single instance of a
 * 
 * @author Kevin Glover
 */
@Entity
public class Instance
{
	@Id
	@GeneratedValue
	private int id;
	@NotNull
	private Location location;
	private Collection<Location> destinations = new ArrayList<Location>();
	@OneToMany(mappedBy = "instance")
	private Collection<Taxi> taxis = new ArrayList<Taxi>();
	private boolean enabled = true;

	@NotNull
	private String number;

	public Instance()
	{

	}

	public void add(final Location destination)
	{
		destinations.add(destination);
	}

	public void add(final Taxi taxi)
	{
		taxis.add(taxi);
	}

	public Iterable<Location> getDestinations()
	{
		return destinations;
	}

	public boolean getEnabled()
	{
		return enabled;
	}

	public int getId()
	{
		return id;
	}

	public Location getLocation()
	{
		return location;
	}

	public String getNumber()
	{
		return number;
	}

	public Iterable<Taxi> getTaxis()
	{
		return taxis;
	}

	public void setLocation(final Location location)
	{
		this.location = location;
	}

	public void setNumber(final String number)
	{
		this.number = number;
	}
}
