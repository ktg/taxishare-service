package uk.ac.horizon.taxishare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.sun.istack.internal.NotNull;

@Entity
public class Person
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull
	private String name;
	@NotNull
	@Column(length = 16, unique = true)
	private String number;
	@ManyToOne
	private Taxi taxi;

	public Person()
	{

	}

	public Person(final String name, final String number)
	{
		this.name = name;
		this.number = number;
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

	public Taxi setTaxi(final Taxi taxi)
	{
		final Taxi oldTaxi = this.taxi;
		if (this.taxi != null)
		{
			this.taxi.remove(this);
		}
		this.taxi = taxi;
		if (taxi != null)
		{
			this.taxi.add(this);
		}
		return oldTaxi;
	}
}
