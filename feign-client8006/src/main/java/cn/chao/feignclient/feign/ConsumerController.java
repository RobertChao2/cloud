package cn.chao.feignclient.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    FeignService feignService;

    @RequestMapping("/consumer")
    public String helloConsumer(){
        return feignService.hello();
    }

    @RequestMapping("/consumer2")
    public String helloConsumer2(){
        String r1 = feignService.hello("feign-client");     // 简单的请求参数
        String r2 = feignService.hello("feign-client", 18).toString();  // 请求头的内容
        String r3 = feignService.hello(new User("feign-client", 18));   // JSON 请求字符串
        return r1 + "====" + r2 + "===" + r3;
    }

}