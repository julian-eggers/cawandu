package com.itelg.docker.cawandu.repository.http.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ImageTagListParserTest
{
    @Test
    public void testParse() throws IOException
    {
        String json = IOUtils.toString(new ClassPathResource("imageTags.json").getInputStream());
        List<String> tags = new ImageTagListParser().convert(json);
        Assert.assertNotNull(tags);
        Assert.assertEquals(7, tags.size());
        Assert.assertEquals("latest", tags.get(0));
        Assert.assertEquals("1.4.2-RELEASE", tags.get(1));
        Assert.assertEquals("1.4.1-RELEASE", tags.get(2));
        Assert.assertEquals("1.4.0-RELEASE", tags.get(3));
        Assert.assertEquals("1.3.5-RELEASE", tags.get(4));
        Assert.assertEquals("1.3.4-RELEASE", tags.get(5));
        Assert.assertEquals("1.2.1-RELEASE", tags.get(6));
    }
}