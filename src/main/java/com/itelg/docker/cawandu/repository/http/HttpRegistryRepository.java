package com.itelg.docker.cawandu.repository.http;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.itelg.docker.cawandu.repository.RegistryRepository;
import com.itelg.docker.cawandu.repository.http.parser.ImageTagListParser;

// TODO refactor!
@Repository
public class HttpRegistryRepository implements RegistryRepository
{
    private static final Logger log = LoggerFactory.getLogger(HttpRegistryRepository.class);
    private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    private HttpClient httpClient;
    private static final int MAX_CONNECTION_IDLE_TIME = 20;
    
    @Value("${docker.registry.username}")
    private String registryUsername;
    
    @Value("${docker.registry.password}")
    private String registryPassword;
    
    @Autowired
    private ImageTagListParser imageTagListParser;

    @PostConstruct
    public void init()
    {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setSocketTimeout(5000);
        requestConfigBuilder.setConnectTimeout(5000);
        requestConfigBuilder.setConnectionRequestTimeout(5000);
        RequestConfig requestConfig = requestConfigBuilder.build();

        connectionManager.setDefaultMaxPerRoute(2);
        connectionManager.setMaxTotal(2);

        Credentials usernamePasswordCredentials = new UsernamePasswordCredentials(registryUsername, registryPassword);
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), usernamePasswordCredentials);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClient = httpClientBuilder.build();
    }
    
    @Override
    public List<String> getImageTagsByName(String imageName)
    {
        String url = "https://index.docker.io/v1/repositories/" + imageName + "/tags";
        HttpEntity entity = null;

        try
        {
            AuthCache authCache = new BasicAuthCache();
            AuthScheme basicAuth = new BasicScheme();
            authCache.put(new HttpHost("index.docker.io"), basicAuth);
            HttpClientContext httpClientContext = HttpClientContext.create();
            httpClientContext.setAuthCache(authCache);

            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request, httpClientContext);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, Charset.forName("UTF-8"));
            return imageTagListParser.convert(content);
        }
        catch (ConnectException | SocketTimeoutException | ConnectTimeoutException | UnknownHostException e)
        {
            log.warn("Timeout: " + url + " -- " + e.getMessage());
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