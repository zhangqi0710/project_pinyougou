package com.pinyougou.page.service.impl;

import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.*;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {
    //引入配置文件中关于FreeMarker的bean
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${pagedir}")
    private String pagedir;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 生成静态模型
     * @param goodIds
     * @return
     */
    @Override
    public Boolean genItemHtml(Long goodIds) {
        freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            //获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            //创建数据模型
            Map dataModel = new HashMap();
            //获取基本属性对象
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodIds);
            dataModel.put("goods",tbGoods);
            //获取扩展属性对象
            TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodIds);
            dataModel.put("goodsDesc",tbGoodsDesc);
            //获取商品的三级分类名称
            TbItemCat tbItemCat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
            TbItemCat tbItemCat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
            TbItemCat tbItemCat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
            dataModel.put("Category1",tbItemCat1.getName());
            dataModel.put("Category2",tbItemCat2.getName());
            dataModel.put("Category3",tbItemCat3.getName());
            //获取SkU页面集合
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");//商品状态为正常
            criteria.andGoodsIdEqualTo(goodIds);//获取指定商品
            example.setOrderByClause("is_default desc");//按是否默认降序排列
            List<TbItem> itemList = itemMapper.selectByExample(example);
            dataModel.put("itemList",itemList);
            //获取输出流
            Writer out = new FileWriter(pagedir + goodIds +".html");
            //模板输出
            template.process(dataModel,out);
            //释放流资源
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除静态模型
     * @param goodIds
     * @return
     */
    @Override
    public Boolean deleteItemHtml(Long[] goodIds) {
        try {
            for(Long goodsId:goodIds){
                new File(pagedir+goodsId+".html").delete();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
