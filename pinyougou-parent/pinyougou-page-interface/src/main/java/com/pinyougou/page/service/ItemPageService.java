package com.pinyougou.page.service;

import java.io.IOException;

public interface ItemPageService {
    /**
     * 生成商品详细页
     * @param goodIds
     * @return
     */
    public Boolean genItemHtml(Long goodIds) throws IOException;
}
