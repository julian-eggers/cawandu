package com.itelg.docker.cawandu.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;

import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@ManagedResource
@Slf4j
public class ImagePullTask
{
    @Autowired
    private ImageService imageService;

    @ManagedOperation
    @Scheduled(fixedDelayString = "${task.pullImages.fixedDelay}")
    public void run()
    {
        log.info("ImagePullTask started");

        for (Image image : imageService.getUsedImages())
        {
            imageService.pullImage(image);
        }

        log.info("ImagePullTask finished");
    }
}