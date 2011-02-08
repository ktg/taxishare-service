package uk.ac.horizon.taxishare.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Instance extends JavaScriptObject
{
	protected Instance()
	{
	}

	public final native JsArray<TaxiCompany> getCompanies() /*-{ return this.companies; }-*/;

	public final native JsArray<Destination> getDestinations() /*-{ return this.destinations; }-*/;

	public final native int getId() /*-{ return this.id; }-*/;

	public final native Destination getLocation() /*-{ return this.location; }-*/;

	public final native String getNumber() /*-{ return this.number; }-*/;

	public final native JsArray<Taxi> getTaxis() /*-{ return this.taxis; }-*/;
}
