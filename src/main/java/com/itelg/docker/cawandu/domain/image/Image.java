package com.itelg.docker.cawandu.domain.image;

import java.time.LocalDateTime;

public class Image
{
    private String id;
    private String name;
    private long size;
    private LocalDateTime created;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public LocalDateTime getCreated()
    {
        return created;
    }

    public void setCreated(LocalDateTime created)
    {
        this.created = created;
    }

    public boolean isPullable()
    {
        return !"<none>:<none>".equals(name);
    }

    @Override
    public String toString()
    {
        return "Image [id=" + id + ", name=" + name + ", size=" + size + ", created=" + created + "]";
    }
}