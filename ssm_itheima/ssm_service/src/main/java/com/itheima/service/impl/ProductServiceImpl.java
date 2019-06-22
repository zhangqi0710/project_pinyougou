package com.itheima.service.impl;

import com.itheima.dao.ProductDao;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    @PreAuthorize("hasAnyRole('Test')")
    public List<Product> findAll() throws Exception {
        return productDao.findAll();
    }

    /**
     * 添加商品
     * @param product
     */
    @Override
    @Secured("ROLE_TEST")
    public void save(Product product){
        productDao.save(product);
    }
}
