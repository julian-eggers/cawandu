package com.itelg.docker.cawandu.domain.container;

public enum ContainerState
{
    UP, EXITED, CREATED, RESTARTING;

    public static ContainerState fromString(String value)
    {
        if (value.startsWith("Up"))
        {
            return ContainerState.UP;
        }
        else if (value.startsWith("Exited"))
        {
            return ContainerState.EXITED;
        }
        else if (value.equals("Created")) 
        {
            return ContainerState.CREATED;
        }
        else if (value.startsWith("Restarting")) 
        {
            return ContainerState.RESTARTING;
        }

        throw new RuntimeException("Unknown state (" + value + ")");
    }
}