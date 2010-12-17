package bzb.gwt.taxishare.client;

import java.util.Date;

import bzb.gwt.taxishare.client.model.Taxi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TaxiAdminPanel extends Composite
{

	interface TaxiAdminPanelUiBinder extends UiBinder<Widget, TaxiAdminPanel>
	{
	}

	private static TaxiAdminPanelUiBinder uiBinder = GWT.create(TaxiAdminPanelUiBinder.class);

	private Taxi taxi;
	private final TaxiShareService service;
	private static StatusPopup statusSelect = new StatusPopup();

	@UiField
	Label taxiID;

	@UiField
	Label destination;

	@UiField
	Label status;

	@UiField
	Label company;
	
	@UiField
	TextBox fare;

	@UiField
	TextBox arrival;

	@UiField
	TextBox pickup;

	public TaxiAdminPanel(final Taxi taxi, final TaxiShareService service)
	{
		this.service = service;
		initWidget(uiBinder.createAndBindUi(this));
		update(taxi);
	}

	public void update(final Taxi taxi)
	{
		final String oldArrival = this.taxi.getArrivalTime();
		final String oldPickup = this.taxi.getPickupTime();
		float oldFare = 0;
		if(this.taxi != null)
		{
			oldFare = this.taxi.getPredictedCost();
		}

		this.taxi = taxi;
		if (taxi.getStatus().equals("left"))
		{
			pickup.setEnabled(false);
			arrival.setEnabled(false);
			fare.setEnabled(false);
		}
		else
		{
			pickup.setEnabled(true);
			arrival.setEnabled(true);
			fare.setEnabled(true);
		}

		taxiID.setText("TAXI" + taxi.getId());
		destination.setText(taxi.getDestination().getName());
		status.setText(taxi.getStatus());
		if(taxi.getCompany() != null)
		{
			company.setText(taxi.getCompany().getName());
		}
		else
		{
			company.setText("None");
		}

		if(oldFare != taxi.getPredictedCost())
		{
			fare.setText("" + taxi.getPredictedCost());			
		}
		
		// TODO if day != today print full day
		if (taxi.getArrivalTime() != null && (oldArrival == null || !oldArrival.equals(taxi.getArrivalTime())))
		{
			final Date arrivalTime = DateTimeFormat.getFormat("dd-MMM-yyyy HH:mm:ss").parse(taxi.getArrivalTime());
			arrival.setText(DateTimeFormat.getFormat("h:mm aa").format(arrivalTime));
		}

		if (taxi.getPickupTime() != null && (oldPickup == null || !oldPickup.equals(taxi.getPickupTime())))
		{
			final Date pickupTime = DateTimeFormat.getFormat("dd-MMM-yyyy HH:mm:ss").parse(taxi.getPickupTime());
			pickup.setText(DateTimeFormat.getFormat("h:mm aa").format(pickupTime));
		}
	}

	@UiHandler("arrival")
	void handleArrivalChange(final ChangeEvent e)
	{
		GWT.log("changed");
		service.setTime(taxi.getId(), arrival.getText(), "arrival");
	}

	@UiHandler("fare")
	void handleFareChange(final ChangeEvent e)
	{
		service.setFare(taxi.getId(), fare.getText());
	}

	@UiHandler("status")
	void handleStatusClick(final ClickEvent e)
	{
		statusSelect.setTaxi(taxi.getId(), service);
		statusSelect.setPopupPosition(e.getX(), e.getY());
		statusSelect.show();
	}
	
	@UiHandler("company")
	void handleCompanyClick(final ClickEvent e)
	{
		statusSelect.setTaxi(taxi.getId(), service);
		statusSelect.setPopupPosition(e.getClientX(), e.getClientY());
		statusSelect.show();
	}	
	
	@UiHandler("pickup")
	void handlePickupChange(final ChangeEvent e)
	{
		service.setTime(taxi.getId(), pickup.getText(), "pickup");
	}
}