package com.pinyougou.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/login")
public class LoginController {
    /**
     * 页面显示用户名
     * @return
     */
    @RequestMapping("/name")
    public Map showName(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("username",username);
        return map;
    }

}
