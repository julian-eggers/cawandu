package com.itelg.docker.cawandu.composer.image;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Textbox;

import com.itelg.docker.cawandu.composer.TabComposer;
import com.itelg.docker.cawandu.composer.container.ContainerListComposer;
import com.itelg.docker.cawandu.composer.zk.WireArg;
import com.itelg.docker.cawandu.domain.container.ContainerFilter;
import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.domain.image.ImageFilter;
import com.itelg.docker.cawandu.domain.image.UpdateState;
import com.itelg.docker.cawandu.service.ImageService;
import com.itelg.zkoss.helper.listbox.ListboxHelper;
import com.itelg.zkoss.helper.listbox.ListcellHelper;

@Component
@Scope("request")
public class ImageListComposer extends TabComposer
{
    private static final long serialVersionUID = -5360705760494227016L;
    private transient @Autowired ImageService imageService;
    
    private @Wire Listbox imageListbox;
    private @Wire Textbox nameTextbox;
    private @Wire Textbox idTextbox;
    private @WireArg("filter") ImageFilter filter;
    
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
        nameTextbox.setValue(filter.getName());
        idTextbox.setValue(filter.getId());
    }
    
    private void initListbox()
    {
        imageListbox.setItemRenderer(new ImageListitemRenderer());
    }
    
    private void refreshListbox()
    {
        imageListbox.setModel(new ListModelList<>(imageService.getImagesByFilter(filter)));
        ListboxHelper.hideIfEmpty(imageListbox, "No images found");
    }
    
    private String buildTitle()
    {
        String title = "Images";
        List<String> filterProperties = new ArrayList<>();

        if (StringUtils.isNotBlank(filter.getName()))
        {
            filterProperties.add("Name: " + filter.getName());
        }
        
        if (StringUtils.isNotBlank(filter.getId()))
        {
            filterProperties.add("ID: " + filter.getId());
        }

        if (filterProperties.isEmpty() == false)
        {
            title += " (" + StringUtils.join(filterProperties, ", ") + ")";
        }

        return title;
    }
    
    private class ImageListitemRenderer implements ListitemRenderer<Image>
    {
        @Override
        public void render(Listitem item, Image image, int index) throws Exception
        {
            Menupopup popup = new Menupopup();
            getSelf().appendChild(popup);
            item.setContext(popup);
            item.setPopup(popup);
            item.setValue(image);
            
            Menuitem showContainersMenuitem = new Menuitem("Show containers");
            showContainersMenuitem.setParent(popup);
            showContainersMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                ContainerFilter filter = new ContainerFilter();
                filter.setImageName(image.getName());
                ContainerListComposer.show(filter);
            });
            
            Menuitem pullImageMenuitem = new Menuitem("Pull image");
            pullImageMenuitem.setParent(popup);
            pullImageMenuitem.setDisabled(!image.isPullable());
            pullImageMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                UpdateState state = imageService.pullImage(image);
                
                if (state == UpdateState.PULLED)
                {
                    showNotification("New version pulled");
                    refreshListbox();
                }
                else if (state == UpdateState.NO_UPDATE) 
                {
                    showNotification("No update available");
                }
                else if (state == UpdateState.NO_ACCESS)
                {
                    showWarning("No access to private repository!");
                }
                else 
                {
                    showWarning("Unknown error");
                }
            });
            
            Menuitem removeImageMenuitem = new Menuitem("Remove image");
            removeImageMenuitem.setParent(popup);
            removeImageMenuitem.addEventListener(Events.ON_CLICK, event ->
            {
                if (imageService.removeImage(image))
                {
                    showNotification("Image removed");
                    refreshListbox();
                }
                else
                {
                    showWarning("Failed to delete image");
                }
            });
            
            new Listcell(image.getId()).setParent(item);
            new Listcell(image.getName()).setParent(item);
            new Listcell(image.getSize() + " MB").setParent(item);
            ListcellHelper.buildDateTimeListcell(image.getCreated()).setParent(item);
        }
    }
    
    @Listen("onClick = #searchSubmitButton")
    public void onExecuteFilter()
    {
        filter.setName(nameTextbox.getValue().trim());
        filter.setId(idTextbox.getValue().trim());

        refreshListbox();
        setTitle(buildTitle());
    }

    @Listen("onClick = #searchResetButton")
    public void onResetFilter()
    {
        nameTextbox.setValue("");
        idTextbox.setValue("");
        onExecuteFilter();
    }
    
    @Listen("onClick = #removeUnusedImages")
    public void onRemoveUnusedImages()
    {
        List<Image> removedImages = imageService.removedUnusedImages();
        showNotification(removedImages.size() + " images removed");
        refreshListbox();
    }
    
    public static void show()
    {
        show(new ImageFilter());
    }
    
    public static void show (ImageFilter filter)
    {
        show("/image/list.zul", Collections.singletonMap("filter", filter));
    }
}