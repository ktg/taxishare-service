package uk.ac.horizon.taxishare.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import uk.ac.horizon.taxishare.server.model.Instance;
import uk.ac.horizon.taxishare.server.model.Location;
import uk.ac.horizon.taxishare.server.model.Person;
import uk.ac.horizon.taxishare.server.model.Taxi;

public class Server
{
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	
	private final static SimpleDateFormat[] timeFormats = new SimpleDateFormat[] { new SimpleDateFormat("h:mma"), new SimpleDateFormat("H:mm"), new SimpleDateFormat("ha"), new SimpleDateFormat("H")};
	
	private static final String textURL = "";
	private static final String EsendexUsername = "";
	private static final String EsendexPassword = "";
	private static final String EsendexAccount = "";

	public static boolean canAdd(final Taxi taxi)
	{
		return false;
	}

	public static Person createPerson(final EntityManager entityManager, final String name, final String number)
	{
		final Query query = entityManager.createQuery("SELECT p FROM Person p WHERE p.number = :value");
		query.setParameter("value", number);
		if (query.getResultList().size() == 0)
		{
			logger.info("Create person " + name + ": " + number);
			final Person person = new Person(name, number);
			entityManager.getTransaction().begin();
			entityManager.persist(person);
			entityManager.getTransaction().commit();
			return person;
		}
		else if (query.getResultList().size() == 1)
		{
			final Person person = (Person) query.getSingleResult();
			logger.info("Get person " + person.getName() + ": " + number);
			if (!person.getName().equals(name))
			{
				person.setName(name);
				entityManager.getTransaction().begin();
				entityManager.merge(person);
				entityManager.getTransaction().commit();
			}
			return person;
		}
		else
		{
			logger.warning("More than one result for a phone number: " + query.getMaxResults());
		}
		return null;
	}

	public static Taxi getAvailableTaxi(final EntityManager entityManager, final Location destination,
			final int instanceID, final int spaces, final Date time)
	{
		// TODO Check time
		final Query query = entityManager
				.createQuery("SELECT taxi FROM Taxi taxi WHERE taxi.destination.id = :destID AND taxi.instance.id = :instID");
		query.setParameter("destID", destination.getId());
		query.setParameter("instID", instanceID);
		@SuppressWarnings("unchecked")
		final Iterable<Taxi> taxis = query.getResultList();
		for (final Taxi taxi : taxis)
		{
			if (taxi.getAvailableSpace() >= spaces) { return taxi; }
		}

		// create taxi
		logger.info("Create taxi to " + destination.getName());
		final Instance instance = entityManager.find(Instance.class, instanceID);
		final Taxi taxi = new Taxi();
		taxi.setInstance(instance);
		taxi.setDestination(destination);
		taxi.setPickupTime(time);
		taxi.setTotalSpace(4);
		taxi.setRequestTime(new Date());
		instance.add(taxi);

		entityManager.getTransaction().begin();
		entityManager.persist(taxi);
		entityManager.merge(instance);
		entityManager.getTransaction().commit();
		return taxi;
	}

	public static Location getLocation(final EntityManager entityManager, final String destinationName)
	{
		final String postcode = Location.formatPostcode(destinationName);
		if (Location.isPostcode(postcode))
		{
			final Query query = entityManager.createQuery("SELECT l FROM Location l WHERE l.postcode = :value");
			query.setParameter("value", postcode);
			final Location destination = (Location) query.getSingleResult();
			if (destination != null) { return destination; }
			final Location newDest = new Location(null, postcode);
			entityManager.getTransaction().begin();
			entityManager.persist(newDest);
			entityManager.getTransaction().commit();
			return newDest;
		}

		final Query query = entityManager.createQuery("SELECT l FROM Location l WHERE LOWER(l.name) = :value");
		query.setParameter("value", destinationName.toLowerCase());
		return (Location) query.getSingleResult();
	}

