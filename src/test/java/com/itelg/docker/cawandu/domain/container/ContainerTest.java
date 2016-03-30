package com.itelg.docker.cawandu.domain.container;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

public class ContainerTest
{
    @Test
    public void testGetImageName()
    {
        Container container = new Container();
        container.setImageName("jeggers/test:latest");
        Assert.assertEquals("jeggers/test:latest", container.getImageName());
    }
    
    @Test
    public void testGetImageNameByLabel()
    {
        Container container = new Container();
        container.setImageName("jeggers/test:latest");
        container.setLabels(Collections.singletonMap("cawandu.image.name", "jeggers/test123:latest"));
        Assert.assertEquals("jeggers/test123:latest", container.getImageName());
    }
    
    @Test
    public void testGetImageTag()
    {
        Container container = new Container();
        container.setImageName("jeggers/test:current");
        Assert.assertEquals("current", container.getImageTag());
    }
    
    @Test
    public void testGetImageTagWithoutTag()
    {
        Container container = new Container();
        container.setImageName("jeggers/test");
        Assert.assertEquals("latest", container.getImageTag());
    }
    
    @Test
    public void testGetImageTagEmpty()
    {
        Container container = new Container();
        Assert.assertNull(container.getImageTag());
    }
    
    @Test
    public void testGetImageNameWithoutTag()
    {
        Container container = new Container();
        container.setImageName("jeggers/test:current");
        Assert.assertEquals("jeggers/test", container.getImageNameWithoutTag());
    }
    
    @Test
    public void testGetImageNameWithoutTagWithoutTag()
    {
        Container container = new Container();
        container.setImageName("jeggers/test");
        Assert.assertEquals("jeggers/test", container.getImageNameWithoutTag());
    }
    
    @Test
    public void testGetImageNameWithoutTagMissing()
    {
        Container container = new Container();
        Assert.assertNull(container.getImageNameWithoutTag());
    }
    
    @Test
    public void testHasLabel()
    {
        Container container = new Container();
        Assert.assertFalse(container.hasLabel("key"));
        container.setLabels(Collections.singletonMap("key", "value"));
        Assert.assertTrue(container.hasLabel("key"));
        Assert.assertFalse(container.hasLabel("notfound"));
    }
    
    @Test
    public void testGetLabel()
    {
        Container container = new Container();
        Assert.assertNull(container.getLabel("key"));
        container.setLabels(Collections.singletonMap("key", "value"));
        Assert.assertEquals("value", container.getLabel("key"));
    }
    
    @Test
    public void testToString()
    {
        Assert.assertTrue(new Container().toString().startsWith("Container"));
    }
}