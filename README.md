# cloud
开启 Spring Cloud 的实战。配置和开发，学习配置继而开发。
## 整体实现思路
【（2019.8.8）Eureka（服务内容(3)）】
## 起点：搭建空的 Maven 项目，存放其他项目模块。
【File】-->【new】-->【Project】-->【Maven】-->【不要勾选模板（Create from archetype）直接 
Next】-->【填写公司名称（GroupId）项目名称（artifactId）版本号】-->【因为是父工程，建议删除 src】-->【pom中增加<package>pom</package>】
## 一、创建 eureka-server8001（服务注册中心） 的项目内容
### 新建子工程 eureka-server8001
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Server 
依赖（Eureka Server）】-->【Finish 创建完成】
### 配置 eureka-server8001
1. 修改启动类 EurekaServer8001Application.java，添加 @EnableEurekaServer。
```text
@EnableEurekaServer     // 加入注解声明是 Eureka 服务注册中心。
@SpringBootApplication
public class EurekaServer8001Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer8001Application.class, args);
    }
}
```
2. 修改 application.properties 文件，在默认情况下，鼓舞注册中心会把自己当做一个服务，将自己注册到服务中心，我们可以通过配置来禁用他的客户端注册行为。
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
3. 启动 EurekaServer8001Application 项目即可。
4. 通过 http://localhost:8001/  访问即可。
5. 在【Instances currently registered with Eureka】信息中，没有一个实例，说明目前还没有服务注册。接下来创建一个服务提供者 eureka-client8002进行注册测试。
## 二、创建 erueka-client8002 (服务提供者) 的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Client 
依赖（Eureka Client、Web）】-->【Finish 创建完成】
* 选择 Web 组件或者其他能够持久运行的组件，不然会注册失败。
### 配置 eureka-client8002
1. 启动类 EurekaClient8002Application.java 添加 @EnableEurekaClient，用来以实现 Eureka 中的 DiscoveryClient 实现。
```text
@EnableEurekaClient
@SpringBootApplication
public class EurekaClient8002Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClient8002Application.class, args);
    }
}
```
2. @EnableEurekaClient 和 @EnableDiscoveryClient 的区别
```text
spring cloud 中  discovery service 有许多种实现（eureka、consul、zookeeper等等）
1、@EnableDiscoveryClient 基于 spring-cloud-commons。
2、@EnableEurekaClient 基于 spring-cloud-netflix。

注册中心是 eureka，推荐使用 @EnableEurekaClient。
如果是其他的注册中心，那么推荐使用 @EnableDiscoveryClient。

```
3. 修改 application.properties 文件。
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
4、编写 ProducerController.java 接口，测试访问内容: http://localhost:8002/get/1
```text
@RestController
public class ProducerController {
    @GetMapping("/get/{id}")
    public String get(@PathVariable String id) {
        return "Eureka Producer 8002，hi，I can test this num is "+id;
    }
}
```
## 三、创建 eureka-consumer8003 （服务消费者）的项目内容
【cloud】-->【new】-->【module】-->【Spring Initialize】-->【artifactId 设置项目名称】-->【加入Eureka Client 
依赖（Eureka Client、Web）】-->【Finish 创建完成】
> 依赖的内容：spring-boot-starter-web 和 spring-cloud-starter-netflix-eureka-client

### 配置 eureka-consumer8003 
1. 启动类 EurekaConsumer8003Application.java 添加注解 @EnableDiscoveryClient 。
```text
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumer8003Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumer8003Application.class, args);
    }
}
```
2. application.properties 的配置内容
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
3. 使用 服务消费者（eureka-consumer）访问服务提供者（eureka-producer）
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
