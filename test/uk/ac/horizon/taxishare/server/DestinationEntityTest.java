package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.model.Location;

public class DestinationEntityTest
{
	private EntityManager entityManager;

	@After
	public void after() throws Exception
	{
		TestHelper.tearDown();
	}

	@Before
	public void before() throws Exception
	{
		entityManager = TestHelper.setUp();
	}

	@Test
	public void testNameQuery()
	{
		final Query query = entityManager.createQuery("SELECT l FROM Location l WHERE LOWER(l.name) = :value");
		query.setParameter("value", "HoMe");
		final Location destination = (Location) query.getSingleResult();
		assert destination.getId() == TestHelper.getDestinationID();
	}

	@Test
	public void testPostcodeQuery()
	{
		Location destination = Server.getLocation(entityManager, "Ng9 2wb");
		assert destination.getId() == TestHelper.getDestinationID();
		destination = Server.getLocation(entityManager, "ng92WB");
		assert destination.getId() == TestHelper.getDestinationID();
		destination = Server.getLocation(entityManager, "ng9 2wb");
		assert destination.getId() == TestHelper.getDestinationID();
	}

	@Test
	public void testRename()
	{
		Location destination = entityManager.find(Location.class, TestHelper.getDestinationID());
		assert destination.getName().equals("Home");
		destination.setName("Rename");
		entityManager.getTransaction().begin();
		destination = entityManager.merge(destination);
		entityManager.getTransaction().commit();
		destination = entityManager.find(Location.class, TestHelper.getDestinationID());
		assert destination.getName().equals("Rename");
		destination.setName("Home");
		entityManager.getTransaction().begin();
		destination = entityManager.merge(destination);
		entityManager.getTransaction().commit();
		assert destination.getName().equals("Home");
	}
}