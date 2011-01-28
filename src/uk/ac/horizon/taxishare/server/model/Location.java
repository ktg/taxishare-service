package uk.ac.horizon.taxishare.server.model;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.internal.NotNull;

@Entity
public class Location
{
	private static final Pattern postcodeRegEx = Pattern.compile("[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}");

	public static String formatPostcode(final String postcode)
	{
		final String result = postcode.toUpperCase();
		if (postcode.indexOf(' ') == -1) { return result.substring(0, result.length() - 3) + " "
				+ result.substring(result.length() - 3); }
		return result;
	}

	public static boolean isPostcode(final String destination)
	{
		return postcodeRegEx.matcher(destination).matches();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(unique = true)
	private String name;

	@NotNull
	@Column(length = 10, unique = true)
	private String postcode;

	public Location()
	{

	}

	public Location(final String name, final String postcode)
	{
		this.name = name;
		this.postcode = formatPostcode(postcode);
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setName(final String name)
	{
		this.name = name;
	}
}