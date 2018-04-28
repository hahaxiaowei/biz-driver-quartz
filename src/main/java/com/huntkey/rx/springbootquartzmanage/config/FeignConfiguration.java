package com.huntkey.rx.springbootquartzmanage.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置
 *
 * @author zhangyu
 */
@Configuration
public class FeignConfiguration {

    @Bean
    Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(1 * 1000, 10 * 1000);
    }

}
