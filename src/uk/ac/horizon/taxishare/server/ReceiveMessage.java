package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.util.StringTokenizer;
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

public class ReceiveMessage extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(ReceiveMessage.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		final String notificationType = request.getParameter("notificationType");
		if (notificationType == null) { return; }

		if (notificationType.equals("MessageReceived"))
		{
			final String phoneNumber = request.getParameter("originator");
			final String body = request.getParameter("body");

			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("sms");
			final EntityManager entityManager = factory.createEntityManager();
			
			StringTokenizer tokenizer = new StringTokenizer(body.trim());
			logger.info(body + " (" + tokenizer.countTokens() + " tokens)");
			
			String firstToken = tokenizer.nextToken();
			
			if (firstToken.toUpperCase().equals("LEAVE"))
			{
				Server.leaveTaxi(entityManager, phoneNumber);
			}
			else
			{
				String secondToken = tokenizer.nextToken();
				// Second token should match either destination name, a postcode, or a taxiid
				
				if(secondToken.toUpperCase().startsWith("TAXI"))
				{
					// TODO Join Taxi by Taxi ID
					Server.joinTaxi(entityManager, phoneNumber, secondToken);
				}
				else
				{
					Query query = entityManager.createQuery("SELECT i FROM Instance i WHERE i.enabled = true");
					Instance instance = (Instance) query.getSingleResult();
					
					Server.requestTaxi(entityManager, instance.getId(), firstToken, phoneNumber, secondToken);
				}
			}
		}
	}
}
