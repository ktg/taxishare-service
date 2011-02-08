package uk.ac.horizon.taxishare.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import uk.ac.horizon.taxishare.server.model.Instance;
import uk.ac.horizon.taxishare.server.model.Location;
import uk.ac.horizon.taxishare.server.model.Person;
import uk.ac.horizon.taxishare.server.model.Taxi;
import uk.ac.horizon.taxishare.server.model.TaxiCompany;

public class TestHelper
{
	private static EntityManager entityManager;
	private static Taxi taxi;
	private static Taxi taxi2;
	private static Location destination;
	private static Location destination2;
	private static Location source;
	private static Person person;
	private static Instance instance;

	private static boolean setup = false;

	public static int getDestinationID()
	{
		return destination.getId();
	}

	public static int getInstanceID()
	{
		return instance.getId();
	}

	public static int getTaxiID()
	{
		return taxi.getId();
	}

	public static EntityManager setUp() throws Exception
	{
		if (!setup)
		{
			final Map<String, String> persistProperties = new HashMap<String, String>();
			// persistProperties.put(PersistenceUnitProperties.DDL_GENERATION,
			// PersistenceUnitProperties.DROP_AND_CREATE);

			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare", persistProperties);
			entityManager = factory.createEntityManager();
			destination = new Location("Home", "NG9 2WB");
			source = new Location("EMCC", "NG7 2RJ");

			instance = new Instance();
			instance.setLocation(source);
			instance.setNumber("07798898175");

			final TaxiCompany taxiCompany = new TaxiCompany("DG Cars", "01159607607");

			taxi = new Taxi();
			taxi.setTotalSpace(4);
			taxi.setRequestTime(new Date());
			taxi.setDestination(destination);
			taxi.setPredictedCost(20.0f);
			taxi.setCompany(taxiCompany);
			taxi.setInstance(instance);

			person = new Person("Kevin", "07796698175",1);
			person.setTaxi(taxi);

			destination2 = new Location("Station", "NG2 3AQ");

			taxi2 = new Taxi();
			taxi2.setTotalSpace(4);
			taxi2.setRequestTime(new Date());
			taxi2.setDestination(destination2);
			taxi2.setPredictedCost(20.0f);
			taxi2.setInstance(instance);

			instance.add(taxi);
			instance.add(taxi2);
			instance.add(destination);
			instance.add(destination2);

			entityManager.getTransaction().begin();
			entityManager.persist(taxiCompany);
			entityManager.persist(source);
			entityManager.persist(destination);
			entityManager.persist(destination2);
			entityManager.persist(taxi);
			entityManager.persist(taxi2);
			entityManager.persist(person);
			entityManager.persist(instance);
			entityManager.getTransaction().commit();
			setup = true;
		}

		return entityManager;
	}

	public static void tearDown() throws Exception
	{
		if (setup)
		{
			entityManager.getTransaction().begin();
			entityManager.remove(instance);
			entityManager.remove(taxi);
			entityManager.remove(taxi2);
			entityManager.remove(person);
			entityManager.remove(destination);
			entityManager.remove(destination2);
			entityManager.remove(source);
			entityManager.getTransaction().commit();
			setup = false;
		}
	}
}