# cloud
开启 Spring Cloud 的实战。配置和开发，学习配置继而开发。
#### Spring Boot 基础功能:
* 服务治理：Spring Cloud Eureka
* 客户端负载均衡：Spring Cloud Ribbon
* 服务容错保护：Spring Cloud Hystrix
* 声明式服务调用：Spring Cloud Feign
* API 网关服务：Spring Cloud Zuul
* 分布式配置中心：Spring Cloud Config
#### Spring Boot 高级功能:
* 消息总线：Spring Cloud Bus
* 消息驱动的微服务：Spring Cloud Stream
* 分布式服务跟踪：Spring Cloud Sleuth
## 整体实现思路
【（2019.8.8）Eureka（服务内容(3)）】
【（2019.8.11）Ribbon、Hystrix、Feign】
## 起点：搭建空的 Maven 项目，存放其他项目模块。
【File】-->【new】-->【Project】-->【Maven】-->【不要勾选模板（Create from archetype）直接 
Next】-->【填写公司名称（GroupId）项目名称（artifactId）版本号】-->【因为是父工程，建议删除 src】-->【pom中增加<package>pom</package>】
## 一、服务治理 Eureka (尤里卡)
> 通过 Eureka 的服务治理框架，我们可以通过服务名来获取具体的服务实例的位置（IP）。
### （1）、创建 eureka-server8001（服务注册中心） 的项目内容
#### 新建子工程 eureka-server8001
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Server 
依赖（Eureka Server）】-->【Finish 创建完成】
#### 配置 eureka-server8001
* ①、修改启动类 EurekaServer8001Application.java，添加 @EnableEurekaServer。
```text
@EnableEurekaServer     // 加入注解声明是 Eureka 服务注册中心。
@SpringBootApplication
public class EurekaServer8001Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer8001Application.class, args);
    }
}
```
* ②、修改 application.properties 文件，在默认情况下，鼓舞注册中心会把自己当做一个服务，将自己注册到服务中心，我们可以通过配置来禁用他的客户端注册行为。
```text
spring.application.name=eureka-server
#服务注册中心端口号
server.port=8001
#服务注册中心实例的主机名
eureka.instance.hostname=localhost
#是否向服务注册中心注册自己
eureka.client.register-with-eureka=false
#是否检索服务
eureka.client.fetch-registry=false
#服务注册中心的配置内容，指定服务注册中心的位置
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
```
* ③、启动 EurekaServer8001Application 项目即可。
* ④、通过 http://localhost:8001/  访问即可。
* ⑤、在【Instances currently registered with Eureka】信息中，没有一个实例，说明目前还没有服务注册。接下来创建一个服务提供者 eureka-client8002进行注册测试。
### （2）、创建 erueka-client8002 (服务提供者) 的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Client 
依赖（Eureka Client、Web）】-->【Finish 创建完成】
* 选择 Web 组件或者其他能够持久运行的组件，不然会注册失败。
#### 配置 eureka-client8002
* ①、启动类 EurekaClient8002Application.java 添加 @EnableEurekaClient，用来以实现 Eureka 中的 DiscoveryClient 实现。
```text
@EnableEurekaClient
@SpringBootApplication
public class EurekaClient8002Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClient8002Application.class, args);
    }
}
```
* ②、@EnableEurekaClient 和 @EnableDiscoveryClient 的区别
```text
spring cloud 中  discovery service 有许多种实现（eureka、consul、zookeeper等等）
1、@EnableDiscoveryClient 基于 spring-cloud-commons。
2、@EnableEurekaClient 基于 spring-cloud-netflix。

注册中心是 eureka，推荐使用 @EnableEurekaClient。
如果是其他的注册中心，那么推荐使用 @EnableDiscoveryClient。

```
* ③、修改 application.properties 文件。
```text
spring.application.name=eureka-client
server.port=8002
# 心跳时间，即服务续约间隔时间（缺省为30s）
eureka.instance.lease-renewal-interval-in-seconds=5
# 发呆时间，即服务续约到期时间（缺省为90s）
eureka.instance.lease-expiration-duration-in-seconds=15
# 开启健康检查（依赖spring-boot-starter-actuator）
eureka.client.healthcheck.enabled=true
eureka.client.serviceUrl.defaultZone=http://localhost:8001/eureka
# instance-id: ${spring.application.name}                     # 修改显示的微服务名为：应用名称
# 默认的显示名称是 机器主机名:应用名称:应用端口 ${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}}
eureka.client.instance-id=${spring.cloud.client.ipAddress}:${server.port}  # 修改显示的微服务名为：IP:端口
```
* ④、编写 ProducerController.java 接口，测试访问内容: http://localhost:8002/get/1
```text
@RestController
public class ProducerController {
    @GetMapping("/get/{id}")
    public String get(@PathVariable String id) {
        return "Eureka Producer 8002，hi，I can test this num is "+id;
    }
}
```
### （3）、创建 eureka-consumer8003 （服务消费者）的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Client 
依赖（Eureka Client、Web）】-->【Finish 创建完成】
> 依赖的内容：spring-boot-starter-web 和 spring-cloud-starter-netflix-eureka-client
> 一般在使用 Spring Cloud 的时候不需要手动创建 HttpClient 实例来进行远程调用，可以使用 Spring 封装好的 RestTemplate 的工具类。
#### 配置 eureka-consumer8003 
* ①、启动类 EurekaConsumer8003Application.java 添加注解 @EnableDiscoveryClient 。
```text
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumer8003Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumer8003Application.class, args);
    }
}
```
* ②、application.properties 的配置内容
```text
spring:
  application:
    name: eureka-consumer
server:
  port: 8003
eureka:
  instance:
    prefer-ip-address: true
  client:
    serviceUrl:
      defaultZone: http://localhost:8001/eureka/
```
* ③、使用 服务消费者（eureka-consumer）访问服务提供者（eureka-producer）
```text
// 1、首先确定 RestTemplate
@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```
```text
// 2、接着使消费者访问提供者
@RestController
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/c/get/{id}")
    public String get(@PathVariable String id) {
        String result = restTemplate.getForObject("http://eureka-client/get/"+id, String.class);
        return result;
    }
}
```
> 这里消费者访问的路径是提供者的${spring.application.name}

