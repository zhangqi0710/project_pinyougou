package com.pinyougou.page.service;


public interface ItemPageService {
    /**
     * 生成商品详细页
     * @param goodIds
     * @return
     */
    public Boolean genItemHtml(Long goodIds);

    /**
     * 删除静态模型
     * @param goodIds
     * @return
     */
    public Boolean deleteItemHtml(Long[] goodIds);
}
