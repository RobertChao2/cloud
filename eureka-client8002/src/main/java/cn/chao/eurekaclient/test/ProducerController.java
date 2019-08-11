package cn.chao.eurekaclient.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    
    @GetMapping("/get/{id}")
    public String get(@PathVariable String id) {
        return "Eureka Producer 8002，hi，I can test this num is "+id;
    }

    @GetMapping("ribbonService")
    public String ribbonService(){
        return "this is 8002";
    }
}