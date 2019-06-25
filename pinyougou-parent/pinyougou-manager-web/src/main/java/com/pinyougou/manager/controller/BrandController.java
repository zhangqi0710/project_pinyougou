package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellerfoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌
     * @return
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public List<TbBrand> findAll(){
        List<TbBrand> BrandList = brandService.findAll();
        return BrandList;
    }

    /**
     * 分页查询所有品牌
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/findPage")
    @ResponseBody
    public PageInfo findPage(int page, int size){
        PageInfo pageInfo = brandService.findPage(page, size);
        return pageInfo;
    }

    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestBody TbBrand brand){
        try {
            //添加成功
            brandService.add(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            //添加失败
            return new Result(false,"添加失败");
        }
    }

    /**
     * 根据id查询品牌
      * @param id
     * @return
     */
    @RequestMapping("/findOne")
    @ResponseBody
    public TbBrand findOne(Long id){
         return brandService.findOne(id);
    }

    /**
     * 修改品牌
     * @param brand
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestBody TbBrand brand){
        try {
            brandService.update(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"添加失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Long[] ids){
        try {
            //删除成功
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            //删除失败
            return new Result(true,"删除失败");
        }
    }

    @RequestMapping("/findByExample")
    @ResponseBody
    public PageInfo findByExample(@RequestBody TbBrand brand,int page,int size){
        PageInfo pageInfo = brandService.findByExample(brand, page, size);
        return pageInfo;
    }
}
