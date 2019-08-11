package cn.chao.ribbonclient.ribbon;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
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
    
    /**
    * 配置随机负载策略，需要配置属性service-B.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
    */
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }
}
