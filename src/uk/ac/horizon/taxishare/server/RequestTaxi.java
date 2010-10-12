package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.model.Destination;
import uk.ac.horizon.taxishare.model.Instance;
import uk.ac.horizon.taxishare.model.Person;
import uk.ac.horizon.taxishare.model.Taxi;

public class RequestTaxi extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(RequestTaxi.class.getName());

	Pattern postcode = Pattern.compile("[A-Z]{1,2}[0-9R][0-9A-Z]? {0,1}[0-9][ABD-HJLNP-UW-Z]{2}"); 

	private boolean isPostcode(final String destination)
	{
		return postcode.matcher(destination).matches();
	}
	
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		response.setContentType("application/json");
		logger.info(request.getRequestURL().toString());

		try
		{
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			EntityManager entityManager = factory.createEntityManager();
			final String destinationName = request.getParameter("destination");	
			Taxi taxi = null;
			if(isPostcode(destinationName))
			{
				Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE LOWER(d.postcode) = :value");
				query.setParameter("value", "NG9 2WB".toLowerCase());
				Destination destination = (Destination) query.getSingleResult();
			}
			else
			{
				Query query = entityManager.createQuery("SELECT d FROM Destination d WHERE LOWER(d.postcode) = :value");
				query.setParameter("value", "NG9 2WB".toLowerCase());
				Destination destination = (Destination) query.getSingleResult();
				if(destination != null)
				{
					final String instanceIDString = request.getParameter("instanceID");
					final long instanceID = Long.parseLong(instanceIDString);
					
					query = entityManager.createQuery("SELECT taxi FROM Taxi WHERE destinationID = :destID AND instanceID = :instID");
					query.setParameter("destID", destination.getId());
					query.setParameter("instID", instanceID);							
					Iterable<Taxi> taxis = query.getResultList();
					for(Taxi potentialTaxi: taxis)
					{
						// if(can add to taxi)
						taxi = potentialTaxi;
						break;
					}
					
					if(taxi == null)
					{
						// create taxi
						final Instance instance = entityManager.find(Instance.class, instanceID);
						taxi = new Taxi();
						taxi.setInstance(instance);
						instance.add(taxi);
						
						entityManager.getTransaction().begin();
						entityManager.persist(taxi);
						entityManager.merge(instance);
						entityManager.getTransaction().commit();						
					}
				}

			}
			
			assert(taxi != null);
			
			final String name = request.getParameter("name");
			final String number = request.getParameter("number");
			Person person = new Person(name, number);
			taxi.add(person);
			entityManager.getTransaction().begin();
			entityManager.persist(person);
			entityManager.merge(taxi);
			entityManager.getTransaction().commit();
			
			// create person
			// add person to taxi


		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}
