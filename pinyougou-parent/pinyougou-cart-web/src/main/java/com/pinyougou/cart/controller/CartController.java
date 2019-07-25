package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Reference(timeout=5000)
    private CartService cartService;


    /**
     * 查询购物车
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //从cookie中获取购物车列表
        String cartListStr = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        //如果cookie中的购物车列表为空或者为空字符串,则将其设置为"[]"
        if (cartListStr == null || "".equals(cartListStr)) {
            cartListStr = "[]";
        }
        //将JSON字符串转化为JSON对象
        List<Cart> cartList_cookie = JSON.parseArray(cartListStr, Cart.class);

        if ("anonymousUser".equals(username)) {
            //用户没有登录,从cookie中获取购物车
            return cartList_cookie;
        }else {
            //用户登录,从缓存中获取购物车  ???????
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            //如果cookie中有购物车,合并购物车
            if (cartList_cookie.size()>0){
                List<Cart> cartList  = cartService.margeCartList(cartList_redis,cartList_cookie);
                //将合并的数据存入到redis中
                cartService.saveCartListToRedis(username,cartList);
                //删除cookie中的购物车
                util.CookieUtil.deleteCookie(request,response,"cartList");
                return cartList;
            }
                return cartList_redis;
        }
    }

    /**
     * 购物车添加商品
     *
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://localhost:9105",allowCredentials="true")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        //通过服务器端返回带有 Access-Control-Allow-Origin 标识的 Response header，用来解决资源的跨域权限问题。
        //response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
        //如果访问的资源带有cookie存储的资源,需要如下配置
        //response.setHeader("Access-Control-Allow-Credentials", "true");

        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.println(username);
        try {
            //查询cookie中的购物车列表
            List<Cart> cartList = findCartList();
            //给购物车添加商品,并返回购物车集合
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            if ("anonymousUser".equals(username)) {//用户没有登录,给cookie中存储购物车
                //将返回的购物车集合保存在cookie中
                util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                System.out.println("当前登录用户：" + username);
            } else {
                //用户登录,将购物车存储在缓存中
                cartService.saveCartListToRedis(username, cartList);
                System.out.println("向 cookie 存入数据");
            }
            return new Result(true, "添加商品成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加商品失败");
        }
    }


}
