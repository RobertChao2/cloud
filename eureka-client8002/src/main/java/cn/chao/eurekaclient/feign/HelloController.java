package cn.chao.eurekaclient.feign;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/hello2", method= RequestMethod.GET)
    public User hello(@RequestHeader String name, @RequestHeader Integer age) {
        return new User(name, age);
    }

    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    public String hello (@RequestBody User user) {
        return "Hello "+ user. getName () + ", " + user. getAge ();
    }

}