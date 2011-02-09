package uk.ac.horizon.taxishare.server.servlets;

import java.io.IOException;
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

import uk.ac.horizon.taxishare.server.model.Instance;
import uk.ac.horizon.taxishare.server.model.Location;
import uk.ac.horizon.taxishare.server.model.TaxiCompany;

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
			final Map<String, String> persistProperties = new HashMap<String, String>();
			persistProperties.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare", persistProperties);

			final EntityManager entityManager = factory.createEntityManager();

			final Location source = new Location("EMCC", "NG7 2RJ");
			final Location destination = new Location("TrainStation", "NG2 3AQ");
			final Location destination2 = new Location("MarketSquare", "NG1 6HL");
			final Location destination3 = new Location("Jubilee", "NG8 1BB");
			final Location destination4 = new Location("Airport", "DE74 2SA");			

			final Instance instance = new Instance();
			instance.setLocation(source);
			instance.setNumber("07786202578");

			final TaxiCompany taxiCompany = new TaxiCompany("DG Cars", "0115 9607607");
			final TaxiCompany taxiCompany2 = new TaxiCompany("A1 Cars", "0115 9708708");			

			instance.add(destination);
			instance.add(destination2);
			instance.add(destination3);
			instance.add(destination4);			

			instance.add(taxiCompany);
			instance.add(taxiCompany2);			

			entityManager.getTransaction().begin();
			entityManager.persist(taxiCompany);
			entityManager.persist(taxiCompany2);			
			entityManager.persist(source);
			entityManager.persist(destination);
			entityManager.persist(destination2);
			entityManager.persist(destination3);
			entityManager.persist(destination4);			
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