## 二、客户端负载均衡 Ribbon
> 负载均衡的区分：服务端负载均衡(硬件负载均衡和软件负载均衡[Nginx])与客户端负载均衡(Ribbon)。
> 1、客户端负载均衡：服务实例的清单在客户端，客户端进行负载均衡算法分配。(客户端可以从 Eureka Server[服务注册中心]得到服务清单，在发送请求时通过负载均衡算法，在多个服务之间选择一个进行访问。)
> 2、服务端负载均衡：服务实例的清单在服务器，服务器进行负载均衡算法分配。
### 创建 ribbon_client8004 （客户端负载均衡）的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Server 
依赖（Eureka Server）】-->【Finish 创建完成】
> 依赖包含 spring-boot-starter-web、spring-cloud-starter-netflix-eureka-client 以及 spring-cloud-starter-netflix-ribbon。

> Ribbon 是一个工具类框架，不需要独立部署，几乎存在于每一个 Spring Cloud 构建的微服务和基础设施中。
> 框架非常的好用，只需要一下两步：
> 1、服务提供者只需要启动等多个服务实例并注册到一个注册中心或是多个相关的服务注册中心。
> 2、服务消费者直接通过调用被 @LoadBalanced 注解修饰过的 RestTemplate 来实现面向服务的接口调用。
> 通过上述的两步操作就可以实现服务提供者的高可用，以及服务消费者的负载均衡。
#### 创建工程 ribbon_client8004 
* ①、开启服务注册以及负载均衡
```text
@EnableDiscoveryClient
@SpringBootApplication
public class RibbonClient8004Application {
    public static void main(String[] args) {
        SpringApplication.run(RibbonClient8004Application.class, args);
    }
}
```
* ②、注入 RestTemplate 以及轮询策略，进而执行客户端的方法。
```text
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
```
```text
// 2. 执行客户端的方法调用。
@RestController
public class RibbonController {

    @Autowired
    RestTemplate restTemplate;
    
    @GetMapping("/{applicationName}/ribbonService")
    public String ribbonService(@PathVariable("applicationName") String applicationName){
        // getForObject 的请求，返回是一个字符串
        return restTemplate.getForObject("http://" + applicationName+"/ribbonService", String.class);
    }
}
```
* ③、使用 @RibbonClient 或 @RibbonClients 注解为服务提供者指定配置类。
```text
@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "ribbon-client",configuration = RibbonConfig.class)
public class ribbonClient8004Application {

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }
  
  public static void main(String[] args) {
    SpringApplication.run(ribbonClient8004Application.class, args);
  }
}
```
* ④、注意注意的内容
> RibbonConfiguration 类不能被 @ComponentScan 扫描到，否则配置信息就会被所有 @RibbonClient 共享，
> 因此如果只想自定义某个 Ribbon 客户端的配置，必须防止被 @ComponentScan 扫描

