package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
    /**
     * 搜索方法,传递一个Map集合,返回一个Map集合
     * @param searchMap     前台发送的搜索条件类型可能是多个
     * @return              后台返回给前台的搜索结果也包含多种类型
     */
    public Map search(Map searchMap);

    /**
     *  更新solr索引库
     * @param list
     */
    public void updateSolr(List list);

    /**
     * 删除solr索引库
     * @param ids
     */
    public void deleteSolr(List ids);
}
