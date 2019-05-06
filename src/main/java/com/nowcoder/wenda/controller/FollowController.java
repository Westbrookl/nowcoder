package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.model.*;
import com.nowcoder.wenda.service.CommentService;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.sync.EventModel;
import com.nowcoder.wenda.sync.EventProducer;
import com.nowcoder.wenda.sync.EventType;
import com.nowcoder.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jhc on 2019/5/1
 */
@Controller
public class FollowController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    /**
     * 关注一个用户以后，给该用户发送一个站内信
     * 并且返回该用户关注的总的人数。
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/followUser", method = RequestMethod.POST)
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.get() == null) {
            return WendaUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.get().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.get().getId()).setEntityType(EntityType.ENTITY_USER)
                .setEntityId(userId).setOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.get().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(value = "/unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.get() == null) {
            return WendaUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.get().getId(), EntityType.ENTITY_USER, userId);

        return WendaUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.get().getId(), EntityType.ENTITY_USER)));
    }

    /**
     * 关注一个问题，页面上会显示出关注的人的名称以及头像
     * 返回关注该问题的总的人数
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.get() == null) {
            return WendaUtil.getJSONString(999);
        }
        Question question = questionService.getById(questionId);
        if (question == null) {
            return WendaUtil.getJSONString(1, "问题不存在");
        }
        boolean ret = followService.follow(hostHolder.get().getId(), EntityType.ENTITY_QUESTION, questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.get().getId()).setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityId(questionId).setOwnerId(question.getUserId()));
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("headUrl", hostHolder.get().getHeadUrl());
        info.put("name", hostHolder.get().getName());
        info.put("id", hostHolder.get().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return WendaUtil.getJSONString(ret ? 0 : 1, info);

    }

    @RequestMapping(value = "/unfollowQuestion",method = RequestMethod.POST)
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId")int questionId){
        if(hostHolder.get() == null){
            return WendaUtil.getJSONString(999);
        }
        Question question = questionService.getById(questionId);
        if(question == null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }
        boolean ret = followService.unfollow(hostHolder.get().getId(),EntityType.ENTITY_QUESTION,questionId);
        Map<String,Object> info = new HashMap<String,Object>();
        info.put("id",hostHolder.get().getId());
        info.put("headUrl",hostHolder.get().getHeadUrl());
        return WendaUtil.getJSONString(ret?0:1,info);
    }

    @RequestMapping(value="/user/{uid}/followers",method = RequestMethod.GET)
    public String getFollowers(@PathVariable("uid")int uid, Model model){
        List<Integer> lists = followService.getAllFollowers(EntityType.ENTITY_USER,uid,10);
        model.addAttribute("followers",getUsersInfo(hostHolder.get().getId(),lists));
        model.addAttribute("curUser",hostHolder.get());
        model.addAttribute("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
        return "followers";
    }

    @RequestMapping(value = "/user/{uid}/followees",method = RequestMethod.GET)
    public String getFollowees(@PathVariable("uid")int uid,Model model){
        List<Integer> lists = followService.getAllFollowees(uid,EntityType.ENTITY_USER, 0, 10);
        model.addAttribute("followees",getUsersInfo(uid,lists));
        model.addAttribute("curUser",hostHolder.get());
        model.addAttribute("followeeCount",followService.getFolloweeCount(uid,EntityType.ENTITY_USER));
        return "followees";
    }

    public List<ViewObject> getUsersInfo(int userId,List<Integer> lists){
        List<ViewObject> usersInfos = new ArrayList<ViewObject>();
        for(Integer list : lists){
            User user = userService.getUserById(list);
            if(user == null){
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user",user);
            vo.set("commentCount",commentService.getUserComment(list));
            vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,list));
            vo.set("followeeCount",followService.getFolloweeCount(user.getId(),EntityType.ENTITY_USER));
            vo.set("followed",followService.isFollower(userId,EntityType.ENTITY_USER,list));
            usersInfos.add(vo);
        }
        return usersInfos;
    }


}
