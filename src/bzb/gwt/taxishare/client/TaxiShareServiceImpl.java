package bzb.gwt.taxishare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.URL;

public class TaxiShareServiceImpl implements TaxiShareService
{
	public TaxiShareServiceImpl()
	{
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
}