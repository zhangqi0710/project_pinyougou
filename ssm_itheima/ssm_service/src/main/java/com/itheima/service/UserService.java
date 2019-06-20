package com.itheima.service;

import com.itheima.domain.Role;
import com.itheima.domain.UserInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    /**
     * 查询所有用户
     * @return
     */
    List<UserInfo> findAll();

    /**
     * 添加用户
     * @param userInfo
     */
    void save(UserInfo userInfo);

    /**
     * 根据Id查询用户的详细信息
     * @param id
     * @return
     */
    UserInfo findById(String id);

    /**
     * 根据id查询该用户未拥有的角色
     * @param userId
     * @return
     */
    List<Role> findOtherRole(String userId);

    /**
     * 添加角色
     * @param userId
     * @param roleIds
     */
    void addRoleToUser(String userId, String[] roleIds);
}
