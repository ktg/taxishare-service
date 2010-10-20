package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.model.Person;
import uk.ac.horizon.taxishare.model.Taxi;

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
		final Person person = new Person("Kevin", "x");
		person.setTaxi(taxi);
		entityManager.getTransaction().begin();
		entityManager.persist(person);
		entityManager.getTransaction().commit();
		System.out.println("Av:" + taxi.getAvailableSpace());
		taxi = entityManager.find(Taxi.class, TestHelper.getTaxiID());
		System.out.println("Av:" + taxi.getAvailableSpace());
		entityManager.getTransaction().begin();
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