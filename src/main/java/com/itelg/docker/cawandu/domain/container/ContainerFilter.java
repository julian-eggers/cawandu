package com.itelg.docker.cawandu.domain.container;

import com.itelg.docker.cawandu.domain.filter.AbstractFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContainerFilter extends AbstractFilter
{
    private String id;
    private String name;
    private ContainerState state;
    private String imageName;
}