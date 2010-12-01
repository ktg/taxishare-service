package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import uk.ac.horizon.taxishare.model.Instance;
import uk.ac.horizon.taxishare.model.Location;
import uk.ac.horizon.taxishare.model.Person;
import uk.ac.horizon.taxishare.model.Taxi;
import uk.ac.horizon.taxishare.model.TaxiCompany;
import uk.ac.horizon.taxishare.model.Taxi.Status;

public class Reset extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(Reset.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		logger.info(request.getRequestURL().toString());

		try
		{
			Map<String, String> persistProperties = new HashMap<String, String>();
			persistProperties.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
			
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare", persistProperties);
			final EntityManager entityManager = factory.createEntityManager();

			Location source = new Location("EMCC", "NG7 2RJ");
			Location destination = new Location("TrainStation", "NG2 3AQ");
			Location destination2 = new Location("MarketSquare", "NG1 6HL");
			Location destination3 = new Location("Jubilee", "NG8 1BB");			
			
			Instance instance = new Instance();
			instance.setLocation(source);
			instance.setNumber("0123456789");

			TaxiCompany taxiCompany = new TaxiCompany("DG Cars", "01159607607");			
			
			Person person = new Person("Kevin", "07796698175");
			
			Taxi taxi = new Taxi();
			taxi.setTotalSpace(4);
			long now = new Date().getTime();
			taxi.setRequestTime(new Date(now));
			taxi.setArrivalTime(new Date(now + (35 * 60 * 1000)));
			taxi.setPickupTime(new Date(now + (20 * 60 * 1000)));
			taxi.setStatus(Status.confirmed);
			taxi.setDestination(destination);
			taxi.setPredictedCost(20.0f);
			taxi.setCompany(taxiCompany);
			taxi.setInstance(instance);
			taxi.add(person);

			person.setTaxi(taxi);
			
			instance.add(destination);
			instance.add(destination2);
			instance.add(destination3);			

			entityManager.getTransaction().begin();
			entityManager.persist(taxiCompany);
			entityManager.persist(source);
			entityManager.persist(taxi);
			entityManager.persist(person);			
			entityManager.persist(destination);
			entityManager.persist(destination2);
			entityManager.persist(destination3);			
			entityManager.persist(instance);
			entityManager.getTransaction().commit();
			entityManager.close();
			
			response.getWriter().println("Reset OK");
		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace(response.getWriter());
		}
	}
}