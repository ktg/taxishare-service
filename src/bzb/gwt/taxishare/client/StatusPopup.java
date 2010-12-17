package bzb.gwt.taxishare.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatusPopup extends PopupPanel
{

	private static StatusPopupUiBinder uiBinder = GWT.create(StatusPopupUiBinder.class);

	interface StatusPopupUiBinder extends UiBinder<Widget, StatusPopup>
	{
	}
	
	private int taxiID;
	private TaxiShareService service;
	
	public StatusPopup()
	{
		add(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button unconfirmed;
	
	@UiField
	Button confirmed;
	
	@UiField
	Button arriving;	

	@UiField
	Button arrived;	

	@UiField
	Button left;	
	
	public void setTaxi(int taxiID, TaxiShareService service)
	{
		this.taxiID = taxiID;
		this.service = service;
	}

	@UiHandler({"unconfirmed", "confirmed", "arriving", "arrived", "left"})
	void handleClick(ClickEvent e)
	{
		service.setStatus(taxiID, ((Button)e.getSource()).getText());
		hide();
	}
}
