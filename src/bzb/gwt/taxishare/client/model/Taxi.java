package bzb.gwt.taxishare.client.model;


import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Taxi extends JavaScriptObject
{
	public enum Status
	{
		unconfirmed, confirmed, arriving, arrived, left
	}

	protected Taxi() {}

	public final native Date getArrivalTime() /*-{ return this.arrivalTime; }-*/;

	public final native TaxiCompany getCompany() /*-{ return this.company; }-*/;
	
	public final native Destination getDestination() /*-{ return this.destination; }-*/;

	public final native int getId() /*-{ return this.id }-*/;

	public final native JsArray<Person> getPeople() /*-{ return this.people; }-*/;

	public final native Date getPickupTime() /*-{ return this.pickupTime; }-*/;

	public final native float getPredictedCost() /*-{ return this.predictedCost; }-*/;

	public final native Date getRequestTime() /*-{ return this.requestTime; }-*/;

	public final native String getStatus() /*-{ return this.status; }-*/;

//	public int getAvailableSpace()
//	{
//		return totalSpace - people.size();
//	}
	
	public final native int getTotalSpace() /*-{ return this.totalSpace; }-*/;
}