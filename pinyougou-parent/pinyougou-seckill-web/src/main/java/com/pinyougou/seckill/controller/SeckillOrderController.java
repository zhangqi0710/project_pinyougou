package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.seckill.service.SeckillOrderService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submitOrder")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/submitOrder")
    public Result submitOrder(Long seckillId){
        //获取登录用户名
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //如果未登录,提示登录
        if("anonymousUser".equals(userId)){
            return new Result(false, "用户未登录");
        }
        try {
            //保存秒杀的订单到缓存中
            seckillOrderService.submitOrder(seckillId, userId);
            return new Result(true, "提交成功");
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "提交失败");
        }
    }
}
