package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import uk.ac.horizon.taxishare.model.Destination;
import uk.ac.horizon.taxishare.model.Instance;
import uk.ac.horizon.taxishare.model.Person;
import uk.ac.horizon.taxishare.model.Taxi;

public class Server
{
	public static boolean canAdd(final Taxi taxi)
	{
		return false;
	}

	public static Taxi getAvailableTaxi(final EntityManager entityManager, final int destinationID, final int instanceID) // time
	{
		final Query query = entityManager
				.createQuery("SELECT taxi FROM Taxi WHERE taxi.destinationID = :destID AND taxi.instanceID = :instID");
		query.setParameter("destID", destinationID);
		query.setParameter("instID", instanceID);
		@SuppressWarnings("unchecked")
		final Iterable<Taxi> taxis = query.getResultList();
		for (final Taxi taxi : taxis)
		{
			if (taxi.getAvailableSpace() > 0) { return taxi; }
		}

		// create taxi
		final Instance instance = entityManager.find(Instance.class, instanceID);
		final Taxi taxi = new Taxi();
		taxi.setInstance(instance);
		instance.add(taxi);

		entityManager.getTransaction().begin();
		entityManager.persist(taxi);
		entityManager.merge(instance);
		entityManager.getTransaction().commit();
		return taxi;
	}

	public static Destination getDestination(final EntityManager entityManager, final String destinationName)
	{
		final String postcode = Destination.formatPostcode(destinationName);
		if (Destination.isPostcode(postcode))
		{
			final Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE d.postcode = :value");
			query.setParameter("value", postcode);
			final Destination destination = (Destination) query.getSingleResult();
			if (destination != null) { return destination; }
			final Destination newDest = new Destination(null, postcode);
			entityManager.getTransaction().begin();
			entityManager.persist(newDest);
			entityManager.getTransaction().commit();
			return newDest;
		}

		final Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE LOWER(d.name) = :value");
		query.setParameter("value", destinationName.toLowerCase());
		return (Destination) query.getSingleResult();
	}

	public static Person getPerson(final EntityManager entityManager, final String number)
	{
		final Query query = entityManager.createQuery("SELECT p FROM Person p WHERE number =:value");
		query.setParameter("value", number);
		return (Person) query.getSingleResult();
	}

	public static void joinTaxi(final EntityManager entityManager, final String number, final String taxiID)
	{
		final Person person = getPerson(entityManager, number);
		if (person != null)
		{
			person.setTaxi(null);
			entityManager.getTransaction().begin();
			entityManager.merge(person);
			entityManager.getTransaction().commit();
		}
	}

	public static void leaveTaxi(final EntityManager entityManager, final String number)
	{
		final Person person = getPerson(entityManager, number);
		if (person != null)
		{
			person.setTaxi(null);
			entityManager.getTransaction().begin();
			entityManager.merge(person);
			entityManager.getTransaction().commit();
		}
	}

	public static void requestTaxi(final EntityManager entityManager, final int instanceID, final String name,
			final String number, final String destinationName)
	{
		final Destination destination = Server.getDestination(entityManager, destinationName);
		if (destination != null)
		{
			final Taxi taxi = getAvailableTaxi(entityManager, destination.getId(), instanceID);

			assert (taxi != null);

			Person person = getPerson(entityManager, number);
			entityManager.getTransaction().begin();
			if (person != null)
			{
				person.setName(name);
				person.setTaxi(taxi);
				entityManager.merge(person);
			}
			else
			{
				person = new Person(name, number);
				person.setTaxi(taxi);
				entityManager.persist(person);
			}
			entityManager.getTransaction().commit();
		}
	}
}
