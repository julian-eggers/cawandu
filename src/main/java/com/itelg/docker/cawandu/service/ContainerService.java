package com.itelg.docker.cawandu.service;

import java.util.List;

import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.container.ContainerFilter;

public interface ContainerService
{
    void renameContainer(Container container, String newName);
    void recreateContainer(Container container);
    
    void startContainer(Container container);
    void stopContainer(Container container);
    void restartContainer(Container container);
    void removeContainer(Container container);
    void killContainer(Container container);
    
    Container getContainerById(String id);
    Container getContainerByName(String name);
    
    List<Container> getContainersByFilter(ContainerFilter filter);
    List<Container> getAllContainers();
}