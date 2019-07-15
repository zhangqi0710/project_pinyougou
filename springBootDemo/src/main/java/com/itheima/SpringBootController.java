package com.itheima;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringBootController {
    @RequestMapping("/springBoot")
    public String springBootDemo(){
        return "hello springBoot...";
    }
}
