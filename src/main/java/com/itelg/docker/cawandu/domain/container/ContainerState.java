package com.itelg.docker.cawandu.domain.container;

public enum ContainerState
{
    UP, EXITED, CREATED, RESTARTING, PAUSED;

    public static ContainerState fromString(String value)
    {
        if (value.contains("Paused"))
        {
            return PAUSED;
        }
        else if (value.startsWith("Up"))
        {
            return UP;
        }
        else if (value.startsWith("Exited"))
        {
            return EXITED;
        }
        else if (value.equals("Created")) 
        {
            return CREATED;
        }
        else if (value.startsWith("Restarting")) 
        {
            return RESTARTING;
        }

        throw new RuntimeException("Unknown state (" + value + ")");
    }
}