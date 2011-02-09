package uk.ac.horizon.taxishare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class TaxiShareServiceImpl implements TaxiShareService
{
	private final RequestCallback defaultCallback = new RequestCallback()
	{
		@Override
		public void onError(final Request request, final Throwable exception)
		{
			GWT.log(exception.getMessage(), exception);
		}

		@Override
		public void onResponseReceived(final Request request, final Response response)
		{
			GWT.log(response.getStatusCode() + ": " + response.getStatusText());
		}
	};

	public TaxiShareServiceImpl()
	{
	}

	@Override
	public void getInstance(final RequestCallback callback)
	{
		serverRequest(getHostURL() + "getInstance", callback);
	}

	@Override
	public void setCompany(final int taxiID, final int taxiCompanyID)
	{
		serverRequest(getHostURL() + "setCompany?taxiID=" + taxiID + "&taxiCompanyID=" + taxiCompanyID, defaultCallback);
	}

	@Override
	public void setFare(final int taxiID, final String text)
	{
		serverRequest(getHostURL() + "setFare?taxiID=" + taxiID + "&fare=" + text, defaultCallback);
	}

	@Override
	public void setStatus(final int taxiID, final String text)
	{
		serverRequest(getHostURL() + "setStatus?taxiID=" + taxiID + "&status=" + text, defaultCallback);
	}

	@Override
	public void setTime(final int taxiID, final String time, final String type)
	{
		serverRequest(getHostURL() + "setTime?taxiID=" + taxiID + "&time=" + time + "&type=" + type, defaultCallback);
	}

	private String getHostURL()
	{
		final String url = GWT.getHostPageBaseURL();
		if (url.endsWith("taxishare-ui/")) { return url.substring(0, url.length() - "taxishare-ui/".length())
				+ "taxishare-service/"; }

		return url;
	}

	private void serverRequest(final String url, final RequestCallback callback)
	{
		GWT.log(url);
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		try
		{
			builder.sendRequest(null, callback);
		}
		catch (final Exception e)
		{
			GWT.log(e.getMessage(), e);
		}
	}

	@Override
	public void removePerson(String number)
	{
		serverRequest(getHostURL() + "removePerson?number=" + number, defaultCallback);		
	}
}