## 三、服务容错保护 Hystrix 实现了断路器、线程隔离等一系列服务保护功能。
> 到目前为止，所有服务都可以通过服务名来远程调用其他的服务，可以实现客户端的负载均衡。

> 同样作为基础工具类框架广泛地应用在各个微服务的实现中的 Hystrix
####“雪崩”的概念
> 在高并发的情况下，由于单个服务的延迟，可能导致所有的请求都处于延迟状态，甚至在几秒钟就使服务处于负载饱和的状态，资源耗尽，直到不可用，最终导致这个分布式系统都不可用。
#### 失败快速返回（FallBack） 
当某个服务单元发生故障（类似用电器发生短路）之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个错误响应，而不是长时间的占用不释放，避免了故障在分布式系统中的蔓延。
#### 线程池隔离（资源/依赖隔离）
它会为每一个依赖服务创建爱你一个独立的线程池，这样就算某个一阿里服务出现延迟过高的情况，也只是对改以来服务的调用产生影响，而不会拖慢其他的依赖服务。
#### Hystrix 熔断器的参数
【滑动窗口大小（20）】、【熔断器开关间隔（5s）】、【错误率（50%）】
* 每当有 20 个请求中，有 50 %失败时，熔断器就会打开，此时在调用服务，将会直接返回失败，不在调用远程服务。
* 知道 5s 之后，重新检测该触发条件，判断是否把熔断器关闭，或者继续打开。
### 创建 hystrix_client8005 （客户端负载均衡）的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Server 
依赖（Eureka Server）】-->【Finish 创建完成】
> hystrix和 hystrix-dashboard 相关的依赖
#### 配置 hystrix-client8005 
* ①、在启动类上 Hystrixclient8005Application.java 添加注解 @EnableCircuitBreaker
```text
@EnableDiscoveryClient
@EnableCircuitBreaker   // 开启熔断器功能。
@SpringBootApplication
public class Hystrixclient8005Application {

    public static void main(String[] args) {
        SpringApplication.run(Hystrixclient8005Application.class, args);
    }

}
```
* ②、改造服务的消费方式，新增 testHystrix 的 Controller 类。
```text
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
        System.out.println("=== 进入要访问的方法 ===");
        return restTemplate.getForObject("http://"+url+"/ribbonService",String.class);
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
```
#### 项目运行过程中，一直未加入到 Eureka 的服务注册中心
【猜想（应该是依赖的问题）】spring-cloud-starter-netflix-eureka-client 包含了 spring-cloud-starter-eureka 可以将 eureka 
注册到服务中心，之前使用的是 spring-cloud-netflix-eureka-client 不能够达到效果 。
## 六、声明式服务调用 Spring Cloud Feign
> 当我们通过RestTemplate调用其它服务的API时，所需要的参数须在请求的URL中进行拼接，如果参数少的话或许我们还可以编写，一旦有多个参数的话，这时拼接请求字符串就会效率低。

> Feign 是一个声明式的 Web Service 客户端，它的目的就是让 Web Service 调用更加简单。Feign 提供了 HTTP 请求的模板，通过编写简单的接口和插入注解，就可以定义好HTTP请求的参数、格式、地址等信息。  
> 而 Feign 则会完全代理 HTTP 请求，我们只需要像调用方法一样调用它就可以完成服务请求及相关处理。
> Feign 整合了 Ribbon 和 Hystrix（前边的两个章节我们说过了），可以让我们不再需要显式地使用这两个组件。

