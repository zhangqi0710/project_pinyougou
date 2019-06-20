package com.itheima.service.impl;

import com.itheima.dao.RoleDao;
import com.itheima.domain.Permission;
import com.itheima.domain.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAll() {
       List<Role> roleList = roleDao.findAll();
        return roleList;
    }

    /**
     * 保存角色
     * @param role
     */
    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    /**
     * 根据id查询role信息
     * @param roleId
     * @return
     */
    @Override
    public Role findRoleById(String roleId) {
        Role role = roleDao.findRoleById(roleId);
        return role;
    }

    /**
     * 根据id查询该role角色我拥有的权限
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> findOtherPermission(String roleId) {
        List<Permission> permissionList = roleDao.findOtherPermission(roleId);
        return permissionList;
    }

    /**
     * 根据id给指定的role添加权限
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void addPermissionToRole(String roleId, String[] permissionIds) {
        for (String permissionId : permissionIds) {
            roleDao.addPermissionToRole(roleId,permissionId);
        }
    }
}
