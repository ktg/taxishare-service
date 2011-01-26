package bzb.gwt.taxishare.client;

import com.google.gwt.http.client.RequestCallback;

public interface TaxiShareService
{
	public void getInstance(final RequestCallback callback);

	public void setCompany(int taxiID, String taxiCompanyID);

	public void setFare(int taxiID, String text);

	public void setStatus(int taxiID, String text);

	public void setTime(int taxiID, String time, String type);
}
