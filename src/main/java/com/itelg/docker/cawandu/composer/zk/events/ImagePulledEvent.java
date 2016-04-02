package com.itelg.docker.cawandu.composer.zk.events;

import org.zkoss.zk.ui.event.Event;

import com.itelg.docker.cawandu.domain.image.Image;

public class ImagePulledEvent extends Event
{
    private static final long serialVersionUID = 4285674813486334463L;

    public ImagePulledEvent(Image image)
    {
        super(ImagePulledEvent.class.getName(), null, image);
    }
}