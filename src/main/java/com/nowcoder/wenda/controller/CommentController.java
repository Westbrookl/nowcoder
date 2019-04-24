package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.dao.CommentDao;
import com.nowcoder.wenda.model.Comment;
import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.service.SensitiveService;
import com.nowcoder.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * @author jhc on 2019/4/23
 */
@Controller
public class CommentController {
    private static Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentDao commentDao;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(value="/addComment",method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId")int questionId,
                             @RequestParam("content")String content){
        try{
            content = HtmlUtils.htmlEscape(content);
            content = sensitiveService.filter(content);
            Comment comment = new Comment();
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setContent(content);
            if(hostHolder.get() == null){
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{
                comment.setUserId(hostHolder.get().getId());
            }
            comment.setCreateDate(new Date());
            comment.setEntityId(questionId);
            comment.setStatus(0);
            commentDao.addComment(comment);
        }catch (Exception e){
            e.printStackTrace();
            log.error("添加评论失败",e.getMessage());
        }
        return "redirect:/question/" + String.valueOf(questionId);
    }
}
