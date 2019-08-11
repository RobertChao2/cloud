package cn.chao.feignclient.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 *  ①设置访问的服务（@FeignClient value），②、设置访问的请求（使用简单的请求方法），③、对错误进行处理（@FeignClient fallback）。
 */
@FeignClient(value = "eureka-client",fallback = FeignFallBack.class)
public interface FeignService {
    // 服务中方法的映射路径
    // Controller 层调用这个方法，在正常访问的情况下会访问到服务层的 http://eureka-client/hello 方法。
    @RequestMapping("/hello")
    String hello();

    // controller 层调用 http://eureka-client/hello1 的方法，GET 请求，请求参数是 name
    @RequestMapping(value = "/hellol", method= RequestMethod.GET)
    String hello(@RequestParam("name") String name) ;

    // 请求头
    @RequestMapping(value = "/hello2", method= RequestMethod.GET)
    User hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    // JSON 请求
    @RequestMapping(value = "/hello3", method= RequestMethod.POST)
    String hello(@RequestBody User user);
}