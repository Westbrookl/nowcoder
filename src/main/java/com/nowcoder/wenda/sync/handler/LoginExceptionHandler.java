package com.nowcoder.wenda.sync.handler;

import com.nowcoder.wenda.sync.EventHandler;
import com.nowcoder.wenda.sync.EventModel;
import com.nowcoder.wenda.sync.EventType;
import com.nowcoder.wenda.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jhc on 2019/4/29
 */
@Service
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //判断逻辑
        Map<String,Object> map = new HashMap<String,Object>();
//      //  System.out.println("1111111");
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("mail"),"登录测试","mails/login_exception.html",map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
