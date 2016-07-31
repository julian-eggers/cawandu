package com.itelg.docker.cawandu.service;

import java.util.List;

import com.itelg.docker.cawandu.domain.image.Image;
import com.itelg.docker.cawandu.domain.image.ImageFilter;
import com.itelg.docker.cawandu.domain.image.UpdateState;

public interface ImageService
{
    UpdateState pullImage(Image image);

    UpdateState pullImage(String imageName);

    boolean removeImage(Image image);

    List<Image> removedUnusedImages();

    List<Image> getImagesByFilter(ImageFilter filter);

    List<Image> getUsedImages();

    List<Image> getAllImages();
}