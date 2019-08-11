package cn.chao.feignclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication
@SpringCloudApplication
@EnableFeignClients
public class FeignClient8006Application {

    public static void main(String[] args) {
        SpringApplication.run(FeignClient8006Application.class, args);
    }

}
