package cn.chao.hystrixclient.ribbon;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RibbonConfig {

    /**
     * 实例化 ribbon 使用的 RestTemplate
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate rebbionRestTemplate(){
        return new RestTemplate();
    }

}