package uk.ac.horizon.taxishare.server.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.server.Server;
import uk.ac.horizon.taxishare.server.model.Taxi;

public class SetFare extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(SetFare.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		logger.info(request.getRequestURL().toString());

		try
		{
			final EntityManager entityManager = Server.createEntityManager();

			final int taxiID = Integer.parseInt(request.getParameter("taxiID"));
			String fareString = request.getParameter("fare");

			if (fareString.startsWith("£"))
			{
				fareString = fareString.substring(1);
			}

			final float fare = Float.parseFloat(fareString);

			final Taxi taxi = entityManager.find(Taxi.class, taxiID);
			taxi.setPredictedCost(fare);
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