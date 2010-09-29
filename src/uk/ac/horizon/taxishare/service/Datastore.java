package uk.ac.horizon.taxishare.service;

import java.util.Collection;

public interface Datastore
{
	public <T extends Object> T getItem(final long id, final Class<T> clazz);
	public <T extends Object> Collection<T> list(final Class<T> clazz);
}
