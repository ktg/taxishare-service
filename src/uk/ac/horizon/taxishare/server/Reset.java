package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.model.Instance;
import uk.ac.horizon.taxishare.model.Location;
import uk.ac.horizon.taxishare.model.TaxiCompany;

public class Reset extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(Reset.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		response.setContentType("application/json");
		logger.info(request.getRequestURL().toString());

		try
		{
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare-reset");
			final EntityManager entityManager = factory.createEntityManager();

			Location source = new Location("EMCC", "NG7 2RJ");
			Location destination = new Location("Station", "NG2 3AQ");
			
			Instance instance = new Instance();
			instance.setLocation(source);
			instance.setNumber("07798898175");

			TaxiCompany taxiCompany = new TaxiCompany("DG Cars", "01159607607");			
			
			instance.add(destination);

			entityManager.getTransaction().begin();
			entityManager.persist(taxiCompany);
			entityManager.persist(source);
			entityManager.persist(destination);
			entityManager.persist(instance);
			entityManager.getTransaction().commit();
		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}