> Feign 具有如下特性：
> * 可插拔的注解支持，包括 Feign注解 和 JAX-RS注解;
> * 支持可插拔的HTTP编码器和解码器;
> * 支持 Hystrix 和它的 Fallback;
> * 支持 Ribbon 的负载均衡;
> * 支持 HTTP 请求和响应的压缩。
### 创建 feign-client8006 （声明式服务调用）的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Server 
依赖（Eureka Server）】-->【Finish 创建完成】
> 引入依赖：spring-boot-starter-web、spring-cloud-starter-netflix-eureka-client 以及 spring-cloud-starter-openfeign
仍然需要导入 spring-cloud-starter-hystrix ，否则在 @SpringCloudApplication 注解中使用会报错。
#### 配置 feign-client8006
* ①、启动类上使用 @SpringCloudApplication 注解，同时引入 Spring Boot 与 Ribbon 与 Hystrix ，添加 @FeignClients 开启声明式服务
```text
@SpringCloudApplication
@EnableFeignClients
public class FeignClient8006Application {

    public static void main(String[] args) {
        SpringApplication.run(FeignClient8006Application.class, args);
    }

}
```
* ②、application.properties 中只需要能够 从服务注册中心（Eureka Server） 中获取请求的服务即可。
```text
    spring.application.name=feign-client
    server.port=8006
    eureka.client.service-url.defaultZone=http://localhost:8001/eureka
    eureka.instance.ip-address=true
```
* ③、新建 Package 名为 feign ，存放 controller 、service 以及 entity 。
```text
### entity

public class User {

    private String name;
    private Integer age;

    //序列化传输的时候必须要有空构造方法，不然会出错
    public User() {
    }
    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
```
```text
### controller 设置的内容是通过 8006 端口访问的 url。

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
```
```text
### service 上一章提到过，建议将 Hystrix 的 FeignFallBack 写在 Service 层。

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

// 进行Feign声明式调用服务下的，服务降级的使用。

@Component
public class FeignFallBack implements FeignService{
    // 实现的方法是服务调用的降级方法
    @Override
    public String hello() {
        return "error";
    }

    @Override
    public String hello(String name) {
        return "error";
    }

    @Override
    public User hello(String name, Integer age) {
        return new User();
    }

    @Override
    public String hello(User user) {
        return "error";
    }
}
```
* ④、上述内容是输入 服务消费者（feign-client）调用的内容，服务提供者（eureka-client）同样需要作出修改。
```text
// 在 eureka-client 中作出响应的修改是必须的。 

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("访问来1了......");
        return "hello1";
    }

    @RequestMapping("/addUser")
    public List<String> addUser(String ids){
        List<String> list = new ArrayList<>();
        list.add("Jack");
        list.add("Tom");
        list.add("cat");
        return list;
    }

    // 新增的方法
    @RequestMapping(value = "/hellol", method= RequestMethod.GET)
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }

    // 返回的是实体类
    @RequestMapping(value = "/hello2", method= RequestMethod.GET)
    public User hello(@RequestHeader String name, @RequestHeader Integer age) {
        return new User(name, age);
    }

    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    public String hello (@RequestBody User user) {
        return "Hello "+ user. getName () + ", " + user. getAge ();
    }

}
```
* ⑤、编写代码，测试代码内容的执行。
```text
http://localhost:8006/consumer 的执行。    

    会通过 Feign 的 service 向对应的 hello() 方法发送 http://eureka-client/hello 的请求 
```
```text
http://localhost:8006/consumer2 的执行。
    
    会通过 方法重载（@Overload）调用不同的 service 的 hello() 方法，发送请求内容。
    这些请求会通过 service 中设置的请求名称去访问 @FeignClient 中的 value 中相同的请求名称，携带者相同的参数内容。服务提供者出现请求超时或者断开的问题，会通过 FallBack 的方法回调。
```
* ⑥、通过原生的 Hystrix 请求 service 的内容。（originalHystrix Package Content）
## 七、服务网关 API Zuul
官方文档：https://springcloud.cc/spring-cloud-dalston.html#_router_and_filter_zuul
### 创建 zuul-client8007 （服务网关）的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Server 
依赖（Eureka Server）】-->【Finish 创建完成】
> spring-cloud-starter-eureka 与 spring-cloud-starter-eureka
#### 项目中目前存在的安全问题（为什么需要网关(1)） ？
    进入到一个网站中，请求一个服务，没有特别好的办法，就是通过IP地址+端口号。这样的做法，不仅暴露了我们实体机器的IP
地址，而且处于微服务项目中服务众多，一个一个的启动项目，挨个调用项目的确很复杂。添加一个公共的模块，难道要一个一个的去添加，显然很是麻烦的。
#### 解决这两个问题呢（为什么需要网关(2)）？
* 解决访问服务问题，可以像负载均衡那样，通过动态的维护服务的列表去维护。
* 解决 IP 及 PORT ，可以像 Nginx 做一个反向代理，部署公共模块。

