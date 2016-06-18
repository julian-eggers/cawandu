package com.itelg.docker.cawandu.composer.container;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

import com.itelg.docker.cawandu.composer.PopupComposer;
import com.itelg.docker.cawandu.composer.zk.WireArg;
import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.service.ContainerService;

@Component
@Scope("request")
public class ContainerRenameComposer extends PopupComposer
{
    private static final long serialVersionUID = 1147421985315347809L;
    private transient @Autowired ContainerService containerService;

    private @Wire Textbox nameTextbox;
    private @WireArg("container") Container container;

    @Override
    protected void afterCompose()
    {
        nameTextbox.setValue(container.getName());
        setTitle("Rename: " + container.getName());
    }

    @Listen("onClick = #renameButton")
    public void onRename()
    {
        Clients.clearWrongValue(nameTextbox);
        String name = nameTextbox.getValue().trim();

        if (StringUtils.isBlank(name))
        {
            throw new WrongValueException(nameTextbox, "Enter a name!");
        }

        if (StringUtils.equals(name, container.getName()))
        {
            throw new WrongValueException(nameTextbox, "Choose another name!");
        }

        if (!name.matches(Container.CONTAINER_NAME_PATTERN))
        {
            throw new WrongValueException(nameTextbox, "Invalid name!");
        }

        Container existingContainer = containerService.getContainerByName(name);

        if (existingContainer != null && !existingContainer.getId().equals(container.getId()))
        {
            throw new WrongValueException(nameTextbox, "Name is already taken!");
        }

        containerService.renameContainer(container, name);
        close(container);
    }

    public static org.zkoss.zk.ui.Component show(Container container)
    {
        return show("/container/rename.zul", Collections.singletonMap("container", container));
    }
}