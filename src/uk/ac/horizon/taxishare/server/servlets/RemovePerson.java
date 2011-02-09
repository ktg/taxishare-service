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
import uk.ac.horizon.taxishare.server.model.Person;

public class RemovePerson extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(RemovePerson.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		logger.info(request.getRequestURL().toString());

		try
		{
			final EntityManager entityManager = Server.createEntityManager();

			final String number = request.getParameter("number");

			Server.leaveTaxi(entityManager, number);
			
			final Person person = Server.getPerson(entityManager, number);

			entityManager.getTransaction().begin();
			entityManager.merge(person);
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