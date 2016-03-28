package com.itelg.docker.cawandu.composer.zk.events;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

public class OtherTabsDeleteEvent extends Event
{
	private static final long serialVersionUID = 1715192721893678756L;
	private final String tabId;
	private final Side side;

	public OtherTabsDeleteEvent(String tabId, Side side, Window window)
	{
		super(OtherTabsDeleteEvent.class.getName(), null, window);
		this.tabId = tabId;
		this.side = side;
	}

	public Window getWindow()
	{
		return (Window) getData();
	}

	public String getTabId()
	{
		return tabId;
	}

	public Side getSide()
	{
		return side;
	}

	public enum Side
	{
		LEFT, RIGHT;
	}
}