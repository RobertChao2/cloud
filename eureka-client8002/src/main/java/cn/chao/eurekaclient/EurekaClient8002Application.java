package cn.chao.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class EurekaClient8002Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClient8002Application.class, args);
    }

}
