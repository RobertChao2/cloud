package cn.chao.zuulclient.zuul;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class zuulConfig {

    // 将过滤器交给Spring管理
    @Bean
    public TokenFilter tokenFilter(){
        return new TokenFilter();
    }

}
