package cn.chao.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServer8009Application {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer8009Application.class, args);
    }

}
