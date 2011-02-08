package uk.ac.horizon.taxishare.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.horizon.taxishare.client.model.Instance;
import uk.ac.horizon.taxishare.client.model.Taxi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TaxiShareAdmin extends Composite
{
	interface TaxiShareAdminUiBinder extends UiBinder<Widget, TaxiShareAdmin>
	{
	}

	private static TaxiShareAdminUiBinder uiBinder = GWT.create(TaxiShareAdminUiBinder.class);

	private static final native Instance parseJSONResponse(String json)
	/*-{
		return eval('(' + json + ')');
	}-*/;

	@UiField
	FlowPanel companies;

	@UiField
	FlowPanel locations;

	@UiField
	FlowPanel taxis;

	private final TaxiShareService service;

	private Instance instance;

	private final Map<String, TaxiAdminPanel> panels = new HashMap<String, TaxiAdminPanel>();

	public TaxiShareAdmin(final TaxiShareService service)
	{
		this.service = service;
		initWidget(uiBinder.createAndBindUi(this));

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

			Set<String> ids = new HashSet<String>();
			for (int index = 0; index < instance.getTaxis().length(); index++)
			{
				final Taxi taxi = instance.getTaxis().get(index);
				final String taxiID = "TAXI" + taxi.getId();
				ids.add(taxiID);
				TaxiAdminPanel panel = panels.get(taxiID);
				if (panel == null)
				{
					panel = new TaxiAdminPanel(instance, taxi, service);
					taxis.add(panel);
					panels.put(taxiID, panel);
				}
				panel.update(taxi);
			}
			
			for(String taxiID: panels.keySet())
			{
				if(!ids.contains(taxiID))
				{
					TaxiAdminPanel panel = panels.get(taxiID);
					taxis.remove(panel);
					panels.remove(taxiID);
				}
			}

			locations.clear();
			for (int index = 0; index < instance.getDestinations().length(); index++)
			{
				locations.add(new Label(instance.getDestinations().get(index).getName() + " "
						+ instance.getDestinations().get(index).getPostcode()));
			}

			companies.clear();
			for (int index = 0; index < instance.getCompanies().length(); index++)
			{
				companies.add(new Label(instance.getCompanies().get(index).getName() + ": "
						+ instance.getCompanies().get(index).getNumber()));
			}

		}
		catch (final Exception e)
		{
			GWT.log(e.getMessage(), e);
		}

	}
}