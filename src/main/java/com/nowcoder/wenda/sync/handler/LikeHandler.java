package com.nowcoder.wenda.sync.handler;

import com.nowcoder.wenda.model.Message;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.MessageService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.sync.EventHandler;
import com.nowcoder.wenda.sync.EventModel;
import com.nowcoder.wenda.sync.EventType;
import com.nowcoder.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author jhc on 2019/4/29
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
//        System.out.println("handle");
        Message message = new Message();
        message.setFromId(WendaUtil.ANONYMOUS_USERID);
        message.setToId(model.getOwnerId());
        message.setConversationId(message.getConversationId());
        message.setHasRead(1);
        message.setCreateDate(new Date());
        User user = userService.getUserById(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
