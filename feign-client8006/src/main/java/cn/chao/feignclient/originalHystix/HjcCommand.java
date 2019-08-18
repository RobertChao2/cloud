package cn.chao.feignclient.originalHystix;

import cn.chao.feignclient.feign.FeignService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class HjcCommand {

    @Autowired
    private FeignService feignService;

    // 同步方式
    @HystrixCommand
    public Future<String> getEmployeesAsync(){
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return feignService.hello("Tom");
            }
        };
    }

    // 用同步方式还不如直接用Feign客户端
    @HystrixCommand
    public String getEmployeesAsync1(){
        return feignService.hello("Jack");
    }

}