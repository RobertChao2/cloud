package cn.chao.hystrixclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//@EnableEurekaClient
//@EnableCircuitBreaker   // 开启熔断器功能。
//@SpringBootApplication
@SpringCloudApplication     // 使用 @SpringCloudApplication 包含了 eureka 客户端、ribbon、以及 hystrix
public class Hystrixclient8005Application {

    public static void main(String[] args) {
        SpringApplication.run(Hystrixclient8005Application.class, args);
    }

}
