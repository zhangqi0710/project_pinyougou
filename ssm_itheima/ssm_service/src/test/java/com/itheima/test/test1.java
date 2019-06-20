package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test1 {
    @Test
    public void test(){
        String psw = "123";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(psw);
        System.out.println(encode);
    }
}
