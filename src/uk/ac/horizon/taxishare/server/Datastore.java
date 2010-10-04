package uk.ac.horizon.taxishare.server;

import java.util.Map;

public interface Datastore
{
	public <T extends Object> T add(final T object, final long id);
	public <T extends Object> T delete(final Class<T> clazz, final long id);	
	public <T extends Object> T get(final Class<T> clazz, final long id);
	public <T extends Object> Iterable<T> list(final Class<T> clazz, final Map<String, String> conditions);
}
