package uk.ac.horizon.taxishare.client;

import uk.ac.horizon.taxishare.client.model.Taxi;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaxiPanel extends HorizontalPanel
{
	public TaxiPanel(final Taxi taxi)
	{
		setWidth((Window.getClientWidth() - 40) + "px");

		final SimplePanel idPanel = new SimplePanel();
		final Label idLabel = new Label("TAXI" + String.valueOf(taxi.getId()));
		idLabel.addStyleName("idLabel");
		// idPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// idPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		idPanel.add(idLabel);

		final int spaceLeft = taxi.getTotalSpace() - taxi.getPeople().length();

		Panel spacesPanel;
		if (spaceLeft == 0)
		{
			spacesPanel = new SimplePanel();
			// ((DockPanel)
			// spacesPanel).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			// ((DockPanel) spacesPanel).setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			final Label fullLabel = new Label("FULL");
			fullLabel.addStyleName("idLabel");
			spacesPanel.add(fullLabel);
		}
		else
		{
			spacesPanel = new VerticalPanel();
			spacesPanel.add(new Label(String.valueOf(spaceLeft) + " seat(s) available"));
			final FlowPanel spacesIcons = new FlowPanel();
			for (int i = 0; i < taxi.getTotalSpace() - spaceLeft; i++)
			{
				if (taxi.getStatus().equals("unconfirmed"))
				{
					final Image fadedIcon = new Image("freespace.png");
					fadedIcon.addStyleName("fadedIcon");
					spacesIcons.add(fadedIcon);
				}
				else
				{
					spacesIcons.add(new Image("freespace.png"));
				}
			}
			for (int i = 0; i < spaceLeft; i++)
			{
				final Image fadedIcon = new Image("freespace.png");
				if (taxi.getStatus().equals("unconfirmed"))
				{
					fadedIcon.addStyleName("veryFadedIcon");
				}
				else
				{
					fadedIcon.addStyleName("fadedIcon");
				}
				spacesIcons.add(fadedIcon);
			}
			spacesPanel.add(spacesIcons);
		}

		final SimplePanel destinationPanel = new SimplePanel();
		final Label destinationLabel = new Label(taxi.getDestination().getName());
		destinationLabel.addStyleName("destinationNameLabel");
		// destinationPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// destinationPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		destinationPanel.add(destinationLabel);

		final VerticalPanel timePanel = new VerticalPanel();
		timePanel.add(new Label("Pickup:"));
		Label departureTimeLabel;
		if (taxi.getStatus().equals("unconfirmed"))
		{
			departureTimeLabel = new Label("unconfirmed");
		}
		else
		{
			try
			{
				departureTimeLabel = new Label(DateTimeFormat.getFormat("h:mm a").format(Taxi.dateFormat.parse(taxi
																									.getPickupTime())));
				departureTimeLabel.addStyleName("departureTimeLabel");
			}
			catch (final Exception e)
			{
				departureTimeLabel = new Label("unconfirmed");
				e.printStackTrace();
			}
		}
		timePanel.add(departureTimeLabel);
		timePanel.add(new Label("Journey:"));
		Label arrivalTimeLabel;
		if (taxi.getStatus().equals("unconfirmed"))
		{
			arrivalTimeLabel = new Label("unconfirmed");
		}
		else
		{
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
		}
		timePanel.add(arrivalTimeLabel);

		final VerticalPanel farePanel = new VerticalPanel();
		farePanel.add(new Label("Current fare:"));
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
		final Label priceLabel = new Label("\u00A3" + priceEach);
		priceLabel.addStyleName("fareLabel");
		farePanel.add(priceLabel);
		farePanel.add(new Label("each; \u00A3" + taxi.getPredictedCost() + " total (est.)"));

		final VerticalPanel savingPanel = new VerticalPanel();
		savingPanel.add(new Label("Fare when full:"));
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
		final Label savingLabel = new Label("\u00A3" + savingEach);
		savingLabel.addStyleName("fareLabel");
		savingPanel.add(savingLabel);
		savingPanel.add(new Label("each (estimated)"));

		add(idPanel);
		add(spacesPanel);
		add(destinationPanel);
		add(timePanel);
		add(farePanel);
		add(savingPanel);

		if (taxi.getStatus().equals("unconfirmed") || taxi.getStatus().equals("left"))
		{
			for (int i = 0; i < getWidgetCount() - 1; i++)
			{
				getWidget(i).setStyleName("taxiBoxUnconfirmed");
			}
			getWidget(getWidgetCount() - 1).setStyleName("taxiBoxUnconfirmedLast");
			setStyleName("taxiPanelUnconfirmed");
		}
		else if (spaceLeft == 0)
		{
			for (int i = 0; i < getWidgetCount() - 1; i++)
			{
				getWidget(i).setStyleName("taxiBoxFull");
			}
			getWidget(getWidgetCount() - 1).setStyleName("taxiBoxFullLast");
			setStyleName("taxiPanelFull");
		}
		else
		{
			for (int i = 0; i < getWidgetCount() - 1; i++)
			{
				getWidget(i).setStyleName("taxiBox");
			}
			getWidget(getWidgetCount() - 1).setStyleName("taxiBoxGreenLast");
			setStyleName("taxiPanel");
		}
	}
}