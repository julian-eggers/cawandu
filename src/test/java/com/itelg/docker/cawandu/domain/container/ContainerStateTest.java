package com.itelg.docker.cawandu.domain.container;

import org.junit.Assert;
import org.junit.Test;

public class ContainerStateTest
{
    @Test
    public void testFromString()
    {
        Assert.assertEquals(ContainerState.UP, ContainerState.fromString("Up"));
    }
    
    @Test
    public void testFromStringExited()
    {
        Assert.assertEquals(ContainerState.EXITED, ContainerState.fromString("Exited (137) 7 minutes ago"));
    }
    
    @Test
    public void testFromStringCreated()
    {
        Assert.assertEquals(ContainerState.CREATED, ContainerState.fromString("Created"));
    }
    
    @Test
    public void testFromStringRestarting()
    {
        Assert.assertEquals(ContainerState.RESTARTING, ContainerState.fromString("Restarting"));
    }
    
    @Test
    public void testFromStringPaused()
    {
        Assert.assertEquals(ContainerState.PAUSED, ContainerState.fromString("Up 21 hours (Paused)"));
    }
    
    @Test(expected = RuntimeException.class)
    public void testFromStringUnknownState()
    {
        ContainerState.fromString("Unknown");
    }
}