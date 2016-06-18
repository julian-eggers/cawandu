package com.itelg.docker.cawandu.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.container.ContainerFilter;
import com.itelg.docker.cawandu.repository.ContainerRepository;
import com.itelg.docker.cawandu.service.ContainerService;
import com.itelg.docker.cawandu.service.ImageService;

@Service
public class DefaultContainerService implements ContainerService
{
    private static final Logger log = LoggerFactory.getLogger(DefaultContainerService.class);

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private ImageService imageService;

    @Override
    public void renameContainer(Container container, String newName)
    {
        containerRepository.renameContainer(container, newName);
        log.info("Container renamed (" + container + ")");
    }

    @Override
    public void recreateContainer(Container container)
    {
        imageService.pullImage(container.getImageName());
        containerRepository.recreateContainer(container);
        log.info("Container recreated (" + container + ")");
    }

    @Override
    public void switchTag(Container container, String tag)
    {
        String imageName = container.getImageNameWithoutTag() + ":" + tag;
        container.setImageName(imageName);

        if (container.hasLabel(Container.IMAGE_NAME_LABEL))
        {
            container.addLabel(Container.IMAGE_NAME_LABEL, imageName);
        }

        recreateContainer(container);
    }

    @Override
    public void startContainer(Container container)
    {
        containerRepository.startContainer(container);
        log.info("Container started (" + container + ")");
    }

    @Override
    public void stopContainer(Container container)
    {
        containerRepository.stopContainer(container);
        log.info("Container stopped (" + container + ")");
    }

    @Override
    public void restartContainer(Container container)
    {
        containerRepository.restartContainer(container);
        log.info("Container restarted (" + container + ")");
    }

    @Override
    public void removeContainer(Container container)
    {
        containerRepository.removeContainer(container);
        log.info("Container removed (" + container + ")");
    }

    @Override
    public void killContainer(Container container)
    {
        containerRepository.killContainer(container);
        containerRepository.removeContainer(container);
        log.info("Container killed (" + container + ")");
    }

    @Override
    public Container getContainerById(String id)
    {
        return containerRepository.getContainerById(id);
    }

    @Override
    public Container getContainerByName(String name)
    {
        return containerRepository.getContainerByName(name);
    }

    @Override
    public List<Container> getContainersByFilter(ContainerFilter filter)
    {
        return containerRepository.getContainersByFilter(filter);
    }

    @Override
    public List<Container> getAllContainers()
    {
        return containerRepository.getAllContainers();
    }
}