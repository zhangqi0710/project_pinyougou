package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class DeleteHtmlMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long[] goodsId = (Long[]) objectMessage.getObject();
            System.out.println("ItemDeleteListener 监听接收到消息..." + goodsId);
            Boolean b = itemPageService.deleteItemHtml(goodsId);
            System.out.println("网页删除结果：" + b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
