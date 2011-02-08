package uk.ac.horizon.taxishare.client;

import uk.ac.horizon.taxishare.client.model.Taxi;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TaxiPanel extends FlowPanel
{
	interface TaxiPanelUiBinder extends UiBinder<Widget, TaxiPanel>
	{
	}

	// private static TaxiPanelUiBinder uiBinder = GWT.create(TaxiPanelUiBinder.class);

	@UiField
	Label idLabel;

	@UiField
	Label spaceLabel;

	@UiField
	Label destinationLabel;

	@UiField
	FlowPanel spaceIcons;

	public TaxiPanel(final Taxi taxi)
	{
		final Label idLabel = new Label("TAXI" + String.valueOf(taxi.getId()));
		idLabel.setStyleName("idLabel");
		idLabel.addStyleName("taxiBoxLeft");

		final int spaceLeft = taxi.getTotalSpace() - taxi.getUsedSpace();

		Panel spacesPanel;
		if (spaceLeft == 0)
		{
			spacesPanel = new SimplePanel();
			final Label fullLabel = new Label("FULL");
			fullLabel.setStyleName("destinationNameLabel");
			spacesPanel.add(fullLabel);
		}
		else
		{
			spacesPanel = new FlowPanel();
			Label spaceLabel = null;
			if (spaceLeft == 1)
			{
				spaceLabel = new Label(String.valueOf(spaceLeft) + " space");
			}
			else
			{
				spaceLabel = new Label(String.valueOf(spaceLeft) + " spaces");
			}
			spaceLabel.setStyleName("infoLabel");
			spacesPanel.add(spaceLabel);
			final FlowPanel spacesIcons = new FlowPanel();
			for (int i = 0; i < taxi.getTotalSpace() - spaceLeft; i++)
			{
				spacesIcons.add(new Image("freespace.png"));
			}
			for (int i = 0; i < spaceLeft; i++)
			{
				final Image fadedIcon = new Image("freespace.png");
				fadedIcon.addStyleName("fadedIcon");
				spacesIcons.add(fadedIcon);
			}
			spacesPanel.add(spacesIcons);
		}
		spacesPanel.setStyleName("taxiBoxLeft");		

		final Label destinationLabel = new Label(taxi.getDestination().getName());
		destinationLabel.setStyleName("destinationNameLabel");
		destinationLabel.addStyleName("taxiBoxMid");
		// destinationPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// destinationPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		add(idLabel);
		add(spacesPanel);
		add(destinationLabel);

		if (taxi.getStatus().equals("unconfirmed"))
		{
			final Label label = new Label("Not Booked Yet");
			label.addStyleName("destinationNameLabel");
			label.addStyleName("taxiBoxRight");

			add(label);
		}
		else if(taxi.getStatus().equals("left"))
		{
			final Label label = new Label("Already Left");
			label.addStyleName("destinationNameLabel");
			label.addStyleName("taxiBoxRight");

			add(label);			
		}
		else
		{
			final FlowPanel timePanel = new FlowPanel();
			final Label pickupLabel = new Label("Pickup");
			pickupLabel.setStyleName("infoLabel");
			timePanel.add(pickupLabel);
			Label departureTimeLabel;
			if(taxi.getStatus().equals("arrived"))
			{
				departureTimeLabel = new Label("Arrived");				
			}
			else
			{
				try
				{
					departureTimeLabel = new Label(DateTimeFormat.getFormat("h:mm a").format(	Taxi.dateFormat.parse(taxi
																										.getPickupTime())));
				}
				catch (final Exception e)
				{
					departureTimeLabel = new Label("unconfirmed");
					e.printStackTrace();
				}
			}
			departureTimeLabel.addStyleName("departureTimeLabel");			
			timePanel.add(departureTimeLabel);
			final Label journeyLabel = new Label("Journey");
			journeyLabel.setStyleName("infoLabel");
			timePanel.add(journeyLabel);
			Label arrivalTimeLabel;
			try
			{
				arrivalTimeLabel = new Label(
						(int) Math.round(((Taxi.dateFormat.parse(taxi.getArrivalTime()).getTime() - Taxi.dateFormat
								.parse(taxi.getPickupTime()).getTime()) / (60.0 * 1000))) + " mins");
				arrivalTimeLabel.addStyleName("arrivalTimeLabel");
			}
			catch (final Exception e)
			{
				arrivalTimeLabel = new Label("unconfirmed");
			}
			timePanel.addStyleName("taxiBoxRight");
			timePanel.add(arrivalTimeLabel);

			final FlowPanel farePanel = new FlowPanel();
			final Label fareLabel = new Label("Current Fare");
			fareLabel.setStyleName("infoLabel");
			farePanel.add(fareLabel);
			String priceEach = String
					.valueOf(((double) taxi.getPredictedCost() / (double) (taxi.getTotalSpace() - spaceLeft)));
			if (priceEach.indexOf('.') > -1)
			{
				if (priceEach.length() < priceEach.indexOf('.') + 3)
				{
					priceEach += "0";
				}
				else
				{
					priceEach = priceEach.substring(0, priceEach.indexOf('.') + 3);
				}
			}
			final Label priceLabel = new Label("\u00A3" + priceEach + " each");
			priceLabel.addStyleName("fareLabel");
			farePanel.add(priceLabel);
			if(taxi.getUsedSpace() > 1)
			{
				final Label totalLabel = new Label("\u00A3" + taxi.getPredictedCost() + " total (est.)");
				totalLabel.setStyleName("infoLabel");
				farePanel.add(totalLabel);
			}
			else
			{
				final Label totalLabel = new Label("estimated");
				totalLabel.setStyleName("infoLabel");
				farePanel.add(totalLabel);
			}
			farePanel.addStyleName("taxiBoxRight");
			
			if(spaceLeft > 0)
			{
				final FlowPanel savingPanel = new FlowPanel();
				savingPanel.addStyleName("taxiBoxRight");
				final Label fullLabel = new Label("Fare when Full");
				fullLabel.setStyleName("infoLabel");
				savingPanel.add(fullLabel);
				String savingEach = String.valueOf((double) taxi.getPredictedCost() / (double) taxi.getTotalSpace());
				if (savingEach.indexOf('.') > -1)
				{
					if (savingEach.length() < savingEach.indexOf('.') + 3)
					{
						savingEach += "0";
					}
					else
					{
						savingEach = savingEach.substring(0, savingEach.indexOf('.') + 3);
					}
				}
				final Label savingLabel = new Label("\u00A3" + savingEach + " each");
				savingLabel.addStyleName("fareLabel");
				savingPanel.add(savingLabel);
				final Label savingEachLabel = new Label("estimated");
				savingEachLabel.setStyleName("infoLabel");
				savingPanel.add(savingEachLabel);
				
				add(savingPanel);			
			}

			add(farePanel);
			add(timePanel);

		}

		if (taxi.getStatus().equals("left"))
		{
			setStyleName("taxiPanelLeft");
		}
		else if (spaceLeft == 0)
		{
			setStyleName("taxiPanelFull");
		}
		else
		{
			setStyleName("taxiPanel");
		}
	}
}