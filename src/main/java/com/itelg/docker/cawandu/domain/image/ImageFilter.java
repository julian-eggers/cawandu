package com.itelg.docker.cawandu.domain.image;

import com.itelg.docker.cawandu.domain.filter.AbstractFilter;

public class ImageFilter extends AbstractFilter
{
    private String id;
    private String name;

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
}