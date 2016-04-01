package com.itelg.docker.cawandu.composer.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Textbox;

import com.itelg.docker.cawandu.composer.TabComposer;
import com.itelg.docker.cawandu.composer.zk.WireArg;
import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.container.ContainerFilter;
import com.itelg.docker.cawandu.domain.container.ContainerState;
import com.itelg.docker.cawandu.service.ContainerService;
import com.itelg.zkoss.helper.combobox.ComboboxHelper;
import com.itelg.zkoss.helper.i18n.Labels;
import com.itelg.zkoss.helper.listbox.ListboxHelper;
import com.itelg.zkoss.helper.listbox.ListcellHelper;

@Component
@Scope("request")
public class ContainerListComposer extends TabComposer
{
    private static final long serialVersionUID = 1714597547432917497L;
    private transient @Autowired ContainerService containerService;

    private @Wire Listbox containerListbox;
    private @Wire Combobox stateCombobox;
    private @Wire Textbox nameTextbox;
    private @Wire Textbox idTextbox;
    private @Wire Textbox imageNameTextbox;
    private @WireArg("filter") ContainerFilter filter;
    
    @Override
    protected void afterCompose()
    {
        initFilter();
        initListbox();
        refreshListbox();
        setTitle(buildTitle());
    }
    
    private void initFilter()
    {
        ComboboxHelper.setDefaultItem(stateCombobox, "Show all");
        ComboboxHelper.init(stateCombobox, ContainerState.values(), filter.getState(), (ComboitemRenderer<ContainerState>) (item, status, index) ->
        {
            item.setValue(status);
            item.setLabel(Labels.getLabel(status));
        });
        
        nameTextbox.setValue(filter.getName());
        idTextbox.setValue(filter.getId());
        imageNameTextbox.setValue(filter.getImageName());
    }
    
    private void initListbox()
    {
        containerListbox.setItemRenderer(new ContainerListitemRenderer());
    }
    
    private void refreshListbox()
    {
        containerListbox.setModel(new ListModelList<>(containerService.getContainersByFilter(filter)));
        ListboxHelper.hideIfEmpty(containerListbox, "No containers found");
    }
    
    private String buildTitle()
    {
        String title = "Container";
        List<String> filterProperties = new ArrayList<>();

        if (filter.getState() != null)
        {
            filterProperties.add("State: " + Labels.getLabel(filter.getState()));
        }

        if (StringUtils.isNotBlank(filter.getName()))
        {
            filterProperties.add("Name: " + filter.getName());
        }
        
        if (StringUtils.isNotBlank(filter.getId()))
        {
            filterProperties.add("ID: " + filter.getId());
        }
        
        if (StringUtils.isNotBlank(filter.getImageName()))
        {
            filterProperties.add("Image-Name: " + filter.getImageName());
        }

        if (filterProperties.isEmpty() == false)
        {
            title += " (" + StringUtils.join(filterProperties, ", ") + ")";
        }

        return title;
    }
    
    private class ContainerListitemRenderer implements ListitemRenderer<Container>
    {
        @Override
        public void render(Listitem item, Container container, int index) throws Exception
        {
            Menupopup popup = new Menupopup();
            getSelf().appendChild(popup);
            item.setContext(popup);
            item.setPopup(popup);
            item.setValue(container);
            
            Menuitem startContainerMenuitem = new Menuitem("Start container");
            startContainerMenuitem.setParent(popup);
            startContainerMenuitem.setIconSclass("z-icon-play");
            startContainerMenuitem.setDisabled(!container.isStartable());
            startContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.startContainer(container);
                refreshListbox();
                showNotification("Container started");
            });
            
