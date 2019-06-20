package com.itheima.dao;

import com.itheima.domain.Member;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MemberDao {
    @Select("select * from member where id = #{id}")
    public Member findByOrdersId(String id);
}
