package com.itelg.docker.cawandu.repository.dockerclient.converter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.itelg.docker.cawandu.domain.container.ContainerState;
import com.spotify.docker.client.messages.Container;

public class ContainerConverterTest
{
    @Test
    public void testConvert()
    {
        Container clientContainer = new Container();
        Whitebox.setInternalState(clientContainer, "id", "123");
        Whitebox.setInternalState(clientContainer, "status", "Up");
        Whitebox.setInternalState(clientContainer, "names", ImmutableList.of("/test"));
        Whitebox.setInternalState(clientContainer, "image", "jeggers/cawandu:latest");
        Whitebox.setInternalState(clientContainer, "imageId", "123456789");
        Whitebox.setInternalState(clientContainer, "labels", ImmutableMap.of("key", "value"));
        Whitebox.setInternalState(clientContainer, "created", Long.valueOf(1459512451));

        com.itelg.docker.cawandu.domain.container.Container container = new ContainerConverter().convert(clientContainer);
        Assert.assertEquals("123", container.getId());
        Assert.assertEquals(ContainerState.UP, container.getState());
        Assert.assertEquals("test", container.getName());
        Assert.assertEquals("jeggers/cawandu:latest", container.getImageName());
        Assert.assertEquals("123456789", container.getImageId());
        Assert.assertEquals(Collections.singletonMap("key", "value"), container.getLabels());
        Assert.assertEquals(HashMap.class, container.getLabels().getClass());
        Assert.assertEquals(LocalDateTime.of(2016, 4, 1, 12, 7, 31), container.getCreated());
    }
}