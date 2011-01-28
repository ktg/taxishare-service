package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.server.model.Person;
import uk.ac.horizon.taxishare.server.model.Taxi;

public class TaxiEntityTest
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
	public void testAddPerson()
	{
		Taxi taxi = entityManager.find(Taxi.class, TestHelper.getTaxiID());
		final Person person = new Person("Kevin", "07797798175");
		person.setTaxi(taxi);
		taxi.add(person);
		entityManager.getTransaction().begin();
		entityManager.persist(person);
		entityManager.merge(taxi);
		entityManager.getTransaction().commit();
		taxi = entityManager.find(Taxi.class, TestHelper.getTaxiID());
		taxi.remove(person);
		entityManager.getTransaction().begin();
		entityManager.merge(taxi);
		entityManager.remove(person);		
		entityManager.getTransaction().commit();
	}

	@Test
	public void testQuery()
	{
		final Taxi taxi = entityManager.find(Taxi.class, TestHelper.getTaxiID());
		assert (taxi != null);
		assert (taxi.getTotalSpace() == 4);
		assert (taxi.getDestination().getName().equals("Home"));
		assert (taxi.getDestination().getPostcode().equals("NG9 2WB"));
		assert (taxi.getPredictedCost() == 20.0f);
	}
}