1. 通过 Zuul API 网关，就可以解决上面的问题。调用某个服务，它会给映射，通过服务的 IP地址 映射成路径，输入该路径，即可找到对应的服务，存在一个请求转发的过程，有些像 Nginx 一样，它不会直接去访问IP，它会去 
Eureka 注册中心拿到服务的实例ID（即服务的名字）。
    
2. Zuul 可以通过加载动态过滤机制，从而实现以下各项功能：

　　①、验证与安全保障: 识别面向各类资源的验证要求并拒绝那些与要求不符的请求。

　　②、审查与监控: 在边缘位置追踪有意义数据及统计结果，从而为我们带来准确的生产状态结论。

　　③、动态路由: 以动态方式根据需要将请求路由至不同后端集群处。

　　④、压力测试: 逐渐增加指向集群的负载流量，从而计算性能水平。

　　⑤、负载分配: 为每一种负载类型分配对应容量，并弃用超出限定值的请求。

　　⑥、静态响应处理: 在边缘位置直接建立部分响应，从而避免其流入内部集群。

　　⑦、多区域弹性: 跨越AWS区域进行请求路由，旨在实现ELB使用多样化并保证边缘位置与使用者尽可能接近。
### 配置 zuul-client8007 
* ①、启动类上添加 @SpringCloudApplication 和 @EnableZuulProxy 的注解。
```text
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulClient8007Application {

    public static void main(String[] args) {
        SpringApplication.run(ZuulClient8007Application.class, args);
    }

}
```
* ②、配置文件中增加内容 application.properties
```text

server.port=8007
spring.application.name=zuul-client
eureka.instance.ip-address=true
eureka.client.service-url.defaultZone=http://localhost:8001/eureka
eureka.client.healthcheck.enabled=true
zuul.routes.springcloud-a.path=/springcloud-a/**
zuul.routes.springcloud-a.service-id=eureka-client  # 微服务
zuul.routes.springcloud-b.path=/springcloud-b/**
zuul.routes.springcloud-b.service-id=eureka-consumer # 微服务

```

* ③、服务配置内容如下：
```text
1. eureka-client :是之前创建的服务客户端，端口是 8002
2. eureka-consumer :是之前创建的服务消费者，端口是 8003
3. 这两个微服务要与本 zuul 项目要同时启动，并注册到统一服务中心内。
```

* 4、设置 zuul 的过滤器。内容详情请见代码，这里就不更新了。
## Git 项目版本控制器
### idea 提交项目时候出现上传拒绝（ Push rejected）
* **原因**：Push rejected: Push to origin/master was rejected  拒绝推到主分支
1. 因为本地仓库和远程仓库的代码不一样。简单来说就是代码冲突了，复杂来说就是你动了不该动的代码。
* **解决办法**：
1. 切换到项目所在目录，右键选择 git bush here。或者在 idea 中按快捷键 【Alt + F12】（快速打开项目文件路径的 terminal）。
2. terminal 中输入如下的内容即可解决问题（请按照顺序输入）：
```text
    1、git pull origin master --allow-unrelated-histories    （允许不相关的代码提交）
    输入上述命令即可解决，在提交内容即可。
    2、git push -u origin master -f 使用强制提交
```
## idea 设置内容工具
### idea 设置代码行宽度
## 需要学习的内容
### 集群
> 计算机集群简称集群，是一种计算机系统，它通过一组松散集成的计算机软件和(或)硬件连接起来高度紧密地协作完成计算工作。在某种意义上，他们可以被看作是一台计算机。
> 集群系统中的单个计算机通常称为**节点**，通常通过局域网连接，但也有其它的可能连接方式。集群计算机通常用来改进单个计算机的计算速度和(或)可靠性。

### 分布式
> 分布式系统是一组计算机，通过网络相互连接传递消息与通信后并协调他们的行为而形成的系统。组件之间彼此进行交互以实现一个共同的目标。

#### 分布式集群
> eg:公司内，5个Java 程序猿，4个前端程序媛，2个测试程序员，1个数据库管理员(DBA)
>   4 种人物的关系就是分布式的。
>   5 个 Java 程序猿 就是集群。
### 微服务

### SOA

### RestTemplate 详解
#### GET 请求
* getForEntity()
* getForObject()
#### POST 请求
* postForEntity()
* postForObject()
#### PUT 请求
* put()
#### DELETE 请求
* delete()