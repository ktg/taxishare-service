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

public class RequestTaxi extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(RequestTaxi.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		response.setContentType("application/json");
		logger.info(request.getRequestURL().toString());

		try
		{
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			final EntityManager entityManager = factory.createEntityManager();
			final String instance = request.getParameter("instance");

			Server.requestTaxi(	entityManager, Integer.parseInt(instance), request.getParameter("name"),
								request.getParameter("number"), request.getParameter("destination"));
		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}