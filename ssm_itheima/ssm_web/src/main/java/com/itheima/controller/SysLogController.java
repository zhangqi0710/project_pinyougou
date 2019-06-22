package com.itheima.controller;

import com.itheima.domain.SysLog;
import com.itheima.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {
    /**
     * 查询所有AOP日志信息
     */
    @Autowired
    private SysLogService sysLogService;
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        ModelAndView modelAndView = new ModelAndView();
        List<SysLog> sysLogList = sysLogService.findAll();
        modelAndView.addObject("sysLogList",sysLogList);
        modelAndView.setViewName("syslog-list");
        return modelAndView;
    }
}
