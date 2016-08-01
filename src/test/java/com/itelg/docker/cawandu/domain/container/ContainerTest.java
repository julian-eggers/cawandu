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
    public void testAddLabel()
    {
        Container container = new Container();
        Assert.assertNull(container.getLabels());
        container.addLabel(Container.IMAGE_NAME_LABEL, "jeggers/cawandu:latest");
        container.addLabel(Container.IMAGE_NAME_LABEL, "jeggers/cawandu:latest");
        Assert.assertNotNull(container.getLabels());
        Assert.assertEquals(1, container.getLabels().size());
    }

    @Test
    public void testIsStartableStatusCreated()
    {
        Container container = new Container();
        container.setState(ContainerState.CREATED);
        Assert.assertTrue(container.isStartable());
    }

    @Test
    public void testIsStartableStatusExited()
    {
        Container container = new Container();
        container.setState(ContainerState.EXITED);
        Assert.assertTrue(container.isStartable());
    }

    @Test
    public void testIsStartableNot()
    {
        Container container = new Container();
        container.setState(ContainerState.PAUSED);
        Assert.assertFalse(container.isStartable());
    }

    @Test
    public void testIsStoppableStatusUp()
    {
        Container container = new Container();
        container.setState(ContainerState.UP);
        Assert.assertTrue(container.isStoppable());
    }

    @Test
    public void testIsStoppableStatusRestarting()
    {
        Container container = new Container();
        container.setState(ContainerState.RESTARTING);
        Assert.assertTrue(container.isStoppable());
    }

    @Test
    public void testIsStoppableNot()
    {
        Container container = new Container();
        container.setState(ContainerState.PAUSED);
        Assert.assertFalse(container.isStoppable());
    }

    @Test
    public void testIsRestartableStatusRestartable()
    {
        Container container = new Container();
        container.setState(ContainerState.UP);
        Assert.assertTrue(container.isRestartable());
    }

    @Test
    public void testIsRestartableNot()
    {
        Container container = new Container();
        container.setState(ContainerState.PAUSED);
        Assert.assertFalse(container.isRestartable());
    }

    @Test
    public void testIsRemovableStatusCreated()
    {
        Container container = new Container();
        container.setState(ContainerState.CREATED);
        Assert.assertTrue(container.isRemovable());
    }

    @Test
    public void testIsRemovableStatusExited()
    {
        Container container = new Container();
        container.setState(ContainerState.EXITED);
        Assert.assertTrue(container.isRemovable());
    }

    @Test
    public void testIsRemovableNot()
    {
        Container container = new Container();
        container.setState(ContainerState.PAUSED);
        Assert.assertFalse(container.isRemovable());
    }

    @Test
    public void testIsKillableNot()
    {
        Container container = new Container();
        Assert.assertTrue(container.isKillable());
    }

    @Test
    public void testHasUpdateNoUpdateAndNoImage()
    {
        Container container = new Container();
        container.setUpdate(false);
        container.setImageName(null);
        Assert.assertFalse(container.hasUpdate());
    }

    @Test
    public void testHasUpdateUpdateAndNoImage()
    {
        Container container = new Container();
        container.setUpdate(true);
        container.setImageName(null);
        Assert.assertFalse(container.hasUpdate());
    }

    @Test
    public void testHasUpdateNoUpdateAndImage()
    {
        Container container = new Container();
        container.setUpdate(false);
        container.setImageName("cawandu");
        Assert.assertFalse(container.hasUpdate());
    }

    @Test
    public void testHasUpdateUpdateAndImage()
    {
        Container container = new Container();
        container.setUpdate(true);
        container.setImageName("cawandu");
        Assert.assertTrue(container.hasUpdate());
    }

    @Test
    public void testIsTagSwitchableNoImage()
    {
        Container container = new Container();
        container.setImageName(null);
        Assert.assertFalse(container.isTagSwitchable());
    }

    @Test
    public void testIsTagSwitchable()
    {
        Container container = new Container();
        container.setImageName("cawandu");
        Assert.assertTrue(container.isTagSwitchable());
    }

    @Test
    public void testEquals()
    {
        Container container1 = new Container();
        container1.setId("1");
        container1.setName("1");

        Container container2 = new Container();
        container2.setId("2");
        container2.setName("2");

        Container container3 = new Container();
        container3.setId("1");
        container3.setName("3");

        Assert.assertEquals(container1, container1);
        Assert.assertNotEquals(container1, container2);
        Assert.assertEquals(container1, container3);
        Assert.assertNotEquals(container2, container3);
    }
}