package com.itelg.docker.cawandu.domain.image;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Image
{
    private String id;
    private String name;
    private long size;
    private LocalDateTime created;

    public boolean isPullable()
    {
        return name != null && !name.contains("@");
    }
}