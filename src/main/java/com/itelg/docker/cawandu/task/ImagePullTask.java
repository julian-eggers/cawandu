package com.itelg.docker.cawandu.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itelg.docker.cawandu.domain.PullMode;
import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.service.ImageService;

@Component
@ManagedResource
public class ImagePullTask
{
    private static final Logger log = LoggerFactory.getLogger(ImagePullTask.class);

    @Value("${docker.pullMode}")
    private PullMode pullMode;

    @Autowired
    private ImageService imageService;

    @ManagedOperation
    @Scheduled(fixedDelay = 60000)
    public void run()
    {
        // Check if task is enabled
        if (pullMode == PullMode.NONE)
        {
            return;
        }

        log.info("ImagePullTask started");

        for (Image image : imageService.getAllImages())
        {
            if (image.isPullable())
            {
                imageService.pullImage(image);
            }
        }

        log.info("ImagePullTask finished");
    }
}