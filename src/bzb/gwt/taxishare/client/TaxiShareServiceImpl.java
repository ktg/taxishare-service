package bzb.gwt.taxishare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class TaxiShareServiceImpl implements TaxiShareService
{
	public TaxiShareServiceImpl()
	{
	}
	
	private final RequestCallback defaultCallback = new RequestCallback()
	{
		@Override
		public void onResponseReceived(Request request, Response response)
		{
			GWT.log(response.getStatusCode() + ": " + response.getStatusText());			
		}
		
		@Override
		public void onError(Request request, Throwable exception)
		{
			GWT.log(exception.getMessage(), exception);			
		}
	};
	
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

	private String getHostURL()
	{
		String url = GWT.getHostPageBaseURL();
		if(url.endsWith("taxishare-ui/"))
		{
			return url.substring(0, url.length() - "taxishare-ui/".length()) + "taxishare-service/";
		}
		
		return url;
	}
	
	@Override
	public void getInstance(final RequestCallback callback)
	{
		serverRequest(getHostURL() + "getInstance", callback);
	}

	@Override
	public void setTime(int taxiID, String time, String type)
	{
		serverRequest(getHostURL() + "setTime?taxiID=" + taxiID + "&time=" + time + "&type=" + type, defaultCallback);		
	}

	@Override
	public void setFare(int taxiID, String text)
	{
		serverRequest(getHostURL() + "setFare?taxiID=" + taxiID + "&fare=" + text, defaultCallback);
	}

	@Override
	public void setStatus(int taxiID, String text)
	{
		serverRequest(getHostURL() + "setStatus?taxiID=" + taxiID + "&status=" + text, defaultCallback);		
	}
	
	@Override
	public void setCompany(int taxiID, String taxiCompanyID)
	{
		serverRequest(getHostURL() + "setCompany?taxiID=" + taxiID + "&taxiCompanyID=" + taxiCompanyID, defaultCallback);		
	}	
}