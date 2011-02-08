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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import uk.ac.horizon.taxishare.server.model.Instance;
import uk.ac.horizon.taxishare.server.model.Location;
import uk.ac.horizon.taxishare.server.model.Person;
import uk.ac.horizon.taxishare.server.model.Taxi;
import uk.ac.horizon.taxishare.server.model.Taxi.Status;

public class Server
{
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	private final static SimpleDateFormat[] timeFormats = new SimpleDateFormat[] { new SimpleDateFormat("h:mma"),
																					new SimpleDateFormat("H:mm"),
																					new SimpleDateFormat("ha"),
																					new SimpleDateFormat("H") };

	private static final String textURL = "http://www.esendex.com/secure/messenger/formpost/SendSMS.aspx";
	private static final String EsendexUsername = "ben@growlingfish.com";
	private static final String EsendexPassword = "spam.1234";
	private static final String EsendexAccount = "EX0074453";

	private static EntityManagerFactory factory = null;

	public static boolean canAdd(final Taxi taxi)
	{
		return false;
	}

	public static EntityManager createEntityManager()
	{
		if (factory == null)
		{
			factory = Persistence.createEntityManagerFactory("taxishare");
		}
		return factory.createEntityManager();
	}

	public static Location getLocation(final EntityManager entityManager, final Instance instance, final String destinationName)
	{
		final String postcode = Location.formatPostcode(destinationName);
		if (Location.isPostcode(postcode))
		{
			final Query query = entityManager.createQuery("SELECT l FROM Location l WHERE l.postcode = :value");
			query.setParameter("value", postcode);
			try
			{
				final Location destination = (Location) query.getSingleResult();
				if (destination != null) { return destination; }
			}
			catch(Exception e)
			{
			}
			final Location newDest = new Location(postcode.replaceAll(" ", ""), postcode);
			entityManager.getTransaction().begin();
			entityManager.persist(newDest);
			if(instance != null)
			{
				instance.add(newDest);				
				entityManager.merge(instance);
			}
			entityManager.getTransaction().commit();
			return newDest;
		}

		final Query query = entityManager.createQuery("SELECT l FROM Location l WHERE LOWER(l.name) = :value");
		query.setParameter("value", destinationName.toLowerCase());
		return (Location) query.getSingleResult();
	}

