package com.itelg.docker.cawandu.repository;

import java.util.List;

public interface RegistryRepository
{
    List<String> getImageTagsByName(String imageName);
}