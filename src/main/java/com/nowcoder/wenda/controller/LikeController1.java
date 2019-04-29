//package com.nowcoder.wenda.controller;
//
//import com.nowcoder.wenda.model.Comment;
//import com.nowcoder.wenda.model.EntityType;
//import com.nowcoder.wenda.model.HostHolder;
//import com.nowcoder.wenda.service.CommentService;
//import com.nowcoder.wenda.service.LikeService1;
//import com.nowcoder.wenda.util.WendaUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * @author jhc on 2019/4/26
// */
//@Controller
//public class LikeController1 {
//    @Autowired
//    LikeService1 likeService;
//
//    @Autowired
//    HostHolder hostHolder;
//
//    @Autowired
//    CommentService commentService;
//    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
//    @ResponseBody
//    public String like(@RequestParam("commentId") int commentId) {
//        if (hostHolder.get() == null) {
//            return WendaUtil.getJSONString(999);
//        }
//        Comment comment = commentService.getById(commentId);
//        long likeCount = likeService.like(hostHolder.get().getId(), EntityType.ENTITY_QUESTION, commentId);
//        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
//    }
//
//    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
//    @ResponseBody
//    public String dislike(@RequestParam("commentId") int commentId) {
//        if (hostHolder.get() == null) {
//            return WendaUtil.getJSONString(999);
//        }
//
//        long likeCount = likeService.disLike(hostHolder.get().getId(), EntityType.ENTITY_QUESTION, commentId);
//        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
//    }
//}
