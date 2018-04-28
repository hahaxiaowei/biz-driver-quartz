package com.huntkey.rx.springbootquartzmanage.config;

import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.ServerListUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhaomj
 */
@Configuration
public class TxConfig {
    @Autowired
    private RestTemplateBuilder builder;


    @Bean(name = "remoteRestTemplate")
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ServerListUpdater serverListUpdater() {
        return new PollingServerListUpdater();
    }
}
