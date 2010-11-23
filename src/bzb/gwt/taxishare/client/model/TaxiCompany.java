package bzb.gwt.taxishare.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class TaxiCompany extends JavaScriptObject
{
	protected TaxiCompany() { }

	public native final int getId() /*-{ return this.id; }-*/;

	public native final String getName() /*-{ return this.name; }-*/;

	public native final String getNumber() /*-{ return this.number; }-*/;
}