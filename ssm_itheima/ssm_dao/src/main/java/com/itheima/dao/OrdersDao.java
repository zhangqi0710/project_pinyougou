package com.itheima.dao;

import com.itheima.domain.Member;
import com.itheima.domain.Orders;
import com.itheima.domain.Product;
import com.itheima.domain.Traveller;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrdersDao {

    @Select("select * from orders")
    @Results({
        @Result(id=true,column = "id",property = "id"),
        @Result(column = "orderNum",property = "orderNum"),
        @Result(column = "orderTime",property = "orderTime"),
        @Result(column = "orderStatus",property = "orderStatus"),
        @Result(column = "peopleCount",property = "peopleCount"),
        @Result(column = "payType",property = "payType"),
        @Result(column = "orderDesc",property = "orderDesc"),
        @Result(property = "product", column = "productId",javaType = Product.class ,one = @One(select = "com.itheima.dao.ProductDao.findById"))
})
    public List<Orders> findAll();

    @Select("select * from orders where id = #{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "orderNum",column = "orderNum"),
            @Result(property = "orderTime",column = "orderTime"),
            @Result(property = "orderDesc",column = "orderDesc"),
            @Result(property = "peopleCount",column = "peopleCount"),
            @Result(property = "payType",column = "payType"),
            @Result(property = "orderStatus",column = "orderStatus"),
            @Result(property = "product",column = "productId",javaType = Product.class,one = @One(select = "com.itheima.dao.ProductDao.findByOrdersId")),
            @Result(property = "member",column = "memberId",javaType = Member.class,one = @One(select = "com.itheima.dao.MemberDao.findByOrdersId")),
            @Result(property = "travellers",column = "id",javaType = java.util.List.class,many = @Many(select = "com.itheima.dao.TravellerDao.findByOrdersId"))
    })
    Orders findById(String ordersId);
}
