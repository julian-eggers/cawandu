package com.itelg.docker.cawandu.domain.container;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Container
{
    public static final String CONTAINER_NAME_PATTERN = "/?[a-zA-Z0-9_-]+";
    
    private String id;
    private ContainerState state;
    private String name;
    private String imageName;
    private String imageId;
    private Map<String, String> labels;
    private LocalDateTime created;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public ContainerState getState()
    {
        return state;
    }

    public void setState(ContainerState state)
    {
        this.state = state;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImageName()
    {
        if (hasLabel("cawandu.image.name"))
        {
            return getLabel("cawandu.image.name");
        }
        
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }
    
    public String getImageTag()
    {
        if (StringUtils.isNotBlank(imageName))
        {
            if (imageName.contains(":"))
            {
                return imageName.split(":")[1];
            }
            
            return "latest";
        }
        
        return null;
    }
    
    public String getImageNameWithoutTag()
    {
        if (StringUtils.isNotBlank(imageName))
        {
            return imageName.split(":")[0];
        }
        
        return null;
    }

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    public Map<String, String> getLabels()
    {
        return labels;
    }

    public void setLabels(Map<String, String> labels)
    {
        this.labels = labels;
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

    public LocalDateTime getCreated()
    {
        return created;
    }

    public void setCreated(LocalDateTime created)
    {
        this.created = created;
    }
    
    public boolean isStartable()
    {
        return (state == ContainerState.CREATED || state == ContainerState.EXITED);
    }
    
    public boolean isStoppable()
    {
        return (state == ContainerState.UP || state == ContainerState.RESTARTING);
    }
    
    public boolean isRestartable()
    {
        return (state == ContainerState.UP);
    }
    
    public boolean isRemovable()
    {
        return (state == ContainerState.CREATED || state == ContainerState.EXITED);
    }
    
    public boolean isKillable()
    {
        return true;
    }
    
    public boolean hasUpdate()
    {
        return imageId.equals(imageName);
    }

    @Override
    public String toString()
    {
        return "Container [id=" + id + ", state=" + state + ", name=" + name + ", imageName=" + imageName + ", imageId=" + imageId + ", labels=" + labels + ", created=" + created + "]";
    }
}