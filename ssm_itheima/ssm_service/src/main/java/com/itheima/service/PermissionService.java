package com.itheima.service;

import com.itheima.domain.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 查询所有的权限
     * @return
     */
    List<Permission> findAll();

    /**
     * 添加权限
     * @param permission
     */
    void save(Permission permission);
}
