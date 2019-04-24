package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.Message;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.model.ViewObject;
import com.nowcoder.wenda.service.MessageService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jhc on 2019/4/24
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/msg/list",method = RequestMethod.GET)
    public String getMessageList(Model model){
        try{
            int localUserId = hostHolder.get().getId();
            List<Message> messages = messageService.getAllConversations(localUserId,0,10);
            List<ViewObject> vos = new ArrayList<ViewObject>();
            for(Message msg : messages){
                ViewObject vo = new ViewObject();
                vo.set("conversation",msg);
                //得到所有发过来的信息的Id
                int targetId = msg.getFromId() == localUserId ? msg.getToId():msg.getFromId();
                User fromUser = userService.getUserById(targetId);
//                vo.set("headUrl",fromUser.getHeadUrl());
                vo.set("unread",messageService.getConversationUnread(localUserId, msg.getConversationId()));
                vo.set("user",fromUser);
                vos.add(vo);
            }
            model.addAttribute("conversations",vos);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取站内信列表失败",e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(value = "/msg/addMessage",method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName")String name,
                             @RequestParam("content")String content){
        try{
            User user = hostHolder.get();
            if(user == null){
                return WendaUtil.getJSONString(999,"用户未登录");
            }
            User user1 = userService.getUserByName(name);
            if(user1 == null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setConversationId(message.getConversationId());
            message.setFromId(user.getId());
            message.setToId(user1.getId());
            message.setCreateDate(new Date());
            message.setHasRead(0);
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("发送消息失败",e.getMessage());
            return WendaUtil.getJSONString(1,"发送站内信失败");
        }
    }

    @RequestMapping(value = "/msg/detail",method = RequestMethod.GET)
    public String getMessageDetail(@RequestParam("conversationId")String conversationId, Model model){
        try{
            List<Message> messages = messageService.getAllMessageByConversationId(conversationId,0,10);
            List<ViewObject> vos = new ArrayList<ViewObject>();
            for(Message message:messages){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                messageService.updateReadStatus(message.getId(),1);
//                vo.set("user",userService.getUserById(message.getFromId()));
                User user = userService.getUserById(message.getFromId());
                if(user == null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                vos.add(vo);
            }
            model.addAttribute("messages",vos);

        }catch (Exception e){
            e.printStackTrace();
            logger.error("展示信息详情页面错误",e.getMessage());

        }
        return "letterDetail";
    }
}
