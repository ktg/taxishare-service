package uk.ac.horizon.taxishare.server.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.server.Server;
import uk.ac.horizon.taxishare.server.model.Person;
import uk.ac.horizon.taxishare.server.model.Taxi;
import uk.ac.horizon.taxishare.server.model.Taxi.Status;

public class SetStatus extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(SetStatus.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		try
		{
			final EntityManager entityManager = Server.createEntityManager();

			final int taxiID = Integer.parseInt(request.getParameter("taxiID"));
			final String status = request.getParameter("status").toLowerCase();

			logger.info("Set Status: TAXI" + taxiID + " to " + status);

			final Taxi taxi = entityManager.find(Taxi.class, taxiID);
			taxi.setStatus(Status.valueOf(status));

			if (taxi.getStatus() == Status.arrived || taxi.getStatus() == Status.cancelled)
			{
				final Date old = taxi.getPickupTime();
				final Date now = new Date();

				taxi.setPickupTime(now);
				taxi.setArrivalTime(new Date(taxi.getArrivalTime().getTime() + (now.getTime() - old.getTime())));

				String message = "Your taxi has bee cancelled.";
				if(taxi.getStatus() == Status.arrived)
				{
					message = "Your taxi has arrived.";
				}
				
				for(final Person person: taxi.getPeople())
				{
					Server.sendMessage(person.getNumber(), message);
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