package com.itheima.dao;

import com.itheima.domain.Permission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface PermissionDao {
    @Select("select * from permission where id in (select permissionId from role_permission where roleId = #{roleId})")
    @Results({
            @Result(property = "permissionName",column = "permissionName"),
            @Result(property = "url",column = "url")
    })
    public Permission findById(String roleId);

    /**
     * 查询所有的权限
     * @return
     */
    @Select("select * from permission")
    List<Permission> findAll();

    /**
     * 添加权限
     * @param permission
     */
    @Insert("insert into permission (permissionName,url) values (#{permissionName},#{url})")
    void save(Permission permission);

}
