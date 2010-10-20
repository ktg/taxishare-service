package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.model.Destination;

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
		final Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE LOWER(d.name) = :value");
		query.setParameter("value", "HoMe");
		final Destination destination = (Destination) query.getSingleResult();
		assert destination.getId() == TestHelper.getDestinationID();
	}

	@Test
	public void testPostcodeQuery()
	{
		Destination destination = Server.getDestination(entityManager, "Ng9 2wb");
		assert destination.getId() == TestHelper.getDestinationID();
		destination = Server.getDestination(entityManager, "ng92WB");
		assert destination.getId() == TestHelper.getDestinationID();
		destination = Server.getDestination(entityManager, "ng9 2wb");
		assert destination.getId() == TestHelper.getDestinationID();
	}

	@Test
	public void testRename()
	{
		Destination destination = entityManager.find(Destination.class, TestHelper.getDestinationID());
		assert destination.getName().equals("Home");
		destination.setName("Rename");
		entityManager.getTransaction().begin();
		destination = entityManager.merge(destination);
		entityManager.getTransaction().commit();
		destination = entityManager.find(Destination.class, TestHelper.getDestinationID());
		assert destination.getName().equals("Rename");
		destination.setName("Home");
		entityManager.getTransaction().begin();
		destination = entityManager.merge(destination);
		entityManager.getTransaction().commit();
		assert destination.getName().equals("Home");
	}
}