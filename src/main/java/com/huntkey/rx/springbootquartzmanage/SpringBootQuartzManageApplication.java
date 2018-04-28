package com.huntkey.rx.springbootquartzmanage;

import com.huntkey.rx.sceo.method.register.plugin.annotation.EnableDriverMethod;
import com.huntkey.rx.sceo.method.register.plugin.annotation.EnableMethodRegisterScanner;
import com.huntkey.rx.sceo.orm.config.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@Import(DynamicDataSourceRegister.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.huntkey.rx.springbootquartzmanage.client")
@SpringBootApplication(scanBasePackages = {"com.huntkey.rx.edm.service", "com.huntkey.rx.springbootquartzmanage"})
@EnableMethodRegisterScanner(startApplicationClass = SpringBootQuartzManageApplication.class,
        edmServiceName = "${edmServiceName:modeler-provider}",
        serviceApplicationName = "${spring.application.name}")
@EnableDriverMethod
public class SpringBootQuartzManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootQuartzManageApplication.class, args);
    }
}
