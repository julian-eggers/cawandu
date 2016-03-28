package com.itelg.docker.cawandu.composer.zk.events;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

public class TabDeleteEvent extends Event
{
	private static final long serialVersionUID = -4042638927767041356L;
	private String tabId;

	public TabDeleteEvent(String tabId, Window window)
	{
		super(TabDeleteEvent.class.getName(), null, window);
		this.tabId = tabId;
	}

	public Window getWindow()
	{
		return (Window) getData();
	}
	
	public String getTabId()
	{
		return tabId;
	}
}