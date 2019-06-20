package com.itheima.controller;

import com.itheima.domain.Role;
import com.itheima.domain.UserInfo;
import com.itheima.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     * @return
     */
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        ModelAndView modelAndView = new ModelAndView();
        List<UserInfo> userInfoList = userService.findAll();
        modelAndView.addObject("userInfoList",userInfoList);
        modelAndView.setViewName("user-list");
        return modelAndView;
    }

    /**
     * 添加用户
     * @return
     */
    @RequestMapping("/save")
    public String save(UserInfo userInfo){
        userService.save(userInfo);
        return "redirect:findAll";
    }

    /**
     * 根据Id查询用户的详细信息
     * @return
     */
    @RequestMapping("/findById")
    public ModelAndView findById(String id){
        ModelAndView modelAndView = new ModelAndView();
        UserInfo userInfo = userService.findById(id);
        modelAndView.addObject("userInfo",userInfo);
        modelAndView.setViewName("user-show1");
        return modelAndView;
    }


    /**
     * 根据Id查询用户的详细信息并且给用户添加角色信息
     * @return
     */
    @RequestMapping("/findUserByIdAndAllRole")
    public ModelAndView findUserByIdAndAllRole(@RequestParam(name = "id") String userId){
        ModelAndView modelAndView = new ModelAndView();
        //查询用户的所有信息
        UserInfo userInfo = userService.findById(userId);
        //查询该用户未拥有的角色信息
        List<Role> roleList = userService.findOtherRole(userId);
        modelAndView.addObject("userInfo",userInfo);
        modelAndView.addObject("roleList",roleList);
        modelAndView.setViewName("user-role-add");
        return modelAndView;
    }

    /**
     * 给用户添加角色
     * @return
     */
    @RequestMapping("/addRoleToUser")
    public String addRoleToUser(@RequestParam(name = "userId", required = true) String userId,@RequestParam(name = "ids", required = true) String[] roleIds){
        userService.addRoleToUser(userId,roleIds);
        return "redirect:findAll";
    }
}
