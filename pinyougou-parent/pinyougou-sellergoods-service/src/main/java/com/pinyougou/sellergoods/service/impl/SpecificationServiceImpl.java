package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<TbSpecification> specificationList = specificationMapper.selectByExample(null);
		PageInfo pageInfo = new PageInfo(specificationList);
		return pageInfo;
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//从组合对象中获取规格,添加规格
		TbSpecification specification1 = specification.getSpecification();
		specificationMapper.insert(specification1);


		//从组合对象中获取规格列表,添加列表信息
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		//遍历添加
		for (TbSpecificationOption SpecificationOption : specificationOptionList) {
			//插入规格返回规格ID，然后循环插入规格选项
			SpecificationOption.setSpecId(specification1.getId());
			specificationOptionMapper.insert(SpecificationOption);
		}

	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification) {
		//保存规格
		TbSpecification specification1 = specification.getSpecification();
		specificationMapper.updateByPrimaryKey(specification1);

		//删除原有的规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());
		//指定规格 ID 为条件
		specificationOptionMapper.deleteByExample(example);
		//循环插入规格选项
		for (TbSpecificationOption
				specificationOption : specification.getSpecificationOptionList()) {
			specificationOption.setSpecId(specification.getSpecification().getId());
			specificationOptionMapper.insert(specificationOption);
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		//根据id查询specification
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);

		//根据specification的id条件查询specificationOption
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);

		//构建组合实体类返回结果
		Specification spec=new Specification();
		spec.setSpecification(tbSpecification);
		spec.setSpecificationOptionList(tbSpecificationOptions);
		return spec;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);

			//删除原有的规格选项
			TbSpecificationOptionExample example=new TbSpecificationOptionExample();
			com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			//指定规格 ID 为条件
			criteria.andSpecIdEqualTo(id);
			//删除
			specificationOptionMapper.deleteByExample(example);
		}		
	}

	/**
	 * 分页条件查询
	 * @param specification
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
			if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 查询所有的规格,name属性名改为text
	 * @return
	 */
	@Override
	public List<Map> selectOptionList() {
		List<Map> selectOptionList = specificationMapper.selectOptionList();
		return selectOptionList;
	}
	
}
