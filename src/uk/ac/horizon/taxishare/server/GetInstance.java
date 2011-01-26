package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.io.Writer;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet that returns an {@link uk.ac.horizon.taxishare.model.Instance} as JSON. If no instance id
 * is given, it will return a single enabled instance.
 * 
 * @author Kevin Glover
 */
public class GetInstance extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(GetInstance.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		try
		{
			response.setContentType("application/json");
			logger.info(request.getRequestURL().toString());

			final String instanceIDString = request.getParameter("instanceID");
			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			final EntityManager entityManager = factory.createEntityManager();

			Instance instance;
			if (instanceIDString != null)
			{
				final long instanceID = Long.parseLong(instanceIDString);
				instance = entityManager.find(Instance.class, instanceID);
			}
			else
			{
				final Query query = entityManager.createQuery("SELECT i FROM Instance i WHERE i.enabled = true");
				instance = (Instance) query.getSingleResult();
			}
			entityManager.close();

			final Writer writer = response.getWriter();
			final Gson gson = new GsonBuilder().setExclusionStrategies(new ManyToOneExclusionStrategy()).create();
			writer.write(gson.toJson(instance));
			logger.info(gson.toJson(instance));
		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}