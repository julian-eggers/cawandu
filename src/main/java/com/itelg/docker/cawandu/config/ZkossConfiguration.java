package com.itelg.docker.cawandu.config;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;

@Configuration
public class ZkossConfiguration extends WebMvcConfigurerAdapter
{
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("").setViewName("forward:/index.zul");
    }

    @Bean
    public HttpSessionListener zkossSessionListener()
    {
        return new HttpSessionListener();
    }

    @Bean
    public ServletRegistrationBean zkLoader()
    {
        ServletRegistrationBean zkLoader = new ServletRegistrationBean(new DHtmlLayoutServlet(), "*.zul", "*.zhtml");
        zkLoader.getInitParameters().put("update-uri", "/zkau");
        return zkLoader;
    }

    @Bean
    public ServletRegistrationBean auEngine()
    {
        return new ServletRegistrationBean(new DHtmlUpdateServlet(), "/zkau/*");
    }
}