            Menuitem restartContainerMenuitem = new Menuitem("Restart container");
            restartContainerMenuitem.setParent(popup);
            restartContainerMenuitem.setIconSclass("z-icon-fast-forward");
            restartContainerMenuitem.setDisabled(!container.isRestartable());
            restartContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.restartContainer(container);
                refreshListbox();
                showNotification("Container restarted");
            });
            
            Menuitem stopContainerMenuitem = new Menuitem("Stop container");
            stopContainerMenuitem.setParent(popup);
            stopContainerMenuitem.setIconSclass("z-icon-stop");
            stopContainerMenuitem.setDisabled(!container.isStoppable());
            stopContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.stopContainer(container);
                refreshListbox();
                showNotification("Container stopped");
            });
            
            Menuitem removeContainerMenuitem = new Menuitem("Remove container");
            removeContainerMenuitem.setParent(popup);
            removeContainerMenuitem.setIconSclass("z-icon-times");
            removeContainerMenuitem.setDisabled(!container.isRemovable());
            removeContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.removeContainer(container);
                refreshListbox();
                showNotification("Container removed");
            });
            
            Menuitem killContainerMenuitem = new Menuitem("Kill container");
            killContainerMenuitem.setParent(popup);
            killContainerMenuitem.setIconSclass("z-icon-flash");
            killContainerMenuitem.setDisabled(!container.isKillable());
            killContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.killContainer(container);
                refreshListbox();
                showNotification("Container killed");
            });
            
            popup.appendChild(new Menuseparator());
            
            Menuitem updateContainerMenuitem = new Menuitem("Update container");
            updateContainerMenuitem.setParent(popup);
            updateContainerMenuitem.setIconSclass("z-icon-times");
            updateContainerMenuitem.setDisabled(!container.hasUpdate());
            updateContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.recreateContainer(container);
                refreshListbox();
                showNotification("Container updated");
            });
            
            Menuitem switchTagMenuitem = new Menuitem("Switch tag");
            switchTagMenuitem.setParent(popup);
            switchTagMenuitem.setIconSclass("z-icon-sort");
            switchTagMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                org.zkoss.zk.ui.Component composer = ContainerSwitchTagComposer.show(container);
                composer.addEventListener(Events.ON_CLOSE, closeEvent ->
                {
                    if (closeEvent.getData() != null)
                    {
                        refreshListbox();
                        showNotification("Tag swiched");                        
                    }
                });
            });
            
            Menuitem recreateContainerMenuitem = new Menuitem("Recreate container");
            recreateContainerMenuitem.setParent(popup);
            recreateContainerMenuitem.setIconSclass("z-icon-repeat");
            recreateContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                containerService.recreateContainer(container);
                refreshListbox();
                showNotification("Container recreated");
            });
            
            popup.appendChild(new Menuseparator());
            
            Menuitem renameContainerMenuitem = new Menuitem("Rename container");
            renameContainerMenuitem.setParent(popup);
            renameContainerMenuitem.setIconSclass("z-icon-edit");
            renameContainerMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                org.zkoss.zk.ui.Component composer = ContainerRenameComposer.show(container);
                composer.addEventListener(Events.ON_CLOSE, closeEvent ->
                {
                    if (closeEvent.getData() != null)
                    {
                        refreshListbox();
                        showNotification("Container renamed");                        
                    }
                });
            });
            
            new Listcell(container.getId()).setParent(item);
            new Listcell(Labels.getLabel(container.getState())).setParent(item);
            new Listcell(container.getName()).setParent(item);
            Listcell imageNameListcell = new Listcell(container.getImageName());
            imageNameListcell.setParent(item);
            imageNameListcell.setStyle(container.hasUpdate() ? "color:green" : "");
            ListcellHelper.buildDateTimeListcell(container.getCreated()).setParent(item);
        }
    }
    
    @Listen("onClick = #searchSubmitButton")
    public void onExecuteFilter()
    {
        filter.setState(stateCombobox.getSelectedItem().getValue());
        filter.setName(nameTextbox.getValue().trim());
        filter.setId(idTextbox.getValue().trim());
        filter.setImageName(imageNameTextbox.getValue().trim());

        refreshListbox();
        setTitle(buildTitle());
    }

    @Listen("onClick = #searchResetButton")
    public void onResetFilter()
    {
        stateCombobox.setSelectedIndex(0);
        nameTextbox.setValue("");
        idTextbox.setValue("");
        imageNameTextbox.setValue("");
        onExecuteFilter();
    }
    
    public static void show()
    {
        show(new ContainerFilter());
    }
    
    public static void show(ContainerFilter filter)
    {
        show("/container/list.zul", Collections.singletonMap("filter", filter));
    }
}