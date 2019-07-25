package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

public interface CartService {
    /**
     * 给购物车集合中添加购物车信息
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * 用户登陆后将购物车存入缓存中
     * @param list
     * @param username
     */
    public void saveCartListToRedis(String username,List<Cart> list);


    /**
     * 用户登陆后,获取在缓存中的购物车
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

    /**
     * 合并cookie和redis中的购物车
     * @param list1
     * @param list2
     * @return
     */
    public List<Cart> margeCartList(List<Cart> list1,List<Cart> list2);
}
