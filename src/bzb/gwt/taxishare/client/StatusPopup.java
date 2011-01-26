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

	interface StatusPopupUiBinder extends UiBinder<Widget, StatusPopup>
	{
	}

	private static StatusPopupUiBinder uiBinder = GWT.create(StatusPopupUiBinder.class);

	private int taxiID;
	private TaxiShareService service;

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

	public StatusPopup()
	{
		add(uiBinder.createAndBindUi(this));
	}

	public void setTaxi(final int taxiID, final TaxiShareService service)
	{
		this.taxiID = taxiID;
		this.service = service;
	}

	@UiHandler({ "unconfirmed", "confirmed", "arriving", "arrived", "left" })
	void handleClick(final ClickEvent e)
	{
		service.setStatus(taxiID, ((Button) e.getSource()).getText());
		hide();
	}
}
