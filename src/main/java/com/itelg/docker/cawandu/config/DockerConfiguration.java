package com.itelg.docker.cawandu.config;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerCertificates;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.AuthConfig;

@Configuration
public class DockerConfiguration
{
    private String dockerRegistryUsername;
    private String dockerRegistryEmail;
    private String dockerRegistryPassword;
    private String dockerHostUri;
    private String dockerHostCertificatesDirectory;
    
    @Autowired
    private Environment env;
    
    @PostConstruct
    public void postConstruct()
    {
        dockerRegistryUsername = env.getProperty("docker.registry.username").replace("null", "");
        dockerRegistryEmail = env.getProperty("docker.registry.email").replace("null", "");
        dockerRegistryPassword = env.getProperty("docker.registry.password").replace("null", "");
        dockerHostUri = env.getProperty("docker.host.uri").replace("null", "");
        dockerHostCertificatesDirectory = env.getProperty("docker.host.certificates.directory").replace("null", "");
    }

    @Bean
    public DockerClient dockerClient() throws DockerCertificateException
    {
        DefaultDockerClient.Builder dockerClientBuilder = getDockerClientBuilder();
        dockerClientBuilder.authConfig(getAuthConfig());
        return dockerClientBuilder.build();
    }
    
    private DefaultDockerClient.Builder getDockerClientBuilder() throws DockerCertificateException
    {
        if (isNotBlank(dockerHostUri) && isNotBlank(dockerHostCertificatesDirectory))
        {
            DefaultDockerClient.Builder dockerClientBuilder = DefaultDockerClient.builder();
            dockerClientBuilder.uri(dockerHostUri);
            dockerClientBuilder.dockerCertificates(new DockerCertificates(Paths.get(dockerHostCertificatesDirectory.trim())));
            return dockerClientBuilder;
        }
        else if (Files.exists(Paths.get("/var/run/docker.sock")))
        {
            return DefaultDockerClient.fromEnv();
        }
        else
        {
            throw new RuntimeException("Invalid host-configuration! Either mount \"docker.sock\" or provide \"HOST_URI\" and \"HOST_CERTIFICATES\"!");
        }
    }
    
    private AuthConfig getAuthConfig()
    {
        if (isNotBlank(dockerRegistryUsername) && isNotBlank(dockerRegistryEmail) && isNotBlank(dockerRegistryPassword))
        {
            AuthConfig.Builder authConfigBuilder = AuthConfig.builder();
            authConfigBuilder.username(dockerRegistryUsername);
            authConfigBuilder.email(dockerRegistryEmail);
            authConfigBuilder.password(dockerRegistryPassword);
            return authConfigBuilder.build(); 
        }
        
        return null;
    }
}