package com.itelg.docker.cawandu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itelg.docker.cawandu.repository.RegistryRepository;
import com.itelg.docker.cawandu.service.RegistryService;

@Service
public class DefaultRegistryService implements RegistryService
{
    @Autowired
    private RegistryRepository registryRepository;

    @Override
    public List<String> getImageTagsByName(String imageName)
    {
        return registryRepository.getImageTagsByName(imageName);
    }
}