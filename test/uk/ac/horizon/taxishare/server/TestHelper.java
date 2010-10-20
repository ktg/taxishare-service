package uk.ac.horizon.taxishare.server;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import uk.ac.horizon.taxishare.model.Destination;
import uk.ac.horizon.taxishare.model.Instance;
import uk.ac.horizon.taxishare.model.Taxi;

public class TestHelper
{
	private static EntityManager entityManager;
	private static Taxi taxi;
	private static Destination destination;
	private static Destination source;
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
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare-setup");
			entityManager = factory.createEntityManager();
			destination = new Destination("Home", "NG9 2WB");
			source = new Destination("EMCC", "NG7 2RJ");

			instance = new Instance();
			instance.setDestination(source);
			instance.setNumber("");

			taxi = new Taxi();
			taxi.setTotalSpace(4);
			taxi.setRequestTime(new Date());
			taxi.setDestination(destination);
			taxi.setPredictedCost(20.0f);
			taxi.setInstance(instance);

			instance.add(taxi);

			entityManager.getTransaction().begin();
			entityManager.persist(source);
			entityManager.persist(destination);
			entityManager.persist(taxi);
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
			entityManager.remove(destination);
			entityManager.remove(source);
			entityManager.getTransaction().commit();
			setup = false;
		}
	}
}
