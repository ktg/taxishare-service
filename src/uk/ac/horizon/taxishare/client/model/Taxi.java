package uk.ac.horizon.taxishare.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;

public class Taxi extends JavaScriptObject
{
	public static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MMM-yyyy HH:mm:ss");
	
	public enum Status
	{
		unconfirmed, confirmed, arriving, arrived, left, cancelled
	}

	protected Taxi()
	{
	}

	public final native String getArrivalTime() /*-{ return this.arrivalTime; }-*/;

	public final native TaxiCompany getCompany() /*-{ return this.company; }-*/;

	public final native Destination getDestination() /*-{ return this.destination; }-*/;

	public final native int getId() /*-{ return this.id }-*/;

	public final native JsArray<Person> getPeople() /*-{ return this.people; }-*/;

	public final native String getPickupTime() /*-{ return this.pickupTime; }-*/;

	public final native float getPredictedCost() /*-{ return this.predictedCost; }-*/;

	public final native String getRequestTime() /*-{ return this.requestTime; }-*/;

	public final native String getStatus() /*-{ return this.status; }-*/;

	// public int getAvailableSpace()
	// {
	// return totalSpace - people.size();
	// }

	public final native int getTotalSpace() /*-{ return this.totalSpace; }-*/;
}