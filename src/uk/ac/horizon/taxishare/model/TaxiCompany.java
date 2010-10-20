package uk.ac.horizon.taxishare.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.internal.NotNull;

@Entity
public class TaxiCompany
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull
	private String name;
	@NotNull
	private String number;

	public TaxiCompany()
	{

	}

	public TaxiCompany(final String name, final String number)
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
}