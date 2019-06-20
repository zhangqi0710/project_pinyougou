package com.itheima.dao;

import com.itheima.domain.Permission;
import com.itheima.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleDao {
    @Select("select * from role where id in(select roleId from users_role where userId = #{userId})")
    public List<Role> findById(String userId);


    @Select("select * from role where id in (select roleId from users_role where userId = #{userId})")
    @Results({
            @Result(property = "roleName", column = "roleName"),
            @Result(property = "roleDesc", column = "roleDesc"),
            @Result(property = "permissions", column = "id",javaType = java.util.List.class,many = @Many(select = "com.itheima.dao.PermissionDao.findById")),
    })
    public Role findByUserId(String userId);

    /**
     * 查询所有role信息
     * @return
     */
    @Select("select * from role")
    List<Role> findAll();

    /**
     * 保存角色
     */
    @Insert("insert into role (roleName,roleDesc) values (#{roleName},#{roleDesc})")
    void save(Role role);

    /**
     * 根据id查询role信息
     * @param roleId
     * @return
     */
    @Select("select * from role where id = #{roleId}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "roleName",  column = "roleName"),
            @Result(property = "roleDesc",  column = "roleDesc"),
            @Result(property = "permissions",  column = "id",javaType = java.util.List.class,many = @Many(select = "com.itheima.dao.PermissionDao.findById"))

    })
    Role findRoleById(String roleId);

    /**
     * 根据id查询该role角色未拥有的权限
     * @param roleId
     * @return
     */
    @Select("select * from permission where id not in (select permissionId from role_permission where roleId = #{roleId})")
    List<Permission> findOtherPermission(String roleId);

    /**
     * 根据id给指定的role添加权限
     * @param roleId
     * @param permissionId
     */
    @Insert("insert into role_permission (roleId,permissionId) values (#{roleId},#{permissionId})")
    void addPermissionToRole(@Param("roleId") String roleId, @Param("permissionId") String permissionId);
}
