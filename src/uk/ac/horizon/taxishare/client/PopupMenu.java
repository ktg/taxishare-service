package uk.ac.horizon.taxishare.client;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class PopupMenu extends PopupPanel
{
	private FlowPanel panel = new FlowPanel();
	
	public PopupMenu()
	{
		super(true);
		setWidget(panel);
	}
	
	public void popup(Collection<PopupAction> actions, MouseEvent<?> e)
	{
		panel.clear();
		for(final PopupAction action: actions)
		{
			final Button button = new Button(action.getName());
			button.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(final ClickEvent event)
				{
					action.execute();
					hide();
				}
			});
			button.setStyleName("menuItem");
			panel.add(button);
		}
		setPopupPosition(e.getClientX(), e.getClientY());
		show();
	}
}
