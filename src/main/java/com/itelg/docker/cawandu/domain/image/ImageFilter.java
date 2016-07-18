package com.itelg.docker.cawandu.domain.image;

import com.itelg.docker.cawandu.domain.filter.AbstractFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImageFilter extends AbstractFilter
{
    private String id;
    private String name;
}