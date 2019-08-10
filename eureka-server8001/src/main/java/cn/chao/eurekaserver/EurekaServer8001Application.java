package cn.chao.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer     // 加入注解声明是 Eureka 服务注册中心。
@SpringBootApplication
public class EurekaServer8001Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer8001Application.class, args);
    }

}
