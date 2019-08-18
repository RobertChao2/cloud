package cn.chao.feignclient.originalHystix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author RobertChao
 */
public class originalHystrixCommand extends HystrixCommand {
    /*
        原生的Hystrix，跟我们之前的Hystrix用法一下，不要走Feign客户端调用就行了
     */
    protected originalHystrixCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    @Override
    protected Object run() throws Exception {
        return null;
    }
}