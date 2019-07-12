package com.pinyougou.sellergoods.service.impl;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import entity.Result;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Transactional
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbItemMapper itemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		goods.getTbGoods().setAuditStatus("0");//设置未申请状态
		goodsMapper.insert(goods.getTbGoods());
		//获取tbGoods的id,设置到tbGoodsDesc中,连接两表的关系
		goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());
		goodsDescMapper.insert(goods.getTbGoodsDesc());
		//SkU列表插入
		//添加新的 sku 列表数据
		saveItemList(goods);
	}

	//抽取方法,抽取没有启用规格的共同部分
	private void setItemValus(Goods goods, TbItem item) {
		//商品 SPU 编号
		item.setGoodsId(goods.getTbGoods().getId());
		//商家编号
		item.setSellerId(goods.getTbGoods().getSellerId());
		//商品分类编号（3 级）
		item.setCategoryid(goods.getTbGoods().getCategory3Id());
		//创建日期
		item.setCreateTime(new Date());
		//修改日期
		item.setUpdateTime(new Date());

		//品牌名称
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
		item.setBrand(brand.getName());

		//分类名称
		TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(goods.getTbGoods().getCategory3Id());
		String name = tbItemCat.getName();
		item.setCategory(name);

		//商家名称
		TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId());
		String nickName = tbSeller.getNickName();
		item.setSeller(nickName);

		//图片地址（取第一个图片）
		List<Map> imageList = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(),Map.class);
		if(imageList.size()>0){
			item.setImage((String) imageList.get(0).get("url"));
		}
	}


	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		//设置未申请状态:如果是经过修改的商品，需要重新设置状态
		goods.getTbGoods().setAuditStatus("0");
		//保存商品表
		goodsMapper.updateByPrimaryKey(goods.getTbGoods());
		//保存商品扩展表
		goodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());

		//删除原有的 sku 列表数据
		TbItemExample example=new TbItemExample();
		com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getTbGoods().getId());
		itemMapper.deleteByExample(example);

		//添加新的 sku 列表数据
		saveItemList(goods);
	}

	//创建一个私有方法,插入SKU列表
	private void saveItemList(Goods goods){
		//SkU列表插入
		if ("1".equals(goods.getTbGoods().getIsEnableSpec())){
			//设置itemList的值
			//遍历itemList
			for (TbItem item: goods.getItemList()){
				//标题
				//SPU的名字
				String title = goods.getTbGoods().getGoodsName();
				//规格,规格选项名

				String spec = item.getSpec();
				Map<String,Object> maps = JSON.parseObject(spec);
				for (String map : maps.keySet()) {
					title += " " + maps.get(map);
				}
				item.setTitle(title);
				setItemValus(goods, item);

				itemMapper.insert(item);
			}
		}else {
			TbItem item = new TbItem();
			item.setTitle(goods.getTbGoods().getGoodsName());//商品 KPU+规格描述串作为SKU 名称
			//设置价格
			item.setPrice(goods.getTbGoods().getPrice());
			//设置库存
			item.setNum(9999);
			//设置状态
			item.setStatus("1");
			//设置默认
			item.setIsDefault("1");
			//设置spec属性
			item.setSpec("{}");

			setItemValus(goods,item);

			itemMapper.insert(item);

		}
	}


	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){
		Goods goods = new Goods();
		//获取goods对象的属性值
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setTbGoods(tbGoods);

		//获取goodDesc对象的属性值
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setTbGoodsDesc(tbGoodsDesc);

		//获取itemsList对象的属性值
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> tbItems = itemMapper.selectByExample(example);
		goods.setItemList(tbItems);
		return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids) {
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(goods);
		}
	}

	/**
	 * 条件分页查询
	 * @param goods
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		//指定条件为未逻辑删除记录
		criteria.andIsDeleteIsNull();
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
		}
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 运营商审核
	 * @param ids
	 * @param status
	 */
	@Override
	public void updateStatus(Long[] ids, String status) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	/**
	 * 根据SPU的ID集合和状态获取SKU
	 * @param goodIds
	 * @param status
	 * @return
	 */
	@Override
	public List<TbItem> findItemByGoodIdsAndStatus(Long[] goodIds, String status) {

		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andGoodsIdIn(Arrays.asList(goodIds));
		List<TbItem> itemList = itemMapper.selectByExample(example);
		return itemList;
	}
}
