package com.itelg.docker.cawandu.repository.dockerclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.container.ContainerFilter;
import com.itelg.docker.cawandu.domain.container.ContainerState;
import com.itelg.docker.cawandu.repository.ContainerRepository;
import com.itelg.docker.cawandu.repository.dockerclient.converter.ContainerConverter;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerInfo;

@Repository
public class DockerContainerRepository implements ContainerRepository
{
    private static final Logger log = LoggerFactory.getLogger(DockerContainerRepository.class);

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private ContainerConverter containerConverter;

    @Override
    public void renameContainer(Container container, String newName)
    {
        try
        {
            dockerClient.renameContainer(container.getId(), newName);
            container.setName(newName);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }

    @Override
    public void recreateContainer(Container container)
    {
        ContainerInfo containerInfo = getContainerInfoById(container.getId());
        ContainerConfig.Builder containerConfigBuilder = ContainerConfig.builder();
        containerConfigBuilder.hostConfig(containerInfo.hostConfig());
        containerConfigBuilder.env(containerInfo.config().env());
        containerConfigBuilder.labels(container.getLabels());
        containerConfigBuilder.volumes(containerInfo.config().volumes());
        containerConfigBuilder.image(container.getImageName());
        containerConfigBuilder.exposedPorts(containerInfo.config().exposedPorts());
        containerConfigBuilder.user(containerInfo.config().user());
        containerConfigBuilder.workingDir(containerInfo.config().workingDir());

        stopContainer(container);
        removeContainer(container);
        String containerId = createContainer(containerConfigBuilder.build(), container.getName());

        if (container.getState() == ContainerState.UP)
        {
            Container newContainer = getContainerById(containerId);
            startContainer(newContainer);
        }
    }

    private ContainerInfo getContainerInfoById(String id)
    {
        try
        {
            return dockerClient.inspectContainer(id);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private String createContainer(ContainerConfig containerConfig, String name)
    {
        try
        {
            return dockerClient.createContainer(containerConfig, name).id();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public void startContainer(Container container)
    {
        try
        {
            dockerClient.startContainer(container.getId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void stopContainer(Container container)
    {
        try
        {
            dockerClient.stopContainer(container.getId(), 5);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void restartContainer(Container container)
    {
        try
        {
            dockerClient.restartContainer(container.getId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void removeContainer(Container container)
    {
        try
        {
            dockerClient.removeContainer(container.getId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void killContainer(Container container)
    {
        try
        {
            dockerClient.killContainer(container.getId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Container getContainerById(String id)
    {
        ContainerFilter filter = new ContainerFilter();
        filter.setId(id);
        List<Container> containers = getContainersByFilter(filter);

        return (containers.isEmpty() ? null : containers.get(0));
    }

    @Override
    public Container getContainerByName(String name)
    {
        ContainerFilter filter = new ContainerFilter();
        filter.setName(name);
        List<Container> containers = getContainersByFilter(filter);

        return (containers.isEmpty() ? null : containers.get(0));
    }

    @Override
    public List<Container> getContainersByFilter(ContainerFilter filter)
    {
        List<Container> allContainers = getAllContainers();
        List<Container> filteredContainers = new ArrayList<>(allContainers);

        for (Container container : allContainers)
        {
            if (StringUtils.isNotBlank(filter.getId()))
            {
                if (!container.getId().contains(filter.getId()))
                {
                    filteredContainers.remove(container);
                }
            }

            if (StringUtils.isNotBlank(filter.getName()))
            {
                if (!container.getName().contains(filter.getName()))
                {
                    filteredContainers.remove(container);
                }
            }

            if (filter.getState() != null)
            {
                if (container.getState() != filter.getState())
                {
                    filteredContainers.remove(container);
                }
            }

            if (StringUtils.isNotBlank(filter.getImageName()))
            {
                if (!container.getImageName().contains(filter.getImageName()))
                {
                    filteredContainers.remove(container);
                }
            }
        }

        return filteredContainers;
    }

    @Override
    public List<Container> getAllContainers()
    {
        try
        {
            return containerConverter.convert(dockerClient.listContainers(ListContainersParam.allContainers()));
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}