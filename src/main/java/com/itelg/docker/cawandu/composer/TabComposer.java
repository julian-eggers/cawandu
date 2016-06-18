package com.itelg.docker.cawandu.composer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import com.itelg.docker.cawandu.composer.zk.events.TabCreatedEvent;

public abstract class TabComposer extends AbstractComposer<Window>
{
    private static final Logger log = LoggerFactory.getLogger(TabComposer.class);
    private static final long serialVersionUID = 4358837585811186910L;
    private String tabId = String.valueOf(System.currentTimeMillis());

    @Override
    public void doAfterCompose(Window window) throws Exception
    {
        try
        {
            super.doAfterCompose(window);
            publish(new TabCreatedEvent(tabId, getSelf()));
            afterCompose();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    protected void afterCompose()
    {

    }

    protected void setTitle(String title)
    {
        Events.postEvent("onTitleChange", getSelf(), title);
    }

    protected void close()
    {
        close(null);
    }

    protected void close(Object data)
    {
        Events.postEvent(Events.ON_CLOSE, getSelf(), data);
    }
}