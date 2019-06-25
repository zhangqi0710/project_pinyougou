package com.itheima.dubbodemo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dubbodemo.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getName() {
        return "hello dubbo...";
    }
}
