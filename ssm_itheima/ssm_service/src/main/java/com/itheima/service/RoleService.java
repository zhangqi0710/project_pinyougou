package com.itheima.service;

import com.itheima.domain.Permission;
import com.itheima.domain.Role;

import java.util.List;

public interface RoleService {
    /**
     *      * 查询所有role信息
     * @return
     */
    List<Role> findAll();

    /**
     * 保存role
     * @param role
     */
    void save(Role role);

    /**
     * 根据id查询role信息
     * @param roleId
     * @return
     */
    Role findRoleById(String roleId);

    /**
     * 根据id查询该role就角色未拥有的权限
     * @param roleId
     * @return
     */
    List<Permission> findOtherPermission(String roleId);

    /**
     * 根据id给指定的role添加权限
     * @param roleId
     * @param permissionIds
     */
    void addPermissionToRole(String roleId, String[] permissionIds);
}
