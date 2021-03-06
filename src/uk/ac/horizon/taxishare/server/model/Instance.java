package uk.ac.horizon.taxishare.server.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	private Location location;
	private Collection<Location> destinations = new ArrayList<Location>();
	@OneToMany(mappedBy = "instance")
	private Collection<Taxi> taxis = new ArrayList<Taxi>();

	private Collection<TaxiCompany> companies = new ArrayList<TaxiCompany>();
	private boolean enabled = true;

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

	public void remove(final Taxi taxi)
	{
		taxis.remove(taxi);
	}
	
	public void add(final TaxiCompany taxiCompany)
	{
		companies.add(taxiCompany);
	}

	public Iterable<TaxiCompany> getCompanies()
	{
		return companies;
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
