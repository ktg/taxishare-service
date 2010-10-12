package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.model.Taxi;


public class TaxiEntityTest
{
	private EntityManager entityManager;
	private long id;	
	
	@Before
	public void setUp() throws Exception
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
		entityManager = factory.createEntityManager();	
		Taxi taxi = new Taxi();
		entityManager.getTransaction().begin();		
		entityManager.persist(taxi);
		entityManager.getTransaction().commit();
		id = taxi.getId();
	}

	@After
	public void tearDown() throws Exception
	{
		entityManager.getTransaction().begin();
		Taxi taxi = entityManager.find(Taxi.class, id);
		entityManager.remove(taxi);
		entityManager.getTransaction().commit();		
	}
	
	@Test
	public void testQuery()
	{
		Taxi taxi = entityManager.find(Taxi.class, id);
		assert(taxi != null);
	}
}