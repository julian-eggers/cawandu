package com.itelg.docker.cawandu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.cawandu.domain.container.Container;
import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.domain.image.ImageFilter;
import com.itelg.docker.cawandu.domain.image.UpdateState;
import com.itelg.docker.cawandu.repository.ImageRepository;
import com.itelg.docker.cawandu.service.ContainerService;
import com.itelg.docker.cawandu.service.ImageService;

@Service
public class DefaultImageService implements ImageService
{
    private static final Logger log = LoggerFactory.getLogger(DefaultImageService.class);
    
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ContainerService containerService;
    
    @Override
    public UpdateState pullImage(Image image)
    {
        UpdateState state = imageRepository.pullImage(image);
        log.info("Image pulled - " + state + " - (" + image + ")");
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
                if (image.getId().equals(container.getImageId()) ||
                    image.getName().equals(container.getImageName()))
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
    public List<Image> getAllImages()
    {
        return imageRepository.getAllImages();
    }
}