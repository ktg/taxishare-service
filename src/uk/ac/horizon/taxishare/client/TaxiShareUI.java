package uk.ac.horizon.taxishare.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.adamtacy.client.ui.effects.events.EffectCompletedEvent;
import org.adamtacy.client.ui.effects.events.EffectCompletedHandler;
import org.adamtacy.client.ui.effects.impl.Fade;
import org.adamtacy.client.ui.effects.impl.Highlight;
import org.adamtacy.client.ui.effects.impl.NShow;
import org.adamtacy.client.ui.effects.impl.SlideLeft;

import uk.ac.horizon.taxishare.client.model.Destination;
import uk.ac.horizon.taxishare.client.model.Instance;
import uk.ac.horizon.taxishare.client.model.Taxi;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaxiShareUI implements EntryPoint
{
	// private static final GreetingServiceAsync greetingService = GWT
	// .create(GreetingService.class);
	private static final TaxiShareService service = new TaxiShareServiceImpl();
	private static String response;

	private static Instance instance;

	private static final Collection<String> destinations = new HashSet<String>();
	private static int pageNum = 0;

	private static final int PAGE_TIMEOUT = 20000;
	private static final int TIMEOUT_INDICATOR = 1000;
	private static final int CLOCK_UPDATE = 1000;

	private static Fade f = new Fade();
	private static NShow ns = new NShow();
	private static Fade fPage = new Fade();
	private static Highlight mf;

	private static final Image busyIcon = new Image("busy.gif");

	private static List<List<TaxiPanel>> pages = new ArrayList<List<TaxiPanel>>();

	private static MapPanel mp = new MapPanel();
	private static VerticalPanel vPanel = new VerticalPanel();
	private static HorizontalPanel hPanel = new HorizontalPanel();
	private static DockPanel ivPanel = new DockPanel();

	private static Timer countdownTimer = new Timer()
	{
		@Override
		public void run()
		{
			f.play();
		}
	};

	static final Label timeLabel = new Label();

	static final Label pageLabel = new Label();

	static Label infoLabel = new Label();

	private static final int ph = 120;

	private static void loadAttsPanel()
	{
		RootPanel.get("page").add(pageLabel);
		RootPanel.get("pageAtts").add(timeLabel);
		final Timer pageAttsTimer = new Timer()
		{
			@Override
			public void run()
			{
				timeLabel.setText(DateTimeFormat.getFormat("h:mm:ss a").format(new Date()));
				timeLabel.setStyleName("timeLabel");
			}
		};
		pageAttsTimer.scheduleRepeating(CLOCK_UPDATE);
	}

	private static void loadCurrentPage()
	{
		pageLabel.setText("Page " + (pageNum + 1) + "/" + (pages.size() + 1));
		countdownTimer.schedule(PAGE_TIMEOUT - TIMEOUT_INDICATOR);
		fPage.play();
		if (pageNum == 0)
		{
			requestUpdate();
		}
	}

	private static void loadDestinationPanel()
	{
		final Label destinationTitle = new Label("Common destinations");
		destinationTitle.setStyleName("destinationTitleLabel");
		hPanel.add(destinationTitle);
	}

	private static void loadInfoPanel()
	{
		ivPanel.clear();
		ivPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		ivPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		ivPanel.setSpacing(20);

		final VerticalPanel v = new VerticalPanel();
		String instructions;
		if (instance != null)
		{
			infoLabel.setText(instance.getNumber());
			instructions = "To book a taxi, send a text message to "
					+ instance.getNumber()
					+ " containing a one-word name (to identify yourself to the taxi-driver) followed by your destination. ";
		}
		else
		{
			infoLabel.setText("Instructions");
			instructions = "Waiting";
		}
		infoLabel.setStyleName("infoTitleLabel");
		v.add(infoLabel);
		final Label exampleLabel = new Label("e.g. \"ben trainstation\"");
		exampleLabel.addStyleName("exampleLabel");
		v.add(exampleLabel);
		v.addStyleName("examplePanel");
		ivPanel.add(v, DockPanel.WEST);

		String sampleDestination;
		if (instance != null && instance.getDestinations().length() > 0)
		{
			sampleDestination = "You can type one of the destinations listed above (e.g. "
					+ instance.getDestinations().get(0).getName()
					+ "), or specify one of your own choosing (e.g. a postcode or a place name)";
		}
		else
		{
			sampleDestination = "You can specify a destination using a postcode (e.g. ng51bb) or a placename (e.g. Victoria Centre)";
		}
		final Label instructionLabel = new Label(instructions + sampleDestination);
		instructionLabel.addStyleName("instructionLabel");
		ivPanel.add(instructionLabel, DockPanel.CENTER);

		// ivPanel.add(new Image("horizonIcon.png"), DockPanel.EAST);
	}

	private static final native Instance parseJSONResponse(String json)
	/*-{
		return eval('(' + json + ')');
	}-*/;

	private static void parseResponse(final String response)
	{
		try
		{
			GWT.log(response);
			instance = parseJSONResponse(response);
			pages.clear();

			loadInfoPanel();

			final int heightToFill = (int) (Window.getClientHeight() * 0.4);
			int heightRemaining = heightToFill;
			int currPage = 0;
			final JsArray<Taxi> taxis = instance.getTaxis();
			for (int i = 0; i < taxis.length(); i++)
			{
				if (heightRemaining < 0)
				{
					heightRemaining = heightToFill;
					currPage++;
				}

				if(!taxis.get(i).getStatus().equals("left"))
				{
					final TaxiPanel p = new TaxiPanel(taxis.get(i));
					if (pages.size() <= currPage)
					{
						pages.add(currPage, new ArrayList<TaxiPanel>());
					}
					p.setHeight(ph + "px");
					pages.get(currPage).add(p);
					heightRemaining -= ph;
					}
			}
			mp.addRoutes(instance.getLocation(), instance.getTaxis());

			final JsArray<Destination> newDests = instance.getDestinations();
			for (int index = 0; index < newDests.length(); index++)
			{
				final Destination destination = newDests.get(index);
				if (!destinations.contains(destination.getName()))
				{
					destinations.add(destination.getName());
					final Label thisDestination = new Label(destination.getName());
					thisDestination.setStyleName("destinationLabel");
					final SlideLeft sl = new SlideLeft();
					sl.setEffectElement(thisDestination.getElement());
					hPanel.add(thisDestination);
					sl.play();
				}
			}
			if (hPanel.getOffsetWidth() > Window.getClientWidth() * 0.6)
			{
				RootPanel.get("destinationPanel").setStyleName("destinationPanelScroll");
			}
			else
			{
				RootPanel.get("destinationPanel").removeStyleName("destinationPanelScroll");
			}
		}
		catch (final Exception e)
		{
			GWT.log(e.getMessage(), e);
		}
	}

	private static void requestUpdate()
	{
		RootPanel.get("progress").add(busyIcon);
		service.getInstance(new RequestCallback()
		{

			@Override
			public void onError(final Request request, final Throwable exception)
			{
				GWT.log(exception.getMessage(), exception);

			}

			@Override
			public void onResponseReceived(final Request request, final Response response)
			{
				if (200 == response.getStatusCode())
				{
					try
					{
						parseResponse(response.getText());
					}
					catch (final Exception e)
					{
						GWT.log(response.getText(), e);
					}
				}
				else
				{
					GWT.log("Response code: " + response.getStatusCode(), null);
					// Handle the error. Can get the status text from response.getStatusText()
				}
			}
		});
	}

	public TaxiShareUI()
	{
		mf = new Highlight(vPanel.getElement());
		f.setEffectElement(vPanel.getElement());
		ns.setEffectElement(vPanel.getElement());
		ns.setDuration(1);
		f.addEffectCompletedHandler(new EffectCompletedHandler()
		{
			@Override
			public void onEffectCompleted(final EffectCompletedEvent evt)
			{
				vPanel.clear();

				if (pages.size() == 0)
				{
					vPanel.add(new Label("System is currently experiencing issues"));
				}
				else if (pageNum < pages.size())
				{
					for (TaxiPanel panel: pages.get(pageNum))
					{
						vPanel.add(panel);
					}
				}
				else
				{
					mp.zoom();
					vPanel.add(mp);
					mf.play();
				}

				ns.play();
			}
		});
		fPage.addEffectElement(pageLabel.getElement());
		fPage.setDuration(PAGE_TIMEOUT / 1000);
	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad()
	{
		if (RootPanel.get("activePanel") != null)
		{
			RootPanel.get("activePanel").add(vPanel);
			RootPanel.get("destinationPanel").add(hPanel);
			RootPanel.get("info").add(ivPanel);

			requestUpdate();

			loadAttsPanel();
			loadDestinationPanel();
			loadInfoPanel();
			loadCurrentPage();
			f.play();

			final Timer nextPageTimer = new Timer()
			{
				@Override
				public void run()
				{
					pageNum++;
					if (pageNum > pages.size())
					{
						pageNum = 0;
					}
					loadCurrentPage();
					fPage.setPosition(0);
				}
			};
			nextPageTimer.scheduleRepeating(PAGE_TIMEOUT);
		}
		else if (RootPanel.get("adminPanel") != null)
		{
			RootPanel.get("adminPanel").add(new TaxiShareAdmin(service));
		}
	}
}