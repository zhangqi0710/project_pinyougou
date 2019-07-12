package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
public class ItemPageService implements com.pinyougou.page.service.ItemPageService {
    //引入配置文件中关于FreeMarker的bean
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${pagedir}")
    private String pagedir;
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Override
    public Boolean genItemHtml(Long goodIds) {
        freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            //获取模板对象
            Template template = configuration.getTemplate("item_ftl");
            //创建数据模型
            Map dataModel = new HashMap();
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodIds);
            dataModel.put("goods",tbGoods);
            TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodIds);
            dataModel.put("goodsDesc",tbGoodsDesc);
            //获取输出流
            Writer out = new FileWriter(pagedir + goodIds +".html ");
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
}
