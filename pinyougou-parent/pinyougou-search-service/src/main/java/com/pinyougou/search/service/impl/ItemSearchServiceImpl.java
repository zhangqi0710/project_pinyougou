package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;

@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map search(Map searchMap) {
        //处理关键字空格问题
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords", keywords.replace(" ", ""));

        Map map = new HashMap();

        //1.查询所有商品的SKU信息
        Map itemList = searchList(searchMap);
        map.putAll(itemList);

        //2.查询分类列表
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //3.查询商品类别的品牌和规格列表
        String categoryName = (String) searchMap.get("category");
        if (!"".equals(categoryName)){
            //点击商品分类查询
            map.putAll(searchBrandAndSpecList(categoryName));
        }else {
            //未点击商品分类查询
            map.putAll(searchBrandAndSpecList(categoryList.get(0)));
        }
        return map;
    }


    /**
     * 设置高亮
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        Map map = new HashMap<>();

        //高亮选项初始化
        HighlightQuery query = new SimpleHighlightQuery();
        //设置给item_title域添加高亮
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        //为查询对象设置高亮选项
        query.setHighlightOptions(highlightOptions);

        //封装关键字,进行查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //过滤品牌分类查询
        if (!searchMap.get("category").equals("")){
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery1 = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery1);
        }
        //过滤品牌查询
        if (!searchMap.get("brand").equals("")){
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery1 = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery1);
        }
        //过滤规格查询
        if (searchMap.get("spec")!= null){
            //获取规格列表
            Map<String,String> spec = (Map) searchMap.get("spec");
            for (String key : spec.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(spec.get(key));
                FilterQuery filterQuery1 = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery1);
            }
        }
        //价格过滤查询
        if (!searchMap.get("price").equals("")){
            String priceStr = (String) searchMap.get("price");
            String[] price = priceStr.split("-");
            //如果最低价等于零,只需要过滤最高价
            if (!price[0].equals("0")){
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
                FilterQuery filterQuery1 = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery1);
            }
            //如果最最高价为*,也就是说没有最高价,只需要过滤最低价格
            if (!price[1].equals("*")){
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
                FilterQuery filterQuery1 = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery1);
            }
        }


        //分页查询
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if ("".equals(pageNo)){
            pageNo = 1;
        }
        if ("".equals(pageSize)){
            pageSize = 40;
        }
        //设置当前页
        query.setOffset((pageNo-1)*pageSize);
        //设置每页显示的记录数
        query.setRows(pageSize);

        //排序
        String sortField = (String) searchMap.get("sortField");
        String sortValue = (String) searchMap.get("sort");
        if (!sortField.equals("") || !sortValue.equals("")){
            if (sortValue.equals("ACE")){
                //升序
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);
            }
            if (sortValue.equals("DESC")){
                //升序
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);
            }
        }


        //返回高亮页对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮入口集合(显示高亮记录集合)
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
        //遍历得到每条显示高亮的记录
        for (HighlightEntry<TbItem> entry : entryList) {
            //获取每条记录显示高亮的域的集合(本案例只有一个域)
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            //遍历显示高亮的列的集合
            /*for (HighlightEntry.Highlight highlight : highlightList) {
                //每个列(域)可能会有多个片段或者是多值,所以返回一个List
                List<String> sns = highlight.getSnipplets();
                System.out.println(sns);
            }*/
            //item_title域只有一个值,所以直接get(0)
            if (highlightList.size()>0 && highlightList.get(0).getSnipplets().size()>0){
                TbItem item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }
        map.put("rows",page.getContent());

        //获取总记录数
        map.put("total",page.getTotalElements());
        //获取总页数
        map.put("totalPages",page.getTotalPages());

        return map;
    }


    /**
     * 分组查询商品分类列表
     * @param searchMap
     * @return
     */
    private List<String> searchCategoryList(Map searchMap){
        List<String> list = new ArrayList<String>();

        Query query = new SimpleQuery("*:*");
        //关键字查询  where
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //获取关键字查询分组页(可以包含多个分组对象)
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);


        //获取分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //获取分组入口页,用于获取数据
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
        //entry=分类名称+分类结果（商品数量等）
        for (GroupEntry<TbItem> entry : entryList) {
            //获取分组结果,将分组的结果添加到返回值中
            String groupValue = entry.getGroupValue();
            list.add(groupValue);
        }
        return  list;
    }


    /**
     * 根据分类名称在缓存中查询品牌和规格列表
     * @param category
     * @return
     */
    private Map searchBrandAndSpecList(String category){
        Map map = new HashMap();
        //在缓存中获取模板ID
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);

        if (typeId != null){
            //根据模板ID获取品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList",brandList);
            //根据模板ID获取规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList",specList);
        }
        return map;
    }

    /**
     * 更新solr索引库
     * @param list
     */
    @Override
    public void updateSolr(List list){
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    /**
     * 删除solr索引库
     * @param ids
     */
    @Override
    public void deleteSolr(List ids) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").in(ids);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
