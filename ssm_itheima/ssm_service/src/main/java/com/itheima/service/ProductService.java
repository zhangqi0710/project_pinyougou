package com.itheima.service;

import com.itheima.domain.Product;

import java.util.List;

public interface ProductService {
    //查询所有
    public List<Product> findAll() throws Exception;
    //添加商品
    void save(Product product) throws Exception;
}
