package com.itelg.docker.cawandu.service;

import java.util.List;

public interface RegistryService
{
    List<String> getImageTagsByName(String imageName);
}