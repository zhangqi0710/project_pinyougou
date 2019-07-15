package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;
import java.util.Arrays;
@Component
public class DeleteSolrMessageListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message) {

        ObjectMessage objectMessage = (ObjectMessage)message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("ItemDeleteListener 监听接收到消息..."+goodsIds);
            itemSearchService.deleteSolr(Arrays.asList(goodsIds));
            System.out.println("成功删除索引库中的记录");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
