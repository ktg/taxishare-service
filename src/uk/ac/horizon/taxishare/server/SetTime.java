package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import uk.ac.horizon.taxishare.model.Taxi;

public class SetTime extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(SetTime.class.getName());

	SimpleDateFormat timeFormat1 = new SimpleDateFormat("hh:mm aa");
	SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm");

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

			logger.info(request.getRequestURI().toString() + "Type: " + type + ", taxi: " + taxiID + ", time: " + timeString);
			
			Date parsed = null;
			try
			{
				parsed = timeFormat1.parse(timeString);
			}
			catch (final Exception e)
			{
				logger.log(Level.WARNING, e.getMessage(), e);
			}
			if (parsed == null)
			{
				try
				{
					parsed = timeFormat2.parse(timeString);
				}
				catch (final Exception e)
				{
					logger.log(Level.WARNING, e.getMessage(), e);
				}
			}

			final Taxi taxi = entityManager.find(Taxi.class, taxiID);
			if (parsed != null)
			{
				final Calendar parsedTime = Calendar.getInstance();
				parsedTime.setTime(parsed);

				final Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, parsedTime.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, parsedTime.get(Calendar.MINUTE));
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				if (type.equals("arrival"))
				{
					taxi.setArrivalTime(cal.getTime());
				}
				else if (type.equals("pickup"))
				{
					taxi.setPickupTime(cal.getTime());
				}
				else if (type.equals("request"))
				{
					taxi.setRequestTime(cal.getTime());
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