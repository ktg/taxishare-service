package uk.ac.horizon.taxishare.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class Destination extends JavaScriptObject
{
	protected Destination()
	{
	}

	public native final int getId() /*-{ return this.id; }-*/;

	public native final String getName() /*-{ return this.name; }-*/;

	public native final String getPostcode() /*-{ return this.postcode; }-*/;
}