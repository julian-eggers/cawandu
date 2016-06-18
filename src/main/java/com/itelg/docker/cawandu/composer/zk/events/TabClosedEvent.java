package com.itelg.docker.cawandu.composer.zk.events;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

public class TabClosedEvent extends Event
{
    private static final long serialVersionUID = -4042638927767041356L;

    public TabClosedEvent(Window window)
    {
        super(TabClosedEvent.class.getName(), null, window);
    }

    public Window getWindow()
    {
        return (Window) getData();
    }
}