package com.itelg.docker.cawandu.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.domain.image.ImageFilter;
import com.itelg.docker.cawandu.domain.image.UpdateState;
import com.itelg.docker.cawandu.repository.ImageRepository;
import com.itelg.docker.cawandu.service.ContainerService;
import com.itelg.docker.cawandu.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultImageService implements ImageService
{
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private CounterService counterService;

    @Override
    public UpdateState pullImage(Image image)
    {
        UpdateState state = imageRepository.pullImage(image);
        log.info("Image pulled - " + state + " - (" + image + ")");
        return state;
    }

    @Override
    public UpdateState pullImage(String imageName)
    {
        UpdateState state = imageRepository.pullImage(imageName);
        log.info("Image pulled - " + state + " - (" + imageName + ")");
        counterService.increment("images.pulled");
        return state;
    }

    @Override
    public boolean removeImage(Image image)
    {
        boolean successful = imageRepository.removeImage(image);

        if (successful)
        {
            log.info("Image removed (" + image + ")");
        }

        return successful;
    }

    @Override
    public List<Image> removedUnusedImages()
    {
        List<Container> allContainers = containerService.getAllContainers();
        List<Image> allImages = getAllImages();
        List<Image> removedImages = new ArrayList<>();

        for (Image image : allImages)
        {
            boolean inUse = false;

            for (Container container : allContainers)
            {
                if (image.getId().equals(container.getImageId()))
                {
                    inUse = true;
                    break;
                }
            }

            if (!inUse)
            {
                removedImages.add(image);
                removeImage(image);
            }
        }

        return removedImages;
    }

    @Override
    public List<Image> getImagesByFilter(ImageFilter filter)
    {
        return imageRepository.getImagesByFilter(filter);
    }

    @Override
    public List<Image> getUsedImages()
    {
        List<Container> allContainers = containerService.getAllContainers();
        List<Image> allImages = getAllImages();
        Set<Image> usedImages = new HashSet<>();

        for (Image image : allImages)
        {
            for (Container container : allContainers)
            {
                if (!container.isSwarmTask())
                {
                    if (image.getName() != null && StringUtils.equals(image.getName(), container.getImageName()))
                    {
                        usedImages.add(image);
                    }
                    else if (container.getImageName() != null && image.getId().equals(container.getImageId()))
                    {
                        image.setName(container.getImageName());
                        usedImages.add(image);
                    }
                }
            }
        }

        return new ArrayList<>(usedImages);
    }

    @Override
    public List<Image> getAllImages()
    {
        return imageRepository.getAllImages();
    }
}