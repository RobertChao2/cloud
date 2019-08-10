package cn.chao.eurekaconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumer8003Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumer8003Application.class, args);
    }

}
