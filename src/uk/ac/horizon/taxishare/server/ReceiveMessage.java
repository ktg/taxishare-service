package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.model.Instance;
import uk.ac.horizon.taxishare.model.Message;

/**
 * Servlet to recieve a text message from Esendex.
 * 
 * @author Kevin Glover
 */
public class ReceiveMessage extends HttpServlet
{
	private static final Logger logger = Logger.getLogger(ReceiveMessage.class.getName());

	private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat parser = new SimpleDateFormat("HH:mm");

	@Override
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		doGet(request, response);
	}

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		logger.info(request.getRequestURL().toString());

		final String notificationType = request.getParameter("notificationType");
		if (notificationType == null) { return; }

		if (notificationType.equals("MessageReceived"))
		{
			logger.info("Message Received");
			logger.info("From: " + request.getParameter("originator"));
			logger.info("Body: " + request.getParameter("body"));
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			final EntityManager entityManager = factory.createEntityManager();

			final Message message = new Message(request.getParameter("originator"), request.getParameter("body"), true);
			message.setProviderMessageID(request.getParameter("id"));
			try
			{
				message.setTimeReceived(formatter.parse(request.getParameter("receivedAt")));
			}
			catch (final Exception e)
			{
				message.setTimeReceived(new Date());
				logger.log(Level.WARNING, e.getMessage(), e);
			}

			entityManager.getTransaction().begin();
			entityManager.persist(message);
			entityManager.getTransaction().commit();

			final StringTokenizer tokenizer = new StringTokenizer(message.getBody().trim());
			logger.info(message.getBody() + " (" + tokenizer.countTokens() + " tokens)");

			final String firstToken = tokenizer.nextToken();

			if (firstToken.toUpperCase().equals("LEAVE"))
			{
				Server.leaveTaxi(entityManager, message.getPhoneNumber());
			}
			else
			{
				final String secondToken = tokenizer.nextToken();
				// Second token should match either destination name, a postcode, or a taxiid

				if (secondToken.toUpperCase().startsWith("TAXI"))
				{
					logger.info("Join taxi " + secondToken);
					// Join Taxi by Taxi ID
					Server.joinTaxi(entityManager, firstToken, message.getPhoneNumber(), secondToken);
				}
				else
				{
					final Query query = entityManager.createQuery("SELECT i FROM Instance i WHERE i.enabled = true");
					final Instance instance = (Instance) query.getSingleResult();

					int spaces = 1;
					if (tokenizer.hasMoreTokens())
					{
						final String spaceToken = tokenizer.nextToken();
						try
						{
							spaces = Integer.parseInt(spaceToken);
						}
						catch (final Exception e)
						{
							e.printStackTrace();
						}
					}

					Date time = null;
					if (tokenizer.hasMoreTokens())
					{
						final String timeToken = tokenizer.nextToken();
						try
						{
							time = parser.parse(timeToken);
						}
						catch (final ParseException e)
						{
							e.printStackTrace();
						}
					}

					Server.requestTaxi(	entityManager, instance.getId(), firstToken, message.getPhoneNumber(),
										secondToken, spaces, time);
				}
			}
			entityManager.close();
		}
	}
}