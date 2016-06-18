package com.itelg.docker.cawandu.composer;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

public abstract class PopupComposer extends AbstractComposer<Window>
{
    private static final long serialVersionUID = 4358837585811186910L;

    @Override
    public void doAfterCompose(Window window) throws Exception
    {
        super.doAfterCompose(window);
        afterCompose();
    }

    protected void afterCompose()
    {

    }

    protected void setTitle(String title)
    {
        getSelf().setTitle(title);
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