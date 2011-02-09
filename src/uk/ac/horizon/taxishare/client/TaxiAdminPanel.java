package uk.ac.horizon.taxishare.client;

import java.util.ArrayList;
import java.util.Collection;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
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
	private static PopupMenu popupMenu = new PopupMenu();
	
	private final Collection<PopupAction> stateActions = new ArrayList<PopupAction>();

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
		
		stateActions.add(new PopupAction("Unconfirmed", new Runnable()
		{
			@Override
			public void run()
			{
				service.setStatus(taxi.getId(), "unconfirmed");				
			}
		}));
		stateActions.add(new PopupAction("Confirmed", new Runnable()
		{
			@Override
			public void run()
			{
				service.setStatus(taxi.getId(), "confirmed");				
			}
		}));
		stateActions.add(new PopupAction("Arriving", new Runnable()
		{
			@Override
			public void run()
			{
				service.setStatus(taxi.getId(), "arriving");				
			}
		}));
		stateActions.add(new PopupAction("Arrived", new Runnable()
		{
			@Override
			public void run()
			{
				service.setStatus(taxi.getId(), "arrived");				
			}
		}));
		stateActions.add(new PopupAction("Left", new Runnable()
		{
			@Override
			public void run()
			{
				service.setStatus(taxi.getId(), "left");				
			}
		}));
		stateActions.add(new PopupAction("cancelled", new Runnable()
		{
			@Override
			public void run()
			{
				service.setStatus(taxi.getId(), "cancelled");				
			}
		}));		
	}

	public void update(final Taxi taxi)
	{
		String oldArrival = null;
		String oldPickup = null;
		float oldFare = 0;
		if (this.taxi != null)
		{
			oldArrival = this.taxi.getArrivalTime();
			oldPickup = this.taxi.getPickupTime();
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
		for (int index = 0; index < taxi.getPeople().length(); index++)
		{
			final Person person = taxi.getPeople().get(index);
			Label label;
			if(person.getSpaces() > 1)
			{
				label = new Label(person.getName() + " (+" + (person.getSpaces() - 1) + ")");
			}
			else
			{
				label = new Label(person.getName());				
			}

			label.setStyleName("infoLabel");
			label.setTitle(person.getNumber());
			label.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					final Collection<PopupAction> actions = new ArrayList<PopupAction>();
					actions.add(new PopupAction("Remove " + person.getName(), new Runnable()
					{	
						@Override
						public void run()
						{
							service.removePerson(person.getNumber());
						}
					}));
					popupMenu.popup(actions, event);
					
				}
			});
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
		Collection<PopupAction> actions = new ArrayList<PopupAction>();
		for (int index = 0; index < instance.getCompanies().length(); index++)
		{
			final TaxiCompany company = instance.getCompanies().get(index);
			
			actions.add(new PopupAction(company.getName(), new Runnable()
			{
				
				@Override
				public void run()
				{
					service.setCompany(taxi.getId(), company.getId());
				}
			}));
		}
		popupMenu.popup(actions, e);
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
		popupMenu.popup(stateActions, e);
	}
}