package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 给购物车集合中添加购物车信息
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据商品 SKU ID 查询 SKU 商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item==null){
            throw new RuntimeException("商品不存在");
        }
        if (!"1".equals(item.getStatus())){
            throw new RuntimeException("商品状态无效");
        }
        //2.获取商家 ID
        String sellerId = item.getSellerId();
        //3.根据商家 ID 判断购物车列表中是否存在该商家的购物车
        Cart cart = searchCartBySellerId(cartList, sellerId);
        if (cart == null){
            //4.如果购物车列表中不存在该商家的购物车
            //4.1 新建购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            //获取购物项列表
            TbOrderItem orderItem = createOrderItem(item, num);
            List<TbOrderItem> orderItemList = new ArrayList<TbOrderItem>();
            //将购物项添加到购物项列表
            orderItemList.add(orderItem);
            //将购物项类表设置到购物车对象
            cart.setOrderItemList(orderItemList);
            //4.2 将新建的购物车对象添加到购物车列表
            cartList.add(cart);
        }else {
            //5.如果购物车列表中存在该商家的购物车
            // 查询购物车明细列表中是否存在该商品
            TbOrderItem orderItem1 = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (orderItem1 == null){
                //5.1. 如果没有,新增购物车明细
                orderItem1 = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem1);
            }else {
                //5.2. 如果有,在原购物车明细上添加数量,更改金额
                orderItem1.setNum(orderItem1.getNum()+num);
                orderItem1.setTotalFee(new BigDecimal(orderItem1.getNum()*orderItem1.getPrice().doubleValue()));
                //如果商品的数量小于1时,删除该商品
                if(orderItem1.getNum()<1){
                    cart.getOrderItemList().remove(orderItem1);//移除购物车明细
                }
                //如果购物车里的商品明细个数小于1,则删除该购物车
                if (cart.getOrderItemList().size()==0){
                    cartList.remove(cart);
                }
            }
        }
        return cartList;
    }

    /**
     * 用户登录后,将购物车存入缓存中
     * @param list
     * @param username
     */
    @Override
    public void saveCartListToRedis(String username,List<Cart> list) {
        System.out.println("向 redis 存入购物车数据....."+username);
        redisTemplate.boundHashOps("cartList").put(username,list);
    }

    /**
     * 用户登录后,从缓冲中获取购物车
     * @param username
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从 redis 中提取购物车数据....."+username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null){
            cartList = new ArrayList();
        }
        return cartList;
    }

    /**
     * 合并cookie和redis中的购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    @Override
    public List<Cart> margeCartList(List<Cart> cartList1,List<Cart> cartList2) {
        for (Cart cart : cartList2) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartList1;
    }

    /**
     * 根据商家 ID 判断购物车列表中是否存在该商家的购物车
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId){
        for(Cart cart:cartList){
            if(cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    /**
     * 给购物车详细设置数据
     * @param item
     * @param num
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item,Integer num){
        if(num<=0){
            throw new RuntimeException("数量非法");
        }
        TbOrderItem orderItem=new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
        orderItem.setTotalFee(item.getPrice().multiply(new BigDecimal(num)));
        return orderItem;
    }

    /**
     * 比较购物项ID和添加的商品ID,返回比较的结果
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList ,Long itemId){
        for(TbOrderItem orderItem : orderItemList){
            if(orderItem.getItemId().longValue()==itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }
}
