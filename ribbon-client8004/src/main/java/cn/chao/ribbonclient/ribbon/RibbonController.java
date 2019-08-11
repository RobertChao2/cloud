package cn.chao.ribbonclient.ribbon;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Log4j2
public class RibbonController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/{applicationName}/ribbonService")
    public String ribbonService(@PathVariable("applicationName") String applicationName){
        // getForObject 的请求，返回是一个字符串
        return restTemplate.getForObject("http://" + applicationName+"/ribbonService", String.class);
    }

    @GetMapping("/log-instance")
    public void logInstance(){
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("eureka-client");
        // 打印日志：服务名，主机地址，端口号
        log.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
    }
}
