package cn.chao.hystrixclient.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *  熔断的内容建议编写在 service 中。
 */
@RestController
public class testHystrixController {

    private final String url = "eureka-client";
    // 使用 restTemplate 对 rest 接口进行调用封装的对象
    // RestTemplate 对象提供了多种便捷的 restful 服务模板类，是 Spring 提供的用于访问 rest 服务的客户端模板类。
    @Autowired
    private RestTemplate restTemplate;

    // 调用服务端的服务
    @RequestMapping(value = "testHystrix")
    @HystrixCommand(fallbackMethod = "fallBackMethod",commandKey = "testHystrix")
    public String testHystrix(){
        System.out.println("=== 进入要访问的方法 ===" + url);
        return restTemplate.getForObject("http://eureka-client/ribbonService",String.class);
    }

    // 建议把所有熔断方法放在 service 中去处理
    public String fallBackMethod(){
        System.out.println("容错机制");
        return "error";
    }
    /*
    验证方法一：断开具体的服务实例来模拟节点无法访问
        运行：测试注册中心，两个服务提供者，一个服务消费者。均要启动。
        第一次运行并访问 http://localhost:8005/testHystrix 轮询两个服务提供者。
            断开其中一个服务提供者
        当轮询到断开的那个服务的时，输出的内容为 ‘error’.
     */
    /*
     验证方法二：模拟长时间未响应，服务出现阻塞的情况。
     需要对服务提供者进行修改，
        int i = new Random().nextInt(3000);
        System.out.println("等待的时间" + i);
        try{
            Thread.sleep(i);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
      默认的 Hystrix 的超时时间是 2000 毫秒。
     */

}
