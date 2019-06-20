package com.itheima.service.impl;

import com.itheima.dao.OrdersDao;
import com.itheima.domain.Orders;
import com.itheima.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersDao ordersDao;
    @Override
    public List<Orders> findAll(int page,int size) {
        List<Orders> ordersList = ordersDao.findAll();
        return ordersList;
    }

    /**
     * 查询所有订单信息
     * @param id
     * @return
     */
    @Override
    public Orders findById(String id) {
        Orders orders = ordersDao.findById(id);
        return orders;
    }
}
