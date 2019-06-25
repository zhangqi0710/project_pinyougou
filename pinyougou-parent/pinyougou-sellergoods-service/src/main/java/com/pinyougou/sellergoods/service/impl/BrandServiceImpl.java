package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellerfoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;
    /**
     * 查询所有手机品牌
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageInfo findPage(int pageNum, int pageSize) {
        //分页核心代码
        PageHelper.startPage(pageNum,pageSize);
        //获取所有的商品列表
        /*Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        //将商品列表的总记录数和每页显示的记录设置到PageResult对象中,并返回
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());*/
        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(null);
        PageInfo pageInfo = new PageInfo(tbBrands);
        return pageInfo;
    }

    /**
     * 添加商品
     * @param brand
     */
    @Override
    public void add(TbBrand brand) {
        tbBrandMapper.insert(brand);
    }

    /**
     * 根据id查找品牌
     * @param id
     */
    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改品牌
     * @param brand
     */
    @Override
    public void update(TbBrand brand) {
        tbBrandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 根据id删除品牌
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 条件模糊查询
     * @param brand
     * @return
     */
    @Override
    public PageInfo findByExample(TbBrand brand,int page,int size) {
        //分页查询
        PageHelper.startPage(page,size);
        //条件查询
        TbBrandExample brandExample = new TbBrandExample();
        TbBrandExample.Criteria criteria = brandExample.createCriteria();

        if (brand!=null){
            if (brand.getName() != null && brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(brandExample);
        PageInfo pageInfo = new PageInfo(tbBrands);
        return pageInfo;
    }
}
