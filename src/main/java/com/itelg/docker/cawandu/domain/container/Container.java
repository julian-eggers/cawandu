package com.itelg.docker.cawandu.domain.container;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Container
{
    public static final String CONTAINER_NAME_PATTERN = "/?[a-zA-Z0-9_-]+";
    public static final String IMAGE_NAME_LABEL = "cawandu.image.name";

    private String id;
    private ContainerState state;
    private boolean update;
    private String name;
    private String imageName;
    private String imageId;
    private Map<String, String> labels;
    private LocalDateTime created;

    public String getImageName()
    {
        if (hasLabel(IMAGE_NAME_LABEL))
        {
            return getLabel(IMAGE_NAME_LABEL);
        }

        return imageName;
    }

    public boolean isImagePullable()
    {
        return !isSwarmTask() && StringUtils.isNotBlank(getImageName());
    }

    public String getImageTag()
    {
        if (StringUtils.isNotBlank(getImageName()))
        {
            if (getImageName().contains(":"))
            {
                return getImageName().split(":")[1];
            }

            return "latest";
        }

        return null;
    }

    public String getImageNameWithoutTag()
    {
        if (StringUtils.isNotBlank(getImageName()))
        {
            return getImageName().split(":")[0];
        }

        return null;
    }

    public boolean hasLabel(String key)
    {
        if (labels != null)
        {
            return labels.containsKey(key);
        }

        return false;
    }

    public String getLabel(String key)
    {
        if (labels != null)
        {
            return labels.get(key);
        }

        return null;
    }

    public void addLabel(String key, String value)
    {
        if (labels == null)
        {
            labels = new HashMap<>();
        }

        labels.put(key, value);
    }

    public boolean isSwarmTask()
    {
        if (labels != null)
        {
            return labels.keySet().stream().anyMatch(label -> label.contains("com.docker.swarm"));
        }

        return false;
    }

    public boolean isStartable()
    {
        return !isSwarmTask() && (state == ContainerState.CREATED || state == ContainerState.EXITED);
    }

    public boolean isStoppable()
    {
        return !isSwarmTask() && (state == ContainerState.UP || state == ContainerState.RESTARTING);
    }

    public boolean isRestartable()
    {
        return !isSwarmTask() && (state == ContainerState.UP);
    }

    public boolean isRemovable()
    {
        return (state == ContainerState.CREATED || state == ContainerState.EXITED);
    }

    public boolean isKillable()
    {
        return !isSwarmTask();
    }

    public boolean hasUpdate()
    {
        return !isSwarmTask() && update && getImageName() != null;
    }

    public boolean isTagSwitchable()
    {
        return !isSwarmTask() && getImageName() != null;
    }
}