package bzb.gwt.taxishare.client;

import java.util.HashMap;
import java.util.Map;

import bzb.gwt.taxishare.client.model.Instance;
import bzb.gwt.taxishare.client.model.Taxi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;

public class TaxiShareAdmin extends FlowPanel
{
	private static final native Instance parseJSONResponse(String json)
	/*-{
		return eval('(' + json + ')');
	}-*/;

	private final TaxiShareService service;

	private Instance instance;

	private final Map<String, TaxiAdminPanel> panels = new HashMap<String, TaxiAdminPanel>();

	public TaxiShareAdmin(final TaxiShareService service)
	{
		this.service = service;

		final Timer requestTimer = new Timer()
		{
			@Override
			public void run()
			{
				service.getInstance(new RequestCallback()
				{

					@Override
					public void onError(final Request request, final Throwable exception)
					{
						GWT.log(exception.getMessage(), exception);

					}

					@Override
					public void onResponseReceived(final Request request, final Response response)
					{
						if (200 == response.getStatusCode())
						{
							try
							{
								parseResponse(response.getText());
							}
							catch (final Exception e)
							{
								GWT.log(response.getText(), e);
							}
						}
						else
						{
							GWT.log("Response code: " + response.getStatusCode(), null);
							// Handle the error. Can get the status text from
							// response.getStatusText()
						}
					}
				});
			}
		};
		requestTimer.scheduleRepeating(5000);
	}

	private void parseResponse(final String response)
	{
		try
		{
			GWT.log(response);
			instance = parseJSONResponse(response);

			for (int index = 0; index < instance.getTaxis().length(); index++)
			{
				final Taxi taxi = instance.getTaxis().get(index);
				final String taxiID = "TAXI" + taxi.getId();
				TaxiAdminPanel panel = panels.get(taxiID);
				if (panel == null)
				{
					panel = new TaxiAdminPanel(taxi, service);
					add(panel);
					panels.put(taxiID, panel);
				}
				panel.update(taxi);
			}

		}
		catch (final Exception e)
		{
			GWT.log(e.getMessage(), e);
		}

	}
}