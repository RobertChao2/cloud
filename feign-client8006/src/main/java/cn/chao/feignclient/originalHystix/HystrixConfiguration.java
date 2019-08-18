package cn.chao.feignclient.originalHystix;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    需要声明一个切面，HystrixConfiguration,接着，将HystrixConfiguration加入到spring管理
 */
@Configuration
public class HystrixConfiguration {

    @Bean
    public HystrixCommandAspect hystrixAspect(){
        return  new HystrixCommandAspect();
    }

}