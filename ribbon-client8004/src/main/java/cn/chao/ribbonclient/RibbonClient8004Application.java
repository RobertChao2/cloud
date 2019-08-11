package cn.chao.ribbonclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RibbonClient8004Application {

    public static void main(String[] args) {
        SpringApplication.run(RibbonClient8004Application.class, args);
    }

}
