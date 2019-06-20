package com.itheima.service.impl;

import com.itheima.dao.PermissionDao;
import com.itheima.domain.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Permission> findAll() {
        List<Permission> permissionList = permissionDao.findAll();
        return permissionList;
    }

    /**
     * 添加权限
     * @param permission
     */
    @Override
    public void save(Permission permission) {
        permissionDao.save(permission);
    }
}
