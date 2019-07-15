package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springBoot入门
 */

//代表springboot的启动类,用来标注主程序类,说明是一个springboot应用
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //将springboot应用驱动起来
        SpringApplication.run(Application.class,args);
    }
}
