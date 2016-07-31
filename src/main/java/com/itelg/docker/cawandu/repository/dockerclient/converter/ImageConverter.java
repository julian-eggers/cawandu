package com.itelg.docker.cawandu.repository.dockerclient.converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.spotify.docker.client.messages.Image;

@Component
public class ImageConverter implements Converter<Image, com.itelg.docker.cawandu.domain.image.Image>
{
    @Override
    public com.itelg.docker.cawandu.domain.image.Image convert(Image clientImage)
    {
        com.itelg.docker.cawandu.domain.image.Image image = new com.itelg.docker.cawandu.domain.image.Image();
        image.setId(clientImage.id());
        image.setName(getImageName(clientImage));
        image.setSize(clientImage.size().longValue() / 1000 / 1000);
        image.setCreated(LocalDateTime.ofEpochSecond(Long.parseLong(clientImage.created()), 0, ZoneOffset.UTC));
        return image;
    }

    public List<com.itelg.docker.cawandu.domain.image.Image> convert(List<Image> clientImages)
    {
        List<com.itelg.docker.cawandu.domain.image.Image> images = new ArrayList<>();

        for (Image image : clientImages)
        {
            images.add(convert(image));
        }

        return images;
    }

    private String getImageName(Image clientImage)
    {
        if (clientImage.repoTags() != null)
        {
            String imageName = clientImage.repoTags().get(0);

            if (!"<none>:<none>".equals(imageName))
            {
                return imageName;
            }
        }

        return null;
    }
}