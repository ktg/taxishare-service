package uk.ac.horizon.taxishare.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.horizon.taxishare.model.Destination;


public class DestinationEntityTest
{
	private EntityManager entityManager;
	private long id;	
	
	@Before
	public void setUp() throws Exception
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
		entityManager = factory.createEntityManager();	
		Destination destination = new Destination("test", "NG9 2WB");
		entityManager.getTransaction().begin();		
		entityManager.persist(destination);
		entityManager.getTransaction().commit();
		id = destination.getId();
	}

	@After
	public void tearDown() throws Exception
	{
		entityManager.getTransaction().begin();
		Destination destination = entityManager.find(Destination.class, id);
		entityManager.remove(destination);
		entityManager.getTransaction().commit();		
	}
	
	@Test
	public void testPostcodeQuery()
	{
		Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE LOWER(d.postcode) = :value");
		query.setParameter("value", "NG9 2WB".toLowerCase());
		Destination destination = (Destination) query.getSingleResult();	
		assert(destination.getId() == id);	
	}
	
	@Test
	public void testNameQuery()
	{
		Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE d.name = :value");
		query.setParameter("value", "test");
		Destination destination = (Destination) query.getSingleResult();	
		assert(destination.getId() == id);
	}
	
	@Test
	public void testRename()
	{
		Destination destination = entityManager.find(Destination.class, id);
		assert(destination.getName().equals("test"));		
		destination.setName("Rename");
		entityManager.getTransaction().begin();		
		destination = entityManager.merge(destination);
		entityManager.getTransaction().commit();
		destination = entityManager.find(Destination.class, id);		
		assert destination.getName().equals("Rename");
		destination.setName("test");		
		entityManager.getTransaction().begin();		
		destination = entityManager.merge(destination);
		entityManager.getTransaction().commit();
		assert(destination.getName().equals("test"));			
	}	
}