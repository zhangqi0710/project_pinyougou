package com.itheima.dao;

import com.itheima.domain.Member;
import com.itheima.domain.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductDao {
    //通过id查询商品信息
    @Select("select * from product where id = #{id}")
    public Product findById(@Param("id") String id);

    //查询所有的产品信息
    @Select("select * from product")
    public List<Product> findAll() throws Exception;

    @Insert("insert into product(productNum,productName,cityName,departureTime,productPrice,productDesc,productStatus) values(#{productNum},#{productName},#{cityName},#{departureTime},#{productPrice},#{productDesc},#{productStatus})")
    public void save(Product product);

    @Select("select * from product where id = #{id}")
    public Product findByOrdersId(String id);
}
