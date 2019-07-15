package com.pinyougou.page.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class HtmlMessageListener implements MessageListener {

    @Autowired
    private ItemPageServiceImpl itemPageServiceImpl;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage)message;
        try {
            String text = textMessage.getText();
            Boolean b = itemPageServiceImpl.genItemHtml(Long.parseLong(text));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
