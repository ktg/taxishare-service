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

import uk.ac.horizon.taxishare.model.Taxi;
import uk.ac.horizon.taxishare.model.Taxi.Status;

public class SetCompany extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(SetCompany.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		logger.info(request.getRequestURL().toString());

		try
		{
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			final EntityManager entityManager = factory.createEntityManager();

			int taxiID = Integer.parseInt(request.getParameter("taxiID"));
			String status = request.getParameter("status").toLowerCase();
			
			Taxi taxi = entityManager.find(Taxi.class, taxiID);
			taxi.setStatus(Status.valueOf(status));
			
			entityManager.getTransaction().begin();
			entityManager.merge(taxi);
			entityManager.getTransaction().commit();
			entityManager.close();	
		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace(response.getWriter());
		}
	}
}