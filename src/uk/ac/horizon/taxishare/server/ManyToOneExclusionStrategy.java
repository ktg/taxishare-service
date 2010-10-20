package uk.ac.horizon.taxishare.server;

import javax.persistence.ManyToOne;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ManyToOneExclusionStrategy implements ExclusionStrategy
{
	@Override
	public boolean shouldSkipClass(final Class<?> arg0)
	{
		return false;
	}

	@Override
	public boolean shouldSkipField(final FieldAttributes fieldAttributes)
	{
		return fieldAttributes.getAnnotation(ManyToOne.class) != null;
	}
}
