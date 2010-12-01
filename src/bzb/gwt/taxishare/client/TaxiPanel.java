package bzb.gwt.taxishare.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import bzb.gwt.taxishare.client.model.Taxi;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaxiPanel extends HorizontalPanel
{
	private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MMM-yyyy HH:mm:ss");
	
	public TaxiPanel(Taxi taxi)
	{
		setWidth((Window.getClientWidth() - 40) + "px");

		DockPanel idPanel = new DockPanel();
		Label idLabel = new Label("TAXI" + String.valueOf(taxi.getId()));
		idLabel.addStyleName("idLabel");
		idPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		idPanel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
		idPanel.add(idLabel, DockPanel.CENTER);

		int spaceLeft = taxi.getTotalSpace() - taxi.getPeople().length();
		
		Panel spacesPanel;
		if (spaceLeft == 0)
		{
			spacesPanel = new DockPanel();
			((DockPanel) spacesPanel).setHorizontalAlignment(DockPanel.ALIGN_CENTER);
			((DockPanel) spacesPanel).setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
			Label fullLabel = new Label("FULL");
			fullLabel.addStyleName("idLabel");
			((DockPanel) spacesPanel).add(fullLabel, DockPanel.CENTER);
		}
		else
		{
			spacesPanel = new VerticalPanel();
			spacesPanel.add(new Label(String.valueOf(spaceLeft) + " seat(s) available"));
			FlowPanel spacesIcons = new FlowPanel();
			for (int i = 0; i < taxi.getTotalSpace() - spaceLeft; i++)
			{
				if (taxi.getStatus().equals("unconfirmed"))
				{
					Image fadedIcon = new Image("freespace.png");
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
				Image fadedIcon = new Image("freespace.png");
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

		DockPanel destinationPanel = new DockPanel();
		Label destinationLabel = new Label(taxi.getDestination().getName());
		destinationLabel.addStyleName("destinationNameLabel");
		destinationPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		destinationPanel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
		destinationPanel.add(destinationLabel, DockPanel.CENTER);

		VerticalPanel timePanel = new VerticalPanel();
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
				departureTimeLabel = new Label(DateTimeFormat.getFormat("h:mm a").format(dateFormat.parse(taxi.getPickupTime())));
				departureTimeLabel.addStyleName("departureTimeLabel");
			}
			catch (Exception e)
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
						(int) Math.round(((double) (dateFormat.parse(taxi.getArrivalTime()).getTime() - dateFormat.parse(taxi.getPickupTime()).getTime()) / (60.0 * 1000))) + " mins");
				arrivalTimeLabel.addStyleName("arrivalTimeLabel");
			}
			catch (Exception e)
			{
				arrivalTimeLabel = new Label("unconfirmed");
			}
		}
		timePanel.add(arrivalTimeLabel);

		VerticalPanel farePanel = new VerticalPanel();
		farePanel.add(new Label("Current fare:"));
		String priceEach = String.valueOf(((double) taxi.getPredictedCost() / (double) (taxi.getTotalSpace() - spaceLeft)));
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
		Label priceLabel = new Label("\u00A3" + priceEach);
		priceLabel.addStyleName("fareLabel");
		farePanel.add(priceLabel);
		farePanel.add(new Label("each; \u00A3" + taxi.getPredictedCost() + " total (est.)"));

		VerticalPanel savingPanel = new VerticalPanel();
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
		Label savingLabel = new Label("\u00A3" + savingEach);
		savingLabel.addStyleName("fareLabel");
		savingPanel.add(savingLabel);
		savingPanel.add(new Label("each (estimated)"));

		add(idPanel);
		add(spacesPanel);
		add(destinationPanel);
		add(timePanel);
		add(farePanel);
		add(savingPanel);

		if (taxi.getStatus().equals("unconfirmed"))
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