	public static Person getPerson(final EntityManager entityManager, final String number)
	{
		final Query query = entityManager.createQuery("SELECT p FROM Person p WHERE p.number = :value");
		query.setParameter("value", number);
		try
		{
			return (Person) query.getSingleResult();
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	public static Taxi getTaxi(final EntityManager entityManager, final String taxiID)
	{
		try
		{
			int id;
			if (taxiID.toUpperCase().startsWith("TAXI"))
			{
				id = Integer.parseInt(taxiID.substring(4));
			}
			else
			{
				id = Integer.parseInt(taxiID);
			}

			logger.info("Find TAXI" + id);

			return entityManager.find(Taxi.class, id);
		}
		catch (final Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public static void joinTaxi(final EntityManager entityManager, final String name, final String number,
			final String taxiID)
	{
		try
		{
			final Person person = createPerson(entityManager, name, number);
			final Taxi taxi = getTaxi(entityManager, taxiID);
			logger.info("Join taxi " + taxiID + "=" + taxi + ", " + number + "=" + person);
			if (person != null && taxi != null)
			{
				final Taxi oldTaxi = person.setTaxi(taxi);
				entityManager.getTransaction().begin();
				if (oldTaxi != null)
				{
					oldTaxi.remove(person);
					entityManager.merge(oldTaxi);
				}
				taxi.add(person);
				entityManager.merge(person);
				entityManager.merge(taxi);
				entityManager.getTransaction().commit();
			}
		}
		catch (final Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static void leaveTaxi(final EntityManager entityManager, final String number)
	{
		final Person person = getPerson(entityManager, number);
		if (person != null)
		{
			final Taxi oldTaxi = person.setTaxi(null);
			if (oldTaxi != null)
			{
				oldTaxi.remove(person);
				entityManager.getTransaction().begin();
				entityManager.merge(person);
				entityManager.merge(oldTaxi);
				entityManager.getTransaction().commit();
			}
		}
	}

	public static void requestTaxi(final EntityManager entityManager, final int instanceID, final String name,
			final String number, final String destinationName, final int spaces, final Date time)
	{
		logger.info("Request Taxi for " + name + " to " + destinationName);

		final Location destination = getLocation(entityManager, destinationName);
		if (destination != null)
		{
			final Taxi taxi = getAvailableTaxi(entityManager, destination, instanceID, spaces, time);

			assert (taxi != null);

			final Person person = createPerson(entityManager, name, number);
			assert (person != null);
			person.setTaxi(taxi);
			taxi.add(person);
			entityManager.getTransaction().begin();
			entityManager.merge(person);
			entityManager.merge(taxi);
			entityManager.getTransaction().commit();
		}
	}
	
	public static void sendMessage(final Person person, final String message)
	{
		logger.info("Send text to " + person.getName() + " (" + person.getNumber() + "): \"" + message + "\"");
		
	    try
		{
			String data = URLEncoder.encode("Username", "UTF-8") + "=" + URLEncoder.encode(EsendexUsername, "UTF-8");
			data += "&" + URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(EsendexPassword, "UTF-8");
			data += "&" + URLEncoder.encode("Account", "UTF-8") + "=" + URLEncoder.encode(EsendexAccount, "UTF-8");
			data += "&" + URLEncoder.encode("Recipient", "UTF-8") + "=" + URLEncoder.encode(person.getNumber(), "UTF-8");			
			data += "&" + URLEncoder.encode("Body", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");			

			// Send data
			URL url = new URL(textURL);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
			    logger.info(line);
			}
			wr.close();
			rd.close();
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public static Date parseTime(final String time)
	{
		Date parsed = null;
		String timeString = time.trim().toUpperCase().replaceAll(" ", "");
		
		for(SimpleDateFormat timeFormat: timeFormats)
		{
				
			try
			{
				parsed = timeFormat.parse(timeString);
				if(parsed != null)
				{
					final Calendar parsedTime = Calendar.getInstance();
					parsedTime.setTime(parsed);

					final Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, parsedTime.get(Calendar.HOUR_OF_DAY));
					cal.set(Calendar.MINUTE, parsedTime.get(Calendar.MINUTE));
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					
					return cal.getTime();
				}
			}
			catch(final Exception e)
			{				
			}
		}
		
		logger.warning("Couldn't parse time: " + time);

		return null;
	}
}