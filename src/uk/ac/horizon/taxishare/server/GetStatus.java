package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.model.Instance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetStatus extends HttpServlet
{
	private final static Logger logger = Logger.getLogger(GetStatus.class.getName());

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		response.setContentType("application/json");
		logger.info(request.getRequestURL().toString());

		final String instanceIDString = request.getParameter("instanceID");

		try
		{
			final long instanceID = Long.parseLong(instanceIDString);

			final EntityManagerFactory factory = Persistence.createEntityManagerFactory("taxishare");
			final EntityManager entityManager = factory.createEntityManager();
			final Instance instance = entityManager.find(Instance.class, instanceID);

			final Writer writer = response.getWriter();
			final Gson gson = new GsonBuilder().setExclusionStrategies(new ManyToOneExclusionStrategy()).create();
			writer.write(gson.toJson(instance));
		}
		catch (final Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}
