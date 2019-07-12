package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class contentController {

    @Reference
    private ContentService contentService;

    //查询categoryID为1的图片
    @RequestMapping("/findContentList")
    public List<TbContent> findContentList(Long categoryId){
        List<TbContent> category1List = contentService.findCategory1List(categoryId);
        return category1List;
    }
}
