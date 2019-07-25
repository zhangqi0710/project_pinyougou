package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillGoodsService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Reference(timeout = 10000)
    private SeckillGoodsService seckillGoodsService;

    /**
     * 获取秒杀商品
     * @return
     */
    @RequestMapping("/findList")
    public List<TbSeckillGoods> findList(){
        List<TbSeckillGoods> seckillGoods = seckillGoodsService.findList();
        return seckillGoods;
    }

    /**
     * 生产秒杀产品详细页
     * @param id
     * @return
     */
    @RequestMapping("/findOneFromRedis")
    public TbSeckillGoods findOneFromRedis(Long id){
        return seckillGoodsService.findOneFromRedis(id);
    }
}
