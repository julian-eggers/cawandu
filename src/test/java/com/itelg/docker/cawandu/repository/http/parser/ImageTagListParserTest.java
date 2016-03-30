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
        Assert.assertEquals(6, tags.size());
    }
}