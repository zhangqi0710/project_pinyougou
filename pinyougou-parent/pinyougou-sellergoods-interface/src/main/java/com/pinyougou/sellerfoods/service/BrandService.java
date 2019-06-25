package com.pinyougou.sellerfoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

public interface BrandService {
    //查询所有的品牌
    public List<TbBrand> findAll();

    //分页查询
    public PageInfo findPage(int pageNum, int pageSize);

    //添加品牌
    public void add(TbBrand brand);

    //根据id查找品牌
    public TbBrand findOne(Long id);

    //根据id查找品牌后，再修改品牌
    public void update(TbBrand brand);

    //根据ids删除品牌
    public void delete(Long[] ids);

    //条件查询品牌
    public PageInfo findByExample(TbBrand brand,int page,int size);
}
