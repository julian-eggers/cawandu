package com.itelg.docker.cawandu.repository.dockerclient;

import com.itelg.docker.cawandu.domain.image.UpdateState;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.ProgressHandler;
import com.spotify.docker.client.messages.ProgressMessage;

public class UpdateCheckProgressHandler implements ProgressHandler
{
    private UpdateState state = UpdateState.UNKNOWN_ERROR;

    @Override
    public void progress(ProgressMessage message) throws DockerException
    {
        if (message.status() != null)
        {
            if (message.status().contains("Downloaded newer image for"))
            {
                state = UpdateState.PULLED;
            }
            else if (message.status().contains("Image is up to date")) 
            {
                state = UpdateState.NO_UPDATE;
            }
        }
        
        if (message.error() != null)
        {
            if (message.error().contains("not found")) 
            {
                state = UpdateState.NO_ACCESS;
            }
        }
    }
    
    public UpdateState getState()
    {
        return state;
    }
}