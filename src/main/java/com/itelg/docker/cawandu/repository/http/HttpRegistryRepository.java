package com.itelg.docker.cawandu.repository.http;

import static org.springframework.util.Base64Utils.encodeToString;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.itelg.docker.cawandu.repository.RegistryRepository;
import com.itelg.docker.cawandu.repository.http.parser.RegistryAuthTokenParser;
import com.itelg.docker.cawandu.repository.http.parser.RegistryImageTagListParser;

@Repository
public class HttpRegistryRepository implements RegistryRepository
{
    private static final Logger log = LoggerFactory.getLogger(HttpRegistryRepository.class);
    private PoolingHttpClientConnectionManager connectionManager;
    private HttpClient httpClient;
    private static final int MAX_CONNECTION_IDLE_TIME = 20;

    @Value("${docker.registry.auth.url}")
    private String registryAuthUrl;
    
    @Value("${docker.registry.auth.service}")
    private String registryAuthService;
    
    @Value("${docker.registry.index.url}")
    private String registryIndexUrl;
    
    @Value("${docker.registry.username}") 
    private String registryUsername;

    @Value("${docker.registry.password}") 
    private String registryPassword;

    @Autowired 
    private RegistryAuthTokenParser authTokenParser;

    @Autowired 
    private RegistryImageTagListParser imageTagListParser;

    @PostConstruct
    public void init()
    {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setSocketTimeout(5000);
        requestConfigBuilder.setConnectTimeout(5000);
        requestConfigBuilder.setConnectionRequestTimeout(5000);
        httpClientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());

        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(2);
        connectionManager.setMaxTotal(2);
        httpClientBuilder.setConnectionManager(connectionManager);

        httpClient = httpClientBuilder.build();
    }

    private String getToken(String imageName)
    {
        String url = registryAuthUrl + "token?service=" + registryAuthService + "&scope=repository:" + imageName + ":pull";
        HttpEntity entity = null;

        try
        {
            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization", "Basic " + encodeToString(String.valueOf(registryUsername + ":" + registryPassword).getBytes()));
            HttpResponse response = httpClient.execute(request);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, Charset.forName("UTF-8"));

            if (response.getStatusLine().getStatusCode() == 200 && content.contains("token"))
            {
                return authTokenParser.convert(content);
            }
        }
        catch (ConnectException | SocketTimeoutException | ConnectTimeoutException | UnknownHostException e)
        {
            log.warn("Request-error: " + url + " -- " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            connectionManager.closeIdleConnections(MAX_CONNECTION_IDLE_TIME, TimeUnit.SECONDS);
            connectionManager.closeExpiredConnections();
            EntityUtils.consumeQuietly(entity);
        }

        return null;
    }

    @Override
    public List<String> getImageTagsByName(String imageName)
    {
        String url = registryIndexUrl + imageName + "/tags/list";
        HttpEntity entity = null;

        try
        {
            HttpGet request = new HttpGet(url);
            request.addHeader("Authorization", "Bearer " + getToken(imageName));
            HttpResponse response = httpClient.execute(request);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, Charset.forName("UTF-8"));

            if (response.getStatusLine().getStatusCode() == 200 && content.contains("tags"))
            {
                return imageTagListParser.convert(content);
            }
        }
        catch (ConnectException | SocketTimeoutException | ConnectTimeoutException | UnknownHostException e)
        {
            log.warn("Request-error: " + url + " -- " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            connectionManager.closeIdleConnections(MAX_CONNECTION_IDLE_TIME, TimeUnit.SECONDS);
            connectionManager.closeExpiredConnections();
            EntityUtils.consumeQuietly(entity);
        }

        return null;
    }
}