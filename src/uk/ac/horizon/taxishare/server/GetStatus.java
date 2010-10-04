package uk.ac.horizon.taxishare.server;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.horizon.taxishare.model.Instance;

public class GetStatus extends HttpServlet
{
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException
	{
		response.setContentType("application/json");
		// logger.info(request.getRequestURL().toString());

		final String instanceIDString = request.getParameter("instanceID");

		try
		{
			long instanceID = Long.parseLong(instanceIDString);
			
			final Instance instance = null;
			
			Writer writer = response.getWriter();
			instance.toJSON(writer);

		}
		catch(Exception e)
		{
			
		}
	}
}
