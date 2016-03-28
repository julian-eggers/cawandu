package com.itelg.docker.cawandu.composer.zk;

import org.zkoss.zk.ui.event.MouseEvent;

public class Position
{
    private int left;
    private int top;

    public Position(int left, int top)
    {
        this.left = left;
        this.top = top;
    }

    public Position(MouseEvent mouseEvent)
    {
        left = mouseEvent.getPageX() + 10;
        top = mouseEvent.getPageY() + 10;
    }

    public String getLeft()
    {
        return left + "px";
    }

    public String getTop()
    {
        return top + "px";
    }
}