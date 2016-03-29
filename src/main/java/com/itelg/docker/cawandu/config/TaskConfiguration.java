package com.itelg.docker.cawandu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.itelg.docker.cawandu.domain.PullMode;

@EnableScheduling
@Configuration
public class TaskConfiguration
{
    @Value("${docker.pullMode}")
    public PullMode pullMode;
}