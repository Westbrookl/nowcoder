package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.MessageDao;
import com.nowcoder.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jhc on 2019/4/24
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;
    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return  messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getAllMessageByConversationId(String conversationId,int offset,int limit){
        return messageDao.getAllMessageByConversationId(conversationId,offset,limit);
    }

    public List<Message> getAllConversations(int userId,int offset,int limit){
        return messageDao.getAllConversations(userId,offset,limit);
    }


    public int getConversationUnread(int userId,String conversationId){
        return messageDao.getConversationUnread(userId,conversationId);
    }

    public int updateReadStatus(int messageId,int status){
        return messageDao.updateReadStatus(messageId,status);
    }
}