	public static Taxi joinTaxi(final EntityManager entityManager, final String name, final String number,
			final String taxiID)
	{
		try
		{
			final Person person = createPerson(entityManager, name, number, 1);
			final Taxi taxi = getTaxi(entityManager, taxiID);
			logger.info("Join taxi " + taxiID + "=" + taxi + ", " + number + "=" + person);
			joinTaxi(entityManager, taxi, person);
		}
		catch (final Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	public static Taxi leaveTaxi(final EntityManager entityManager, final String number)
	{
		final Person person = getPerson(entityManager, number);
		return leaveTaxi(entityManager, person);
	}

	public static Date parseTime(final String time)
	{
		Date parsed = null;
		final String timeString = time.trim().toUpperCase().replaceAll(" ", "");

		for (final SimpleDateFormat timeFormat : timeFormats)
		{

			try
			{
				parsed = timeFormat.parse(timeString);
				if (parsed != null)
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
			catch (final Exception e)
			{
			}
		}

		logger.warning("Couldn't parse time: " + time);

		return null;
	}

	public static Taxi requestTaxi(final EntityManager entityManager, final Instance instance, final String name,
			final String number, final String destinationName, final int spaces, final Date time)
	{
		if(spaces > 1)
		{
			logger.info("Request Taxi for " + name + "(+" + (spaces - 1) + ") to " + destinationName);			
		}
		else
		{
			logger.info("Request Taxi for " + name + " to " + destinationName);
		}

		final Location destination = getLocation(entityManager, instance, destinationName);
		if (destination != null)
		{
			final Person person = createPerson(entityManager, name, number, spaces);
			
			if(person.getTaxi() != null && destination.equals(person.getTaxi().getDestination()))
			{
				person.getTaxi().refreshSpaces();
				entityManager.getTransaction().begin();
				entityManager.merge(person.getTaxi());
				entityManager.getTransaction().commit();
				return person.getTaxi();
			}
			
			final Taxi taxi = getAvailableTaxi(entityManager, destination, instance.getId(), spaces, time);
			
			System.out.println("Spaces: " + person.getSpaces());

			joinTaxi(entityManager, taxi, person);
			return taxi;
		}
		return null;
	}

	public static void sendMessage(final String phoneNumber, final String message)
	{
		logger.info("Send text to " + phoneNumber + ": \"" + message + "\"");

		try
		{
			String data = URLEncoder.encode("Username", "UTF-8") + "=" + URLEncoder.encode(EsendexUsername, "UTF-8");
			data += "&" + URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(EsendexPassword, "UTF-8");
			data += "&" + URLEncoder.encode("Account", "UTF-8") + "=" + URLEncoder.encode(EsendexAccount, "UTF-8");
			data += "&" + URLEncoder.encode("Recipient", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8");
			data += "&" + URLEncoder.encode("Body", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

			// Send data
			final URL url = new URL(textURL);
			final URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// String line;
			// while ((line = rd.readLine()) != null)
			// {
			// //logger.info(line);
			// }
			wr.close();
			rd.close();
		}
		catch (final Exception e)
		{
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private static Person createPerson(final EntityManager entityManager, final String name, final String number, final int spaces)
	{
		final Query query = entityManager.createQuery("SELECT p FROM Person p WHERE p.number = :value");
		query.setParameter("value", number);
		if (query.getResultList().size() == 0)
		{
			logger.info("Create person " + name + ": " + number);
			final Person person = new Person(name, number, spaces);
			entityManager.getTransaction().begin();
			entityManager.persist(person);
			entityManager.getTransaction().commit();
			return person;
		}
		else if (query.getResultList().size() == 1)
		{
			final Person person = (Person) query.getSingleResult();
			logger.info("Get person " + person.getName() + ": " + number);
			person.setName(name);
			person.setSpaces(spaces);
			entityManager.getTransaction().begin();
			entityManager.merge(person);
			entityManager.getTransaction().commit();
			return person;
		}
		else
		{
			logger.warning("More than one result for a phone number: " + query.getResultList().size());
		}
		return null;
	}

	private static Taxi getAvailableTaxi(final EntityManager entityManager, final Location destination,
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
			if(taxi.getStatus() == Status.left || taxi.getStatus() == Status.cancelled)
			{
				continue;
			}
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

	private static Person getPerson(final EntityManager entityManager, final String number)
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

	private static Taxi getTaxi(final EntityManager entityManager, final String taxiID)
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

	private static void joinTaxi(final EntityManager entityManager, final Taxi taxi, final Person person)
	{
		assert person != null;
		assert taxi != null;
		if(person.getTaxi() == taxi)
		{
			taxi.refreshSpaces();
		}
		logger.info(person.getName() + " joining TAXI" + taxi.getId());
		if (person.getTaxi() != null)
		{
			leaveTaxi(entityManager, person);
		}
		person.setTaxi(taxi);
		entityManager.getTransaction().begin();
		entityManager.merge(person);
		entityManager.merge(taxi);
		entityManager.getTransaction().commit();
	}

	private static void removeTaxi(final EntityManager entityManager, final Taxi taxi)
	{
		assert taxi != null;
		Instance instance = taxi.getInstance();
		instance.remove(taxi);
		taxi.setInstance(null);
		logger.info("Removing TAXI" + taxi.getId());
		entityManager.getTransaction().begin();
		entityManager.merge(instance);
		entityManager.remove(taxi);
		entityManager.getTransaction().commit();		
	}
	
	private static Taxi leaveTaxi(final EntityManager entityManager, final Person person)
	{
		assert person != null;
		final Taxi oldTaxi = person.getTaxi();
		if (oldTaxi != null)
		{
			logger.info(person.getName() + " leaving TAXI" + oldTaxi.getId());
			person.setTaxi(null);
			entityManager.getTransaction().begin();
			entityManager.merge(person);
			entityManager.merge(oldTaxi);
			entityManager.getTransaction().commit();			
			if (oldTaxi.getStatus() == Status.unconfirmed && oldTaxi.getUsedSpace() == 0)
			{
				removeTaxi(entityManager, oldTaxi);
			}
		}
		return oldTaxi;
	}
}