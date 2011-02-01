package uk.ac.horizon.taxishare.client;

import java.util.Date;

import uk.ac.horizon.taxishare.client.model.Instance;
import uk.ac.horizon.taxishare.client.model.Person;
import uk.ac.horizon.taxishare.client.model.Taxi;
import uk.ac.horizon.taxishare.client.model.TaxiCompany;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
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
	private Instance instance;
	private final TaxiShareService service;
	private static StatusPopup statusSelect = new StatusPopup();
	private static PopupPanel companySelect = new PopupPanel();

	@UiField
	Label taxiID;

	@UiField
	FlowPanel people;
	
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

	public TaxiAdminPanel(final Instance instance, final Taxi taxi, final TaxiShareService service)
	{
		this.instance = instance;
		this.service = service;
		initWidget(uiBinder.createAndBindUi(this));
		update(taxi);
	}

	public void update(final Taxi taxi)
	{
		final String oldArrival = this.taxi.getArrivalTime();
		final String oldPickup = this.taxi.getPickupTime();
		float oldFare = 0;
		if (this.taxi != null)
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
		destination.setTitle(taxi.getDestination().getPostcode());
		status.setText(taxi.getStatus());
		if (taxi.getCompany() != null)
		{
			company.setText(taxi.getCompany().getName());
		}
		else
		{
			company.setText("None");
		}

		if (oldFare != taxi.getPredictedCost())
		{
			fare.setText("" + taxi.getPredictedCost());
		}

		// TODO if day != today print full day
		if (taxi.getArrivalTime() != null && (oldArrival == null || !oldArrival.equals(taxi.getArrivalTime())))
		{
			final Date arrivalTime = Taxi.dateFormat.parse(taxi.getArrivalTime());
			arrival.setText(DateTimeFormat.getFormat("h:mm aa").format(arrivalTime));
		}

		if (taxi.getPickupTime() != null && (oldPickup == null || !oldPickup.equals(taxi.getPickupTime())))
		{
			final Date pickupTime = Taxi.dateFormat.parse(taxi.getPickupTime());
			pickup.setText(DateTimeFormat.getFormat("h:mm aa").format(pickupTime));
		}
		
		people.clear();
		for(int index = 0; index < taxi.getPeople().length(); index++)
		{
			Person person = taxi.getPeople().get(index);
			Label label = new Label(person.getName());
			label.setTitle(person.getNumber());
			people.add(label);
		}
	}

	@UiHandler("arrival")
	void handleArrivalChange(final ChangeEvent e)
	{
		GWT.log("changed");
		service.setTime(taxi.getId(), arrival.getText(), "arrival");
	}

	@UiHandler("company")
	void handleCompanyClick(final ClickEvent e)
	{
		companySelect.clear();
		for(int index = 0; index < instance.getCompanies().length(); index++)
		{
			final TaxiCompany company = instance.getCompanies().get(index);
			final Button button = new Button(company.getName());
			button.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					service.setCompany(taxi.getId(), company.getId());
					companySelect.hide();
					
				}
			});
			companySelect.add(button);
		}
		companySelect.setPopupPosition(e.getClientX(), e.getClientY());
		companySelect.show();
	}

	@UiHandler("fare")
	void handleFareChange(final ChangeEvent e)
	{
		service.setFare(taxi.getId(), fare.getText());
	}

	@UiHandler("pickup")
	void handlePickupChange(final ChangeEvent e)
	{
		service.setTime(taxi.getId(), pickup.getText(), "pickup");
	}

	@UiHandler("status")
	void handleStatusClick(final ClickEvent e)
	{
		statusSelect.setTaxi(taxi.getId(), service);
		statusSelect.setPopupPosition(e.getClientX(), e.getClientY());
		statusSelect.show();
	}
}