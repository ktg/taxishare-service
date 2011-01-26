package bzb.gwt.taxishare.client;

import java.util.ArrayList;
import java.util.Iterator;

import bzb.gwt.taxishare.client.model.Destination;
import bzb.gwt.taxishare.client.model.Taxi;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionResults;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MapPanel extends VerticalPanel
{

	private static MapWidget map;
	private static LatLng currentLocation;

	private static final int DEFAULT_ZOOM_LEVEL = 12;

	private static int zoomLevel = DEFAULT_ZOOM_LEVEL;

	private static ArrayList<LatLng> routeCenters;

	public MapPanel()
	{
		setWidth((Window.getClientWidth() - 40) + "px");
		setStyleName("mapPanel");

		Maps.loadMapsApi(	"ABQIAAAAhuw3_xPyyS01u9B62XUBMxSlNSnRi6rmoiaafuWcJxiCAAlTnRSrvdk4WuMn96c4QvLabvNEtXGNMg",
							"2",
							false, new Runnable()
							{
								@Override
								public void run()
								{
									currentLocation = LatLng.newInstance(52.939138, -1.203244);
									map = new MapWidget(currentLocation, DEFAULT_ZOOM_LEVEL);
									map.setSize((Window.getClientWidth() - 40) + "px", Window.getClientHeight() * 0.6
											+ "px");
									add(map);
								}
							});
	}

	public void addRoutes(final Destination hostDestination, final JsArray<Taxi> taxis)
	{
		if (map != null)
		{
			map.clearOverlays();
			zoomLevel = DEFAULT_ZOOM_LEVEL;
			routeCenters = new ArrayList<LatLng>();
			for (int index = 0; index < taxis.length(); index++)
			{
				final Taxi taxi = taxis.get(index);
				final DirectionQueryOptions opts = new DirectionQueryOptions(map, null);
				final String query = "from: " + hostDestination.getPostcode() + " to: "
						+ taxi.getDestination().getPostcode();
				Directions.load(query, opts, new DirectionsCallback()
				{
					@Override
					public void onFailure(final int statusCode)
					{
					}

					@Override
					public void onSuccess(final DirectionResults result)
					{
						final int newZoomLevel = map.getBoundsZoomLevel(result.getBounds());
						routeCenters.add(result.getBounds().getCenter());

						if (newZoomLevel < zoomLevel)
						{
							zoomLevel = newZoomLevel;
						}
					}
				});

			}
		}
	}

	public void zoom()
	{
		if (map != null)
		{
			if (routeCenters != null)
			{
				final Iterator<LatLng> it = routeCenters.iterator();
				double lat = 0;
				double lng = 0;
				while (it.hasNext())
				{
					final LatLng center = it.next();
					lat += center.getLatitude();
					lng += center.getLongitude();
				}
				map.setCenter(LatLng.newInstance(lat / routeCenters.size(), lng / routeCenters.size()), zoomLevel);
			}
			else
			{
				map.setCenter(currentLocation, zoomLevel);
			}
		}
	}
}
