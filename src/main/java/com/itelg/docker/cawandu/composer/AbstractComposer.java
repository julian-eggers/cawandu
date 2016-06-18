package com.itelg.docker.cawandu.composer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.itelg.docker.cawandu.composer.zk.Position;
import com.itelg.docker.cawandu.composer.zk.WireArg;

import de.jaggl.utils.events.zk.ZKEventQueue;

public abstract class AbstractComposer<T extends Component> extends SelectorComposer<T>
{
    private static final long serialVersionUID = 6674424476041331852L;
    protected static final String QUEUE_NAME = "DEFAULT_EVENT_QUEUE";

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo)
    {
        ReflectionUtils.doWithFields(getClass(), new FieldCallback()
        {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
            {
                WireArg wireArg = field.getAnnotation(WireArg.class);

                if (wireArg != null)
                {
                    Object arg = Executions.getCurrent().getArg().get(wireArg.value());

                    if (arg != null && ClassUtils.primitiveToWrapper(field.getType()).isAssignableFrom(arg.getClass()))
                    {
                        field.setAccessible(true);
                        field.set(AbstractComposer.this, arg);
                    }
                }
            }
        });

        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(final T comp) throws Exception
    {
        super.doAfterCompose(comp);
        ZKEventQueue.subscribe(QUEUE_NAME, this);
    }

    public void publish(Event event)
    {
        ZKEventQueue.publish(QUEUE_NAME, event);
    }

    // Create new windows
    protected static Component show(String uri, Position position, Map<String, Object> args, Component parent)
    {
        Map<String, Object> data = new HashMap<String, Object>();

        if (args != null)
        {
            data.putAll(args);
        }

        data.put("left", position != null ? position.getLeft() : null);
        data.put("top", position != null ? position.getTop() : null);
        data.put("position", position == null ? "center" : null);

        return Executions.createComponents(uri, parent, data);
    }

    protected static Component show(String uri, Map<String, Object> args, Component parent)
    {
        return show(uri, null, args, parent);
    }

    protected static Component show(String uri, Position position, Map<String, Object> args)
    {
        return show(uri, position, args, null);
    }

    protected static Component show(String uri, Map<String, Object> args)
    {
        return show(uri, null, args, null);
    }

    protected static Component show(String uri)
    {
        return show(uri, null, null, null);
    }

    protected static Component show(String uri, Position position)
    {
        return show(uri, position, null, null);
    }

    // Notifications & alerts
    public void showNotification(String message)
    {
        Clients.showNotification(message, "info", null, "bottom_right", 2000, true);
    }

    public void showInfo(String message)
    {
        Messagebox.show(message);
    }

    public void showWarning(String message)
    {
        Messagebox.show(message, "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
    }

    public void showError(String message)
    {
        Messagebox.show(message, "Error", Messagebox.OK, Messagebox.ERROR);
    }
}