package com.itelg.docker.cawandu.domain.container;

public enum ContainerState
{
    CREATED("#FFFFFF"),
    UP("#00CC00"),
    PAUSED("orange"),
    EXITED("#FFFFFF"),
    RESTARTING("#FF0000"),
    REMOVAL_IN_PROGRESS("#FF0000");

    private String color;

    private ContainerState(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }

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
        else if ("Created".equals(value))
        {
            return CREATED;
        }
        else if (value.startsWith("Restarting"))
        {
            return RESTARTING;
        }
        else if ("Removal In Progress".equals(value))
        {
            return ContainerState.REMOVAL_IN_PROGRESS;
        }

        throw new RuntimeException("Unknown state (" + value + ")");
    }
}