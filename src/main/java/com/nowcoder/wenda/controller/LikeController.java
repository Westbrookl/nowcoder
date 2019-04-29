package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.dao.CommentDao;
import com.nowcoder.wenda.model.Comment;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.service.LikeService;
import com.nowcoder.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jhc on 2019/4/26
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentDao commentDao;
    @Autowired
    LikeService likeService;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if(hostHolder.get() == null){
            WendaUtil.getJSONString(999);
        }
        Comment comment = commentDao.getById(commentId);

        long likeCount = likeService.like(hostHolder.get().getId(),comment.getEntityType(),comment.getEntityId());
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId")int commentId){
        if(hostHolder.get() == null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment = commentDao.getById(commentId);
        long dislike = likeService.dislike(hostHolder.get().getId(),comment.getEntityType(),comment.getEntityId());
        return WendaUtil.getJSONString(0,String .valueOf(dislike));
    }
}
