package uk.ac.horizon.taxishare.server.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.server.Server;
import uk.ac.horizon.taxishare.server.model.Taxi;

public class SetTime extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(SetTime.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		try
		{
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			final EntityManager entityManager = factory.createEntityManager();

			final int taxiID = Integer.parseInt(request.getParameter("taxiID"));
			final String timeString = request.getParameter("time");
			final String type = request.getParameter("type");

			logger.info(request.getRequestURI().toString() + "Type: " + type + ", taxi: " + taxiID + ", time: "
					+ timeString);

			final Date parsed = Server.parseTime(timeString);

			final Taxi taxi = entityManager.find(Taxi.class, taxiID);
			if (parsed != null)
			{
				if (type.equals("arrival"))
				{
					taxi.setArrivalTime(parsed);
				}
				else if (type.equals("pickup"))
				{
					taxi.setPickupTime(parsed);
				}
				else if (type.equals("request"))
				{
					taxi.setRequestTime(parsed);
				}
			}
			else if (timeString == null || timeString.isEmpty())
			{
				if (type.equals("arrival"))
				{
					taxi.setArrivalTime(null);
				}
				else if (type.equals("pickup"))
				{
					taxi.setPickupTime(null);
				}
				else if (type.equals("request"))
				{
					taxi.setRequestTime(null);
				}
			}
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