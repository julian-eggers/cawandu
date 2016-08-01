package com.itelg.docker.cawandu.domain.image;

import org.junit.Assert;
import org.junit.Test;

public class ImageTest
{
    @Test
    public void testIsPullableNoImage()
    {
        Image image = new Image();
        image.setName(null);
        Assert.assertFalse(image.isPullable());
    }

    @Test
    public void testIsPullable()
    {
        Image image = new Image();
        image.setName("cawandu");
        Assert.assertTrue(image.isPullable());
    }

    @Test
    public void testEquals()
    {
        Image name1 = new Image();
        name1.setId("1");
        name1.setName("1");

        Image name2 = new Image();
        name2.setId("2");
        name2.setName("2");

        Image name3 = new Image();
        name3.setId("1");
        name3.setName("3");

        Assert.assertEquals(name1, name1);
        Assert.assertNotEquals(name1, name2);
        Assert.assertEquals(name1, name3);
        Assert.assertNotEquals(name2, name3);
    }
}