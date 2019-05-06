package com.nowcoder.wenda.sync.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.Feed;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.FeedService;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.sync.EventHandler;
import com.nowcoder.wenda.sync.EventModel;
import com.nowcoder.wenda.sync.EventType;
import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.jar.JarEntry;

/**
 * @author jhc on 2019/5/3
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    FeedService feedService;
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    QuestionService questionService;

    public String buildData(EventModel model){
      Map<String,String> map = new HashMap<String,String>();
      User user = userService.getUserById(model.getActorId());
      if(user == null){
          return null;
      }
      map.put("userId",String.valueOf(user.getId()));
      map.put("headUrl",user.getHeadUrl());
      map.put("userName",user.getName());
      if(model.getType()== EventType.COMMENT ||
              (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){
          Question question = questionService.getById(model.getEntityId());
          if(question == null){
              return null;
          }
          map.put("questionId",String.valueOf(question.getId()));
          map.put("questionTitle",String.valueOf(question.getTitle()));
          return JSONObject.toJSONString(map);
      }
      return null;
    }

    @Override
    public void doHandle(EventModel model) {
       Feed feed = new Feed();
       feed.setUserId(model.getActorId());
       feed.setCreateDate(new Date());
       feed.setType(model.getType().getValue());
       feed.setData(buildData(model));
       if(feed.getData() == null){
           return ;
       }
       feedService.addFeed(feed);
       List<Integer> followers = followService.getAllFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
       followers.add(0);
       for(Integer i : followers){
           String timelineKey = RedisKeyUtil.getTimelineKey(i);
           jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
       }

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[]{EventType.FOLLOW,EventType.COMMENT});
    }
}
