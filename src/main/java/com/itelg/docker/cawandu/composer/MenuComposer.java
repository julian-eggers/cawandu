package com.itelg.docker.cawandu.composer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

import com.itelg.docker.cawandu.composer.zk.events.TabClosedEvent;
import com.itelg.docker.cawandu.composer.zk.events.TabCreatedEvent;

import de.jaggl.utils.events.zk.annotations.Processing;

@Component
@Scope("request")
public class MenuComposer extends AbstractComposer<Menubar>
{
    private static final long serialVersionUID = 8993931910418579243L;
    private static final int MAX_TITLE_LENGTH = 30;
    private @Autowired Environment environment;

    private @Wire Tabs menuTabs;
    private @Wire Tabpanels menuTabpanels;
    private @Wire Menuitem idMenuitem;

    @Override
    public void doAfterCompose(Menubar menubar) throws Exception
    {
        super.doAfterCompose(menubar);
        String version = idMenuitem.getLabel();
        version = version.replace("{version}", environment.getProperty("info.version"));

        try
        {
            version = version.replace("{timestamp}", environment.getProperty("info.buildTimestamp"));
        }
        catch (Exception e)
        {
            version = version.replace("{timestamp}", "Unknown");
        }

        idMenuitem.setLabel(version);
    }

    @Processing(TabCreatedEvent.class)
    private void onCreateTab(TabCreatedEvent tabCreatedEvent)
    {
        Tab menuTab = new Tab(tabCreatedEvent.getWindow().getTitle());
        menuTab.setId(tabCreatedEvent.getTabId() + "_Tab");
        menuTab.setClosable(true);
        menuTab.setSelected(true);
        menuTabs.appendChild(menuTab);

        Tabpanel menuTabpanel = new Tabpanel();
        menuTabpanel.setId(tabCreatedEvent.getTabId() + "_Tabpanel");
        menuTabpanel.appendChild(tabCreatedEvent.getWindow());
        menuTabpanels.appendChild(menuTabpanel);

        Window window = tabCreatedEvent.getWindow();
        window.setTitle(null);
        window.addEventListener("onTitleChange", event ->
        {
            String title = (String) event.getData();
            menuTab.setLabel(title);
            window.setTitle(null);

            if (title.length() > MAX_TITLE_LENGTH)
            {
                menuTab.setTooltiptext(title);
                menuTab.setLabel(title.substring(0, MAX_TITLE_LENGTH) + "...");
            }
        });

        menuTab.addEventListener(Events.ON_CLOSE, event ->
        {
            publish(new TabClosedEvent(window));
        });
    }
}