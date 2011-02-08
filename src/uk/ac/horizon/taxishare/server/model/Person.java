package uk.ac.horizon.taxishare.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Person
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	// @NotNull
	private String name;
	@Column(length = 16, unique = true)
	private String number;
	@ManyToOne
	private Taxi taxi;

	private int spaces;
	
	public Person()
	{
	}

	public Person(final String name, final String number, final int spaces)
	{
		this.name = name;
		this.number = number;
		this.spaces = spaces;
	}

	public int getId()
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

	public Taxi getTaxi()
	{
		return taxi;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setTaxi(final Taxi taxi)
	{
		if (this.taxi != null)
		{
			this.taxi.remove(this);
		}
		this.taxi = taxi;
		if (taxi != null)
		{
			this.taxi.add(this);
		}
	}

	public void setSpaces(int spaces)
	{
		this.spaces = spaces;
	}

	public int getSpaces()
	{
		return spaces;
	}
}
