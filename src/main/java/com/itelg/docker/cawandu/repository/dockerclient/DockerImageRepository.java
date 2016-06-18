package com.itelg.docker.cawandu.repository.dockerclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.domain.image.ImageFilter;
import com.itelg.docker.cawandu.domain.image.UpdateState;
import com.itelg.docker.cawandu.repository.ImageRepository;
import com.itelg.docker.cawandu.repository.dockerclient.converter.ImageConverter;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListImagesFilterParam;

@Repository
public class DockerImageRepository implements ImageRepository
{
    private static final Logger log = LoggerFactory.getLogger(DockerImageRepository.class);

    @Autowired
    private DockerClient dockerClient;

    @Autowired
    private ImageConverter imageConverter;

    @Override
    public UpdateState pullImage(Image image)
    {
        return pullImage(image.getName());
    }

    @Override
    public UpdateState pullImage(String imageName)
    {
        try
        {
            UpdateCheckProgressHandler progressHandler = new UpdateCheckProgressHandler();
            dockerClient.pull(imageName, progressHandler);
            return progressHandler.getState();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return UpdateState.UNKNOWN_ERROR;
    }

    @Override
    public boolean removeImage(Image image)
    {
        try
        {
            return !dockerClient.removeImage(image.getId(), true, false).isEmpty();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return false;
    }

    @Override
    public List<Image> getImagesByFilter(ImageFilter filter)
    {
        List<Image> allImages = getAllImages();
        List<Image> filteredImages = new ArrayList<>(allImages);

        for (Image image : allImages)
        {
            if (StringUtils.isNotBlank(filter.getId()))
            {
                if (!image.getId().contains(filter.getId()))
                {
                    filteredImages.remove(image);
                }
            }

            if (StringUtils.isNotBlank(filter.getName()))
            {
                if (!image.getName().contains(filter.getName()))
                {
                    filteredImages.remove(image);
                }
            }
        }

        return filteredImages;
    }

    @Override
    public List<Image> getAllImages()
    {
        try
        {
            return imageConverter.convert(dockerClient.listImages(ListImagesFilterParam.allImages()));
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}