package com.itelg.docker.cawandu.composer.container;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import com.itelg.docker.cawandu.composer.PopupComposer;
import com.itelg.docker.cawandu.composer.zk.WireArg;
import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.service.ContainerService;
import com.itelg.docker.cawandu.service.RegistryService;
import com.itelg.zkoss.helper.combobox.ComboboxHelper;

@Component
@Scope("request")
public class ContainerSwitchTagComposer extends PopupComposer
{
    private static final long serialVersionUID = 1147421985315347809L;
    private transient @Autowired ContainerService containerService;
    private transient @Autowired RegistryService registryService;
    
    private @Wire Combobox tagsCombobox;
    private @WireArg("container") Container container;
    
    @Override
    protected void afterCompose()
    {
        setTitle("Switch tag: " + container.getName());
        List<String> tags = registryService.getImageTagsByName(container.getImageNameWithoutTag());
        ComboboxHelper.init(tagsCombobox, tags, container.getImageTag(), new ComboitemRenderer<String>()
        {
            @Override
            public void render(Comboitem item, String tag, int index) throws Exception
            {
                if (container.getImageTag().equals(tag))
                {
                    item.setDisabled(true);
                }
                
                item.setValue(tag);
                item.setLabel(tag);
            }
        });
    }
    
    @Listen("onClick = #switchTagButton")
    public void onSwitchTag()
    {
        String tag = tagsCombobox.getSelectedItem().getValue();
        
        if (!container.getImageTag().equals(tag))
        {
            containerService.switchTag(container, tag);
            close(container);            
        }
    }
    
    public static org.zkoss.zk.ui.Component show(Container container)
    {
        return show("/container/switchtag.zul", Collections.singletonMap("container", container